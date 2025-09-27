package com.example.Timely.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Service.Parsers.TimetableService;

@RestController
@RequestMapping("/timetable")
public class TimeTableController {

    @Autowired
    TimetableService timetableService;

    @Autowired
    ClassSlotRepo classSlotRepo;

    @GetMapping("/")
    public String getTimeTable() {
        return "This is the timetable endpoint.";
    }

    @GetMapping("/save")
    public String uploadTimetable() {
        
        timetableService.processAndSaveAllHtmlTimetables();
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

    
}
