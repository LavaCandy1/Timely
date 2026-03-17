package com.example.Timely.Models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ExtraClassRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private SlotType slotType;
    private String courseCode;
    private String dayOfWeek;
    private String startTime;
    private String location;
    private String instructor;
    private String batch;
    @Column(name = "group_name")
    private String group;
    private String year;

    private String status; // e.g., "Pending", "Approved", "Rejected"
    private String reason; // Reason for request

    private Date createdAt;
    


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
