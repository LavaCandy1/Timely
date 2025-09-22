package com.example.Timely.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Timely.Models.Course;

@Repository
public interface CourseRepo extends JpaRepository<Course, Long> {
    
}