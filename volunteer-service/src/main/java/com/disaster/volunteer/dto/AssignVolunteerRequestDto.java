package com.disaster.volunteer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Sent to rescue-request-service to link a volunteer to a rescue request
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignVolunteerRequestDto {
    private Long volunteerId;
    private String volunteerName;
}
