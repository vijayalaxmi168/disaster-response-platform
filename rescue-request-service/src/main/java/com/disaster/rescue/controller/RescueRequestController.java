package com.disaster.rescue.controller;

import com.disaster.rescue.dto.AssignVolunteerDto;
import com.disaster.rescue.dto.CreateRescueRequestDto;
import com.disaster.rescue.dto.UpdateStatusDto;
import com.disaster.rescue.entity.RequestStatus;
import com.disaster.rescue.entity.RescueRequest;
import com.disaster.rescue.service.RescueRequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rescue-requests")
@RequiredArgsConstructor
@Tag(name = "Rescue Request Service", description = "Citizens create and track emergency requests")
public class RescueRequestController {

    private final RescueRequestService rescueRequestService;

    @PostMapping
    public ResponseEntity<RescueRequest> create(@Valid @RequestBody CreateRescueRequestDto dto) {
        return new ResponseEntity<>(rescueRequestService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RescueRequest> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rescueRequestService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<RescueRequest>> getAll() {
        return ResponseEntity.ok(rescueRequestService.getAll());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<RescueRequest>> getByStatus(@PathVariable RequestStatus status) {
        return ResponseEntity.ok(rescueRequestService.getByStatus(status));
    }

    @GetMapping("/citizen/{citizenId}")
    public ResponseEntity<List<RescueRequest>> getByCitizenId(@PathVariable Long citizenId) {
        return ResponseEntity.ok(rescueRequestService.getByCitizenId(citizenId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<RescueRequest> updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusDto dto) {
        return ResponseEntity.ok(rescueRequestService.updateStatus(id, dto));
    }

    // Internal endpoint called by volunteer-service via Feign
    @PutMapping("/{id}/assign-volunteer")
    public ResponseEntity<RescueRequest> assignVolunteer(@PathVariable Long id, @Valid @RequestBody AssignVolunteerDto dto) {
        return ResponseEntity.ok(rescueRequestService.assignVolunteer(id, dto));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Rescue Request Service is UP");
    }
}
