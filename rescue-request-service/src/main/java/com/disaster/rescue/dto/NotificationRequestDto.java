package com.disaster.rescue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Mirrors notification-service's NotificationRequest so this service doesn't
// need a shared library dependency between microservices.
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
