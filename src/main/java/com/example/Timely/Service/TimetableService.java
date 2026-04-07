package com.example.Timely.Service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.example.Timely.Events.ClassCancelEvent;
import com.example.Timely.Events.TimetableUpdateEvent;
import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.Student;
import com.example.Timely.Models.dto.timetableDTO.CancelClassDTO;
import com.example.Timely.Models.dto.timetableDTO.DeleteClassDTO;
import com.example.Timely.Models.dto.timetableDTO.TeacherTimetableDTO;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Repository.StudentRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final ClassSlotRepo classSlotRepo;
    private final StudentRepo studentRepo;
    private final ApplicationEventPublisher eventPublisher;

    public List<ClassSlot> getTimeTableForStudent(String enrollmentNumber) throws Exception {
        Student stud = studentRepo.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new Exception("Student not found"));

        String batch = stud.getBatch();
        List<String> courses = stud.getCourseList();

        List<ClassSlot> classSlots = classSlotRepo.findAllByBatchAndCourseCodeIn(batch, courses).orElse(List.of());

        return classSlots;
    }

    public List<TeacherTimetableDTO> getTeacherTimetable(String teacherName){

        return classSlotRepo.findTeacherTimetable(teacherName);
            // .stream()
            // .map(
            //     slot -> {
            //         teachertimetableDTO dto = new teachertimetableDTO();
            //         dto.setCourseCode(slot.getCourseCode());
            //         dto.setStartTime(slot.getStartTime());
            //         dto.setDayOfWeek(slot.getDayOfWeek());
            //         dto.setLocation(slot.getLocation());
            //         dto.setBatches(slot.);
            //     }
            // )

    }

    public void cancelClass(CancelClassDTO classToCancel) {
        
        List<String> batches = classToCancel.getBatches();
        Date cancelledDate = classToCancel.getCancelledDate();
        String courseCode = classToCancel.getCourseCode();
        String dayOfWeek = classToCancel.getDayOfWeek();
        String location = classToCancel.getLocation();
        LocalTime startTime = classToCancel.getStartTime();

        int updatedCount = classSlotRepo.updateCancellationDateForSlots(
            cancelledDate, batches, courseCode, dayOfWeek, startTime, location
        );

        System.out.println("Number of class slots updated: " + updatedCount);
        eventPublisher.publishEvent(new ClassCancelEvent(this,batches, cancelledDate, courseCode,dayOfWeek,startTime));
        System.out.println("event published");
 
    }

    public void addClass(ClassSlot newClass) {
        if (newClass == null) {
            throw new IllegalArgumentException("ClassSlot cannot be null");
        }
        classSlotRepo.save(newClass);
        
        // eventPublisher.publishEvent(new TimetableUpdateEvent(this, newClass, "ADDED"));
    }

    public void deleteClass(DeleteClassDTO classToDelete) {

        System.out.println(classToDelete);
        List<String> batches = classToDelete.getBatches();
        String courseCode = classToDelete.getCourseCode();
        String dayOfWeek = classToDelete.getDayOfWeek();
        Time time = classToDelete.getStartTime();
        
        int deletedCount = classSlotRepo.deleteClassSlots(
            classToDelete.getBatches(),
            classToDelete.getCourseCode(),
            classToDelete.getDayOfWeek(),
            classToDelete.getStartTime(),
            classToDelete.getLocation(),
            classToDelete.getSlotType(),
            classToDelete.getInstructor()
        );

        System.out.println("Number of class slots deleted: " + deletedCount);
        
    }

    public void updateClass(String[] updatedClassIDs) {
        
        
                           
    }

}
