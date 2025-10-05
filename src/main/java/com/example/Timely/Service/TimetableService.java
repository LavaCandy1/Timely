package com.example.Timely.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.Student;
import com.example.Timely.Models.dto.timetableDTO.cancelClassDTO;
import com.example.Timely.Models.dto.timetableDTO.teachertimetableDTO;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Repository.StudentRepo;

@Service
public class TimetableService {

    private final ClassSlotRepo classSlotRepo;
    private final StudentRepo studentRepo;

    TimetableService(ClassSlotRepo classSlotRepo, StudentRepo studentRepo) {
        this.classSlotRepo = classSlotRepo;
        this.studentRepo = studentRepo;
    }

    public List<ClassSlot> getTimeTableForStudent(String enrollmentNumber) throws Exception {
        Student stud = studentRepo.findByEnrollmentNumber(enrollmentNumber)
                .orElseThrow(() -> new Exception("Student not found"));

        String batch = stud.getBatch();
        List<String> courses = stud.getCourseList();

        List<ClassSlot> classSlots = classSlotRepo.findAllByBatchAndCourseCodeIn(batch, courses).orElse(List.of());

        return classSlots;
    }

    public List<teachertimetableDTO> getTeacherTimetable(String teacherName){

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

    public void cancelClass(cancelClassDTO classToCancel) {
        
        List<String> batches = classToCancel.getBatches();
        Date cancelledDate = classToCancel.getCancelledDate();
        String courseCode = classToCancel.getCourseCode();
        String dayOfWeek = classToCancel.getDayOfWeek();
        String location = classToCancel.getLocation();
        Time startTime = classToCancel.getStartTime();

        int updatedCount = classSlotRepo.updateCancellationDateForSlots(
            cancelledDate, batches, courseCode, dayOfWeek, startTime, location
        );

        System.out.println("Number of class slots updated: " + updatedCount);

        
    }

}
