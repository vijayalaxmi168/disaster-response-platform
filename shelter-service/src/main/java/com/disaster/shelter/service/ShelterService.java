package com.disaster.shelter.service;

import com.disaster.shelter.dto.AssignToShelterDto;
import com.disaster.shelter.dto.CreateShelterDto;
import com.disaster.shelter.entity.Shelter;
import com.disaster.shelter.entity.ShelterAssignment;
import com.disaster.shelter.exception.InsufficientCapacityException;
import com.disaster.shelter.exception.ResourceNotFoundException;
import com.disaster.shelter.repository.ShelterAssignmentRepository;
import com.disaster.shelter.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShelterService {

    private final ShelterRepository shelterRepository;
    private final ShelterAssignmentRepository shelterAssignmentRepository;

    public Shelter create(CreateShelterDto dto) {
        Shelter shelter = new Shelter();
        shelter.setName(dto.getName());
        shelter.setLocation(dto.getLocation());
        shelter.setTotalCapacity(dto.getTotalCapacity());
        shelter.setAvailableCapacity(dto.getTotalCapacity());
        return shelterRepository.save(shelter);
    }

    public Shelter getById(Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shelter not found with id: " + id));
    }

    public List<Shelter> getAll() {
        return shelterRepository.findAll();
    }

    @Transactional
    public Shelter assignPeople(Long shelterId, AssignToShelterDto dto) {
        Shelter shelter = getById(shelterId);

        if (shelter.getAvailableCapacity() < dto.getNumberOfPeople()) {
            throw new InsufficientCapacityException(
                    "Shelter '" + shelter.getName() + "' only has " + shelter.getAvailableCapacity()
                            + " spot(s) left, requested " + dto.getNumberOfPeople());
        }

        shelter.setAvailableCapacity(shelter.getAvailableCapacity() - dto.getNumberOfPeople());
        Shelter saved = shelterRepository.save(shelter);

        ShelterAssignment assignment = new ShelterAssignment();
        assignment.setShelterId(shelterId);
        assignment.setRescueRequestId(dto.getRescueRequestId());
        assignment.setPersonName(dto.getPersonName());
        assignment.setNumberOfPeople(dto.getNumberOfPeople());
        shelterAssignmentRepository.save(assignment);

        return saved;
    }

    public List<ShelterAssignment> getAssignments(Long shelterId) {
        getById(shelterId); // ensures shelter exists
        return shelterAssignmentRepository.findByShelterId(shelterId);
    }
}
