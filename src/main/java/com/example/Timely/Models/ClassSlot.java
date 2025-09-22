package com.example.Timely.Models;

import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ClassSlot {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String courseCode;
    String dayOfWeek; // e.g., "Monday", "Tuesday"
    Time startTime; // e.g., "10:00"
    Time endTime;   // e.g., "11:00"
    String location; // e.g., "Room 101"
}
