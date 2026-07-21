package com.disaster.rescue.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateRescueRequestDto {
    @NotNull(message = "Citizen id is required")
    private Long citizenId;

    @NotBlank(message = "Citizen name is required")
    private String citizenName;

    @NotBlank(message = "Citizen email is required")
    private String citizenEmail;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;
}
