package com.disaster.volunteer.dto;

import lombok.Data;

// Minimal mirror of rescue-request-service's RescueRequest, only the fields
// volunteer-service needs when assigning a volunteer.
@Data
public class RescueRequestDto {
    private Long id;
    private Long citizenId;
    private String citizenName;
    private String citizenEmail;
    private String description;
    private String location;
    private String status;
    private Long volunteerId;
    private String volunteerName;
}
