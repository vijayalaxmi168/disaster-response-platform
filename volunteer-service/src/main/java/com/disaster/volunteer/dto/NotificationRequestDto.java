package com.disaster.volunteer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDto {
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String message;
    private String type;
}
