package com.disaster.shelter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Records that a group of rescued people (optionally tied to a rescue request)
// were placed into a shelter, and how many spots that consumed.
@Entity
@Table(name = "shelter_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShelterAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long shelterId;

    private Long rescueRequestId;

    @Column(nullable = false)
    private String personName;

    @Column(nullable = false)
    private Integer numberOfPeople;

    @Column(updatable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onCreate() {
        this.assignedAt = LocalDateTime.now();
    }
}
