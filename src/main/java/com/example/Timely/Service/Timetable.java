package com.example.Timely.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.Timely.Models.ClassSlot;
import com.example.Timely.Models.Student;
import com.example.Timely.Repository.ClassSlotRepo;
import com.example.Timely.Repository.StudentRepo;

@Service
public class Timetable {

    private final ClassSlotRepo classSlotRepo;
    private final StudentRepo studentRepo;

    Timetable(ClassSlotRepo classSlotRepo, StudentRepo studentRepo) {
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

    public List<ClassSlot> getTimeTableForTeacher(String teacherName) {

        return List.of();
    }

}
