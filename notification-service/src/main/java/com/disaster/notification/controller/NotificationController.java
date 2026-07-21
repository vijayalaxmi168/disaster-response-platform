package com.disaster.notification.controller;

import com.disaster.notification.dto.NotificationRequest;
import com.disaster.notification.entity.Notification;
import com.disaster.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Service", description = "Sends email notifications to volunteers and citizens")
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<Notification> send(@Valid @RequestBody NotificationRequest request) {
        return new ResponseEntity<>(notificationService.send(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }

    @GetMapping("/recipient/{email}")
    public ResponseEntity<List<Notification>> getByRecipient(@PathVariable String email) {
        return ResponseEntity.ok(notificationService.getByRecipient(email));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Notification Service is UP");
    }
}
