package com.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${admin.email}")
    private String adminEmail;

    public void sendCandidateConfirmationEmail(String candidateEmail, String candidateName) {
        if (mailSender == null) {
            System.out.println("Email service not configured. Would send confirmation email to: " + candidateEmail);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(candidateEmail);
            message.setSubject("Candidate Registration Confirmation");
            message.setText("Dear " + candidateName + ",\n\n" +
                    "Your candidate registration has been successfully completed.\n\n" +
                    "Thank you for registering with our Candidate Management System.\n\n" +
                    "Best regards,\n" +
                    "Candidate Management Team");

            mailSender.send(message);
            System.out.println("Confirmation email sent to: " + candidateEmail);
        } catch (Exception e) {
            System.err.println("Error sending email to " + candidateEmail + ": " + e.getMessage());
        }
    }

    public void sendAdminSummary(long count) {
        if (mailSender == null) {
            System.out.println("Email service not configured. Would send admin summary: " + count
                    + " candidates added in last 24 hours");
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);
            message.setSubject("Daily Candidate Summary");
            message.setText("Dear Admin,\n\n" +
                    "Total candidates added in the last 24 hours: " + count + "\n\n" +
                    "This is an automated daily summary.\n\n" +
                    "Best regards,\n" +
                    "Candidate Management System");

            mailSender.send(message);
            System.out.println("Admin summary email sent to: " + adminEmail);
        } catch (Exception e) {
            System.err.println("Error sending admin summary: " + e.getMessage());
        }
    }
}
