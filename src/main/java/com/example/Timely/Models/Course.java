package com.example.Timely.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String courseName;
    String courseCode;
    String courseType; // core, elective, lab, etc.
    // String teacherName; // will need to store a list here or create a separate entity for teachers and refernce it here


}
