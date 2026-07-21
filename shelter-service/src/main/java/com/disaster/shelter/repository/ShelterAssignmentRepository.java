package com.disaster.shelter.repository;

import com.disaster.shelter.entity.ShelterAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShelterAssignmentRepository extends JpaRepository<ShelterAssignment, Long> {
    List<ShelterAssignment> findByShelterId(Long shelterId);
}
