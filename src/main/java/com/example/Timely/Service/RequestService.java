package com.example.Timely.Service;

import java.util.List;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

import org.springframework.stereotype.Service;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.ExtraClassRequest;
import com.example.Timely.Models.Locations;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Repository.ExtraClassRequestRepo;
import com.example.Timely.Repository.LocationRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final ExtraClassRequestRepo extraClassReqRepo;
    private final LocationRepo locationRepo;
    private final ClassSlotRepo classSlotRepo;
    private final EmailService emailService;
    

    public void submitExtraClassRequest(ExtraClassRequest request) {
        request.setStatus("PENDING");
        request.setCreatedAt(new Date(System.currentTimeMillis()));
        System.out.println("Saving new extra class request: " + request);
        extraClassReqRepo.save(request);
    }

    public List<ExtraClassRequest> getAllExtraClassRequests() {
        return extraClassReqRepo.findAll();
    }

    public void rejectExtraClassRequest(Long id) {
        if (id == null) {
            return;
        }
        if (extraClassReqRepo.existsById(id)) {
            extraClassReqRepo.findById(id).ifPresent(request -> {
                request.setStatus("REJECTED");
                extraClassReqRepo.save(request);
            });
        }
    }

    @Transactional
    public void approveExtraClassRequest(Long id, String location) {
        if (id == null) {
            return;
        }
        System.out.println("Inside service for approval");
        extraClassReqRepo.findById(id).ifPresent(request -> {
            request.setStatus("APPROVED");
            request.setLocation(location);
            extraClassReqRepo.save(request);


            ClassSlot newSlot = new ClassSlot();
            newSlot.setCourseCode(request.getCourseCode());
            newSlot.setDayOfWeek(request.getDayOfWeek());
            newSlot.setStartTime(Time.valueOf(request.getStartTime() + ":00"));
            newSlot.setLocation(location);
            newSlot.setInstructor(request.getInstructor());
            newSlot.setSlotType(ClassSlot.SlotType.valueOf(request.getSlotType().name())); // ughh... dunno if this will work or not
            newSlot.setBatch(request.getBatch());
            newSlot.setGroup(request.getGroup());
            newSlot.setYear(request.getYear());
            
            classSlotRepo.save(newSlot);
        });

        // String instructorEmail = extraClassReqRepo.findById(id).map(ExtraClassRequest::getInstructor).orElse(null);
        String dummyToEmail = "E22CSEU1201@bennett.edu.in"; // Replace with actual email retrieval logic
        String emailSubject = "Your Extra Class Request has been Approved";
        String emailBody = "Dear Instructor,\n\nYour request for an extra class has been approved. The class will be held at " + location + ".\n\nBest regards,\nTimely Team";

        emailService.sendEmail(dummyToEmail, emailSubject, emailBody);
    }

    public List<Locations> getAvailableLocations(Long id) {
        if (id == null) {
            return null;
        }
        ExtraClassRequest request = extraClassReqRepo.findById(id).orElse(null);
        if (request == null) {
            return null;
        }
        String day = request.getDayOfWeek();
        LocalTime startTime = LocalTime.parse(request.getStartTime());

        List<Locations> availableLocations = locationRepo.findAvailableLocations(day, startTime);

        return availableLocations;

    }
}