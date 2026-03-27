package com.example.Timely.Listeners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.example.Timely.Events.ClassCancelEvent;
import com.example.Timely.Events.TimetableUpdateEvent;
import com.example.Timely.Repository.StudentRepo;
import com.example.Timely.Service.EmailService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class HandleTimetableUpdate {

    private final EmailService emailService;
    private final StudentRepo studentRepo;

    @Async
    @EventListener
    public void onTimetableUpdate(TimetableUpdateEvent event) {
        // Log the update for debugging
        System.out.println("Received timetable update event: " + event.getUpdateType() + " for slot: " + event.getUpdatedSlot() + ", at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(event.getTimestamp()) + ". Notifying students...");
        
        String batch = event.getUpdatedSlot().getBatch();
        List<String> studentEmails = studentRepo.findEmailsByBatch(batch).orElse(new ArrayList<>());
        if (studentEmails.isEmpty()) {
            System.out.println("No students found for batch: " + batch);
            return;
        }

        String subject = "Timetable Updated";
        String body = String.format("""
                        Dear Student,

                        The timetable for your batch has been updated.
                        Change: Class %s for %s on %s, %s.
                        
                        Please check the Timely app for the latest schedule.

                        Regards,
                        Timely Team
                        """,event.getUpdateType().toLowerCase(),
                            event.getUpdatedSlot().getCourseCode(), 
                            event.getUpdatedSlot().getDayOfWeek(), 
                            event.getUpdatedSlot().getStartTime().toString()
                        );
        String instructorEmail = "LavaCandy.alt2@gmail.com"; // Replace with actual instructor email if available
        emailService.sendMassEmail(studentEmails, subject, body, instructorEmail);
    }

    @Async
    @EventListener
    public void onClassCancellation(ClassCancelEvent event) {

        System.out.println("listening");

        List<String> studentEmails = new ArrayList<>();
        List<String> batches = event.getBatches();
        for(String batch : batches){
            studentEmails.addAll(studentRepo.findEmailsByBatch(batch).orElse(new ArrayList<>()));
        }

        if (studentEmails.isEmpty()) {
            System.out.println("No students found for batches: " + batches);
            return;
        }

        if (event.getStatus().equals("CANCELLED")) {
            String subject = "Class Cancelled: " + event.getCourseCode();
            String body = String.format("""
                Hi Student,

                The class for %s on %s at %s has been cancelled.
                
                Please check the Timely app for any updates or potential rescheduling.

                Regards,
                The Timely Team
                """, 
                event.getCourseCode(), 
                event.getDayOfWeek(), 
                event.getTime().toString()
            );
            emailService.sendMassEmail(studentEmails, subject, body);
            // System.out.println("Prepared cancellation email for students: " + String.join(", ", studentEmails));
        } else if (event.getStatus().equals("RESCHEDULED")) {
            String subject = "Class Reinstated: "+event.getCourseCode();
            String body = String.format("""
                Dear Student,

                The cancellation for your %s class on %s at %s has been reverted. 
                The class will now proceed as originally scheduled at the same time and day.

                Please check the Timely app for any further updates.

                Regards,
                Timely Team
                """, 
                event.getCourseCode(), 
                event.getDayOfWeek(), 
                event.getTime().toString()
            );
            emailService.sendMassEmail(studentEmails, subject, body);
            // System.out.println("Prepared rescheduling email for students: " + String.join(", ", studentEmails));
        }
        
        return;

    }
}
