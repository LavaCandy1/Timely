package com.example.Timely.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.dto.timetableDTO.AdminTimetableDTO;
import com.example.Timely.Models.dto.timetableDTO.AddClassDTO;
import com.example.Timely.Models.dto.timetableDTO.CancelClassDTO;
import com.example.Timely.Models.dto.timetableDTO.DeleteClassDTO;
import com.example.Timely.Models.dto.timetableDTO.TeacherTimetableDTO;
import com.example.Timely.Models.dto.timetableDTO.UpdateClassDTO;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Repository.Projections.AdminTimetableProjection;
import com.example.Timely.Service.TimetableService;
import com.example.Timely.Service.Parsers.TimetableSavingService;

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
    public ResponseEntity<List<TeacherTimetableDTO>> getTeacherTimetable() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

        if (name == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<TeacherTimetableDTO> classSlots = timetable.getTeacherTimetable(name);
        return ResponseEntity.ok(classSlots);
    }

    @PostMapping("/cancelClass")
    public ResponseEntity<Void> cancelClass(@RequestBody CancelClassDTO classToCancel){
        
        timetable.cancelClass(classToCancel);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/student")
    public ResponseEntity<List<ClassSlot>> getMyTimetable() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();

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

    // admin controller

    @GetMapping("/admin/teacher/{teacher}")
    public ResponseEntity<List<AdminTimetableProjection>> getTeacherTimetableForAdmin(@PathVariable String teacher) {
        List<AdminTimetableProjection> adminTimetableDTOs = classSlotRepo.findTeacherTimetableForAdmin(teacher);
        // adminTimetableDTOs.forEach(dto -> dto.setInstructor(teacher));
        return ResponseEntity.ok(adminTimetableDTOs);
    }

    @GetMapping("/admin/batch/{year}/{batch}")
    public ResponseEntity<List<AdminTimetableDTO>> getBatchTimetableForAdmin(@PathVariable String year, @PathVariable String batch) {
        batch = batch.toUpperCase();

        // TODO: need to take care of year and then use findAllByYearAndBatchOrderByDayOfWeekAscStartTimeAsc
        // currently IN DB year is saved as FOURTH as well as 4th ... need to use only one format and update the DB accordingly

        List<ClassSlot> classSlots = classSlotRepo.findAllByBatch(batch);
        List<AdminTimetableDTO> adminTimetableDTOs = classSlots.stream()
            .map(AdminTimetableDTO::new)
            .toList();

        return ResponseEntity.ok(adminTimetableDTOs);
    }

    @PostMapping("/admin/addClass")
    public ResponseEntity<Void> addClass(@RequestBody AddClassDTO newClass){

        // TODO
        // add clash checking later
        // check for empty of invalid / null entires (must)
        timetable.addClass(newClass.toEntity());
        return ResponseEntity.ok().build();

    }

    @PostMapping("/admin/deleteClass")
    public ResponseEntity<Void> deleteClass(@RequestBody DeleteClassDTO classToDelete){

        // timetable.deleteClass(classToDelete);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/admin/updateClass")
    public ResponseEntity<Integer> updateClass (@RequestBody UpdateClassDTO updateClass) {
        // TODO
        // check for empty of invalid / null entires (must)

        int updatedClasses = timetable.updateClass(updateClass.getIds(), updateClass.getSlot());
        return ResponseEntity.ok(updatedClasses);
    }

}
