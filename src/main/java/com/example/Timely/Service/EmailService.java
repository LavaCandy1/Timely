package com.example.Timely.Service;

import java.util.List;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String body) {
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            System.out.println("Email sent to: " + to);
        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendMassEmail(List<String> recipients, String subject, String body
        ,String instructor
        ) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String[] to = recipients.toArray(new String[0]);
            if (to.equals(null)) {
                System.out.println("No recipients found for mass email.");
                return;                
            }
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);

            if (instructor != null && !instructor.trim().isEmpty()) {
                message.setBcc(instructor);
                System.out.println("BCCing instructor: " + instructor);
            }
            
            mailSender.send(message);
            System.out.println("Mass email sent to: " + String.join(", ", to));
        } catch (Exception e) {
            System.out.println("Failed to send mass email: " + e.getMessage());
        }
    }

}
