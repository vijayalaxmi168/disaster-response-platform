package com.disaster.volunteer.repository;

import com.disaster.volunteer.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {
    List<Volunteer> findByAvailable(Boolean available);
}
