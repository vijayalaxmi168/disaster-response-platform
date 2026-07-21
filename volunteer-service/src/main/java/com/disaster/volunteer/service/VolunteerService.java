package com.disaster.volunteer.service;

import com.disaster.volunteer.client.NotificationClient;
import com.disaster.volunteer.client.RescueRequestClient;
import com.disaster.volunteer.dto.*;
import com.disaster.volunteer.entity.Volunteer;
import com.disaster.volunteer.exception.ResourceNotFoundException;
import com.disaster.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class VolunteerService {

    private final VolunteerRepository volunteerRepository;
    private final RescueRequestClient rescueRequestClient;
    private final NotificationClient notificationClient;

    public Volunteer create(CreateVolunteerDto dto) {
        Volunteer volunteer = new Volunteer();
        volunteer.setName(dto.getName());
        volunteer.setEmail(dto.getEmail());
        volunteer.setPhone(dto.getPhone());
        volunteer.setSkills(dto.getSkills());
        volunteer.setAvailable(true);
        return volunteerRepository.save(volunteer);
    }

    public Volunteer getById(Long id) {
        return volunteerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Volunteer not found with id: " + id));
    }

    public List<Volunteer> getAll() {
        return volunteerRepository.findAll();
    }

    public List<Volunteer> getAvailable() {
        return volunteerRepository.findByAvailable(true);
    }

    /**
     * Core cross-service workflow:
     * 1. Load the volunteer (must exist and be available)
     * 2. Call rescue-request-service via Feign to link the volunteer + set status ASSIGNED
     * 3. Mark the volunteer unavailable
     * 4. Call notification-service via Feign to email the volunteer
     */
    public RescueRequestDto assignToRequest(Long volunteerId, Long requestId) {
        Volunteer volunteer = getById(volunteerId);

        if (Boolean.FALSE.equals(volunteer.getAvailable())) {
            throw new IllegalStateException("Volunteer " + volunteer.getName() + " is not currently available");
        }

        RescueRequestDto updatedRequest = rescueRequestClient.assignVolunteer(
                requestId, new AssignVolunteerRequestDto(volunteer.getId(), volunteer.getName()));

        volunteer.setAvailable(false);
        volunteerRepository.save(volunteer);

        try {
            notificationClient.send(new NotificationRequestDto(
                    volunteer.getEmail(),
                    volunteer.getName(),
                    "New Rescue Assignment",
                    "You have been assigned to rescue request #" + requestId
                            + " at " + updatedRequest.getLocation() + ". Description: " + updatedRequest.getDescription(),
                    "VOLUNTEER_ASSIGNED"
            ));
        } catch (Exception ex) {
            log.warn("Could not reach notification-service: {}", ex.getMessage());
        }

        return updatedRequest;
    }

    public Volunteer setAvailability(Long id, boolean available) {
        Volunteer volunteer = getById(id);
        volunteer.setAvailable(available);
        return volunteerRepository.save(volunteer);
    }
}
