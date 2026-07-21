package com.disaster.volunteer.controller;

import com.disaster.volunteer.dto.CreateVolunteerDto;
import com.disaster.volunteer.dto.RescueRequestDto;
import com.disaster.volunteer.entity.Volunteer;
import com.disaster.volunteer.service.VolunteerService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/volunteers")
@RequiredArgsConstructor
@Tag(name = "Volunteer Service", description = "Manage volunteers and assign them to rescue requests")
public class VolunteerController {

    private final VolunteerService volunteerService;

    @PostMapping
    public ResponseEntity<Volunteer> create(@Valid @RequestBody CreateVolunteerDto dto) {
        return new ResponseEntity<>(volunteerService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Volunteer> getById(@PathVariable Long id) {
        return ResponseEntity.ok(volunteerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Volunteer>> getAll() {
        return ResponseEntity.ok(volunteerService.getAll());
    }

    @GetMapping("/available")
    public ResponseEntity<List<Volunteer>> getAvailable() {
        return ResponseEntity.ok(volunteerService.getAvailable());
    }

    @PutMapping("/{volunteerId}/assign/{requestId}")
    public ResponseEntity<RescueRequestDto> assign(@PathVariable Long volunteerId, @PathVariable Long requestId) {
        return ResponseEntity.ok(volunteerService.assignToRequest(volunteerId, requestId));
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<Volunteer> setAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return ResponseEntity.ok(volunteerService.setAvailability(id, available));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Volunteer Service is UP");
    }
}
