package com.disaster.rescue.repository;

import com.disaster.rescue.entity.RequestStatus;
import com.disaster.rescue.entity.RescueRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RescueRequestRepository extends JpaRepository<RescueRequest, Long> {
    List<RescueRequest> findByStatus(RequestStatus status);
    List<RescueRequest> findByCitizenId(Long citizenId);
}
