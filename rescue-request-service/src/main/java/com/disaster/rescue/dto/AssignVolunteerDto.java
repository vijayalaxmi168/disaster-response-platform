package com.disaster.rescue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// Populated internally by volunteer-service (via Feign) when a volunteer picks up a request
@Data
public class AssignVolunteerDto {
    @NotNull(message = "Volunteer id is required")
    private Long volunteerId;

    @NotBlank(message = "Volunteer name is required")
    private String volunteerName;
}
