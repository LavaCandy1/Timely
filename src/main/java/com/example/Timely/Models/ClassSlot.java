package com.example.Timely.Models;

import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ClassSlot {
    
    public ClassSlot(String courseCode2, String dayOfWeek2, String startTime2, String endTime2, String location2,
            String instructor2, SlotType slotType2, String batch2, String groups, String year2) {
        
        this.courseCode = courseCode2;
        this.dayOfWeek = dayOfWeek2;
        this.startTime = Time.valueOf(startTime2 + ":00");
        this.endTime = Time.valueOf(endTime2 + ":00");
        this.location = location2;
        this.instructor = instructor2;
        this.slotType = slotType2;
        this.batch = batch2;
        this.group = groups;
        this.year = year2;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String courseCode; // e.g., "CSE101"
    String dayOfWeek; // e.g., "Monday", "Tuesday"
    Time startTime; // e.g., "10:00"
    Time endTime;   // e.g., "11:00"
    String location; // e.g., "Room 101"
    String instructor; // e.g., "Dr. Smith"
    @Enumerated(EnumType.STRING)
    SlotType slotType; // e.g., "Lecture", "Lab", "Tutorial"

    String batch;
    @Column(name = "group_name")
    String group;
    String year;
    // String semester; //maybe in future if need to save old timetables too

    //enum for slotType with display names for easy understanding
    public enum SlotType {
        LECTURE("Lecture"),
        LAB("Lab"),
        TUTORIAL("Tutorial"),
        UNKNOWN("Unknown");

        private final String displayName;

        SlotType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
