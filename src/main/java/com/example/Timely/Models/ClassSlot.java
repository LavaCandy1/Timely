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
    String courseCode; // e.g., "CSE101"
    String dayOfWeek; // e.g., "Monday", "Tuesday"
    Time startTime; // e.g., "10:00"
    Time endTime;   // e.g., "11:00"
    String location; // e.g., "Room 101"
    String instructor; // e.g., "Dr. Smith"
    SlotType slotType; // e.g., "Lecture", "Lab", "Tutorial"

    String batch;
    String group;
    String year;
    // String semester; //maybe in future if need to save old timetables too

    //enum for slotType with display names for easy understanding
    public enum SlotType {
        LECTURE("Lecture"),
        LAB("Lab"),
        TUTORIAL("Tutorial");

        private final String displayName;

        SlotType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
