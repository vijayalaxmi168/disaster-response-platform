package com.disaster.shelter.controller;

import com.disaster.shelter.dto.AssignToShelterDto;
import com.disaster.shelter.dto.CreateShelterDto;
import com.disaster.shelter.entity.Shelter;
import com.disaster.shelter.entity.ShelterAssignment;
import com.disaster.shelter.service.ShelterService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shelters")
@RequiredArgsConstructor
@Tag(name = "Shelter Service", description = "Add shelters, track capacity, assign rescued people")
public class ShelterController {

    private final ShelterService shelterService;

    @PostMapping
    public ResponseEntity<Shelter> create(@Valid @RequestBody CreateShelterDto dto) {
        return new ResponseEntity<>(shelterService.create(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shelter> getById(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Shelter>> getAll() {
        return ResponseEntity.ok(shelterService.getAll());
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Shelter> assignPeople(@PathVariable Long id, @Valid @RequestBody AssignToShelterDto dto) {
        return ResponseEntity.ok(shelterService.assignPeople(id, dto));
    }

    @GetMapping("/{id}/assignments")
    public ResponseEntity<List<ShelterAssignment>> getAssignments(@PathVariable Long id) {
        return ResponseEntity.ok(shelterService.getAssignments(id));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Shelter Service is UP");
    }
}
