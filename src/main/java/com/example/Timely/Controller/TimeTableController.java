package com.example.Timely.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.dto.timetableDTO.teachertimetableDTO;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Service.TimetableService;
import com.example.Timely.Service.Parsers.TimetableSavingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/timetable")
public class TimeTableController {

    @Autowired
    TimetableSavingService timetableSavingService;

    @Autowired
    ClassSlotRepo classSlotRepo;

    @Autowired
    TimetableService timetable;

    @GetMapping("/")
    public String getTimeTable() {
        return "This is the timetable endpoint.";
    }

    @GetMapping("/save")
    public String uploadTimetable() {

        timetableSavingService.processAndSaveAllHtmlTimetables();
        return "Timetable uploaded successfully!";
    }

    @GetMapping("/{batch}")
    public ResponseEntity<List<ClassSlot>> getTimetableByBatch(@PathVariable String batch) {
        batch = batch.toUpperCase();
        List<ClassSlot> classSlots = classSlotRepo.findAllByBatch(batch);
        return ResponseEntity.ok(classSlots);
    }

    @GetMapping("/teacher/{teacher}")
    public ResponseEntity<List<ClassSlot>> getTimetableByTeacher(@PathVariable String teacher) {

        List<ClassSlot> classSlots = classSlotRepo.findAllByInstructor(teacher);
        return ResponseEntity.ok(classSlots);
    }

    @GetMapping("/teacher")
    public ResponseEntity<List<teachertimetableDTO>> getTeacherTimetable(HttpServletRequest request) {
        System.out.println("Here");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        System.out.println(name);

        if (name == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // List<ClassSlot> classSlots = classSlotRepo.findAllByInstructor(name);
        List<teachertimetableDTO> classSlots = timetable.getTeacherTimetable(name);
        System.out.println(classSlots);
        return ResponseEntity.ok(classSlots);

    }

    @GetMapping("/student")
    public ResponseEntity<List<ClassSlot>> getMyTimetable(HttpServletRequest request) {
        System.out.println("Here");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        System.out.println(name);

        if (name == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
        List<ClassSlot> classSlots = timetable.getTimeTableForStudent(name);
        return ResponseEntity.ok(classSlots);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

}
