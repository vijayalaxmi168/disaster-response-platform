package com.disaster.volunteer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateVolunteerDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    private String phone;

    private String skills;
}
