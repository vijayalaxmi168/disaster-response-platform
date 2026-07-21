package com.disaster.notification.service;

import com.disaster.notification.dto.NotificationRequest;
import com.disaster.notification.entity.Notification;
import com.disaster.notification.entity.NotificationStatus;
import com.disaster.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Value("${notification.email.mode:simulate}")
    private String emailMode;

    public Notification send(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setRecipientEmail(request.getRecipientEmail());
        notification.setRecipientName(request.getRecipientName());
        notification.setSubject(request.getSubject());
        notification.setMessage(request.getMessage());
        notification.setType(request.getType());

        try {
            if ("real".equalsIgnoreCase(emailMode)) {
                sendRealEmail(request);
            } else {
                // Simulated mode: logs the "email" to the console instead of
                // requiring live SMTP credentials. Flip notification.email.mode
                // to "real" in application.yml to send actual emails.
                log.info("=== SIMULATED EMAIL ===\nTo: {}\nSubject: {}\nMessage: {}\n========================",
                        request.getRecipientEmail(), request.getSubject(), request.getMessage());
            }
            notification.setStatus(NotificationStatus.SENT);
        } catch (Exception ex) {
            log.error("Failed to send notification to {}: {}", request.getRecipientEmail(), ex.getMessage());
            notification.setStatus(NotificationStatus.FAILED);
        }

        return notificationRepository.save(notification);
    }

    private void sendRealEmail(NotificationRequest request) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(request.getRecipientEmail());
        mailMessage.setSubject(request.getSubject());
        mailMessage.setText(request.getMessage());
        mailSender.send(mailMessage);
    }

    public List<Notification> getAll() {
        return notificationRepository.findAll();
    }

    public List<Notification> getByRecipient(String email) {
        return notificationRepository.findByRecipientEmail(email);
    }
}
