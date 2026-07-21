package com.disaster.rescue.service;

import com.disaster.rescue.client.NotificationClient;
import com.disaster.rescue.dto.*;
import com.disaster.rescue.entity.RequestStatus;
import com.disaster.rescue.entity.RescueRequest;
import com.disaster.rescue.exception.ResourceNotFoundException;
import com.disaster.rescue.repository.RescueRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RescueRequestService {

    private final RescueRequestRepository rescueRequestRepository;
    private final NotificationClient notificationClient;

    public RescueRequest create(CreateRescueRequestDto dto) {
        RescueRequest request = new RescueRequest();
        request.setCitizenId(dto.getCitizenId());
        request.setCitizenName(dto.getCitizenName());
        request.setCitizenEmail(dto.getCitizenEmail());
        request.setDescription(dto.getDescription());
        request.setLocation(dto.getLocation());
        request.setStatus(RequestStatus.PENDING);

        RescueRequest saved = rescueRequestRepository.save(request);

        notifySafely(saved.getCitizenEmail(), saved.getCitizenName(),
                "Rescue Request Received",
                "Your rescue request #" + saved.getId() + " at " + saved.getLocation()
                        + " has been received and is PENDING volunteer assignment.",
                "STATUS_UPDATED");

        return saved;
    }

    public RescueRequest getById(Long id) {
        return rescueRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rescue request not found with id: " + id));
    }

    public List<RescueRequest> getAll() {
        return rescueRequestRepository.findAll();
    }

    public List<RescueRequest> getByStatus(RequestStatus status) {
        return rescueRequestRepository.findByStatus(status);
    }

    public List<RescueRequest> getByCitizenId(Long citizenId) {
        return rescueRequestRepository.findByCitizenId(citizenId);
    }

    public RescueRequest updateStatus(Long id, UpdateStatusDto dto) {
        RescueRequest request = getById(id);
        request.setStatus(dto.getStatus());
        RescueRequest saved = rescueRequestRepository.save(request);

        notifySafely(saved.getCitizenEmail(), saved.getCitizenName(),
                "Rescue Request Status Updated",
                "Your rescue request #" + saved.getId() + " status changed to " + saved.getStatus() + ".",
                "STATUS_UPDATED");

        return saved;
    }

    // Called by volunteer-service (via Feign) when a volunteer is assigned to this request
    public RescueRequest assignVolunteer(Long id, AssignVolunteerDto dto) {
        RescueRequest request = getById(id);
        request.setVolunteerId(dto.getVolunteerId());
        request.setVolunteerName(dto.getVolunteerName());
        request.setStatus(RequestStatus.ASSIGNED);
        RescueRequest saved = rescueRequestRepository.save(request);

        notifySafely(saved.getCitizenEmail(), saved.getCitizenName(),
                "Volunteer Assigned",
                "Volunteer " + dto.getVolunteerName() + " has been assigned to your rescue request #" + saved.getId() + ".",
                "STATUS_UPDATED");

        return saved;
    }

    private void notifySafely(String email, String name, String subject, String message, String type) {
        try {
            notificationClient.send(new NotificationRequestDto(email, name, subject, message, type));
        } catch (Exception ex) {
            // Notification failures should never break the core rescue-request workflow
            log.warn("Could not reach notification-service: {}", ex.getMessage());
        }
    }
}
