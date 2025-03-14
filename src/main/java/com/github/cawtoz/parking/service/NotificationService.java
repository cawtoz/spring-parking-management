package com.github.cawtoz.parking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private JavaMailSender mailSender;

    private final String FROM_EMAIL = "autopark@gmail.com"; // Cambia esto por tu correo

    @Async
    public void sendEmail(String recipient, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(FROM_EMAIL);
            mailMessage.setTo(recipient);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);

            logger.info("Correo enviado a {} con asunto: {}", recipient, subject);
        } catch (Exception e) {
            logger.error("Error al enviar correo a {}: {}", recipient, e.getMessage());
        }
    }

}
