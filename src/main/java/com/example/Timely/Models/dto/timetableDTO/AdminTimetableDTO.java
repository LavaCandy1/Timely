package com.example.Timely.Models.dto.timetableDTO;

import java.sql.Date;
import java.sql.Time;

import com.example.Timely.Models.ClassSlot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AdminTimetableDTO {

    String courseCode;
    Time startTime;
    String dayOfWeek;
    String location;
    String slotType;
    String instructor;
    String batches;
    Date cancelledDate;

    public AdminTimetableDTO(ClassSlot classSlot) {
        this.courseCode = classSlot.getCourseCode();
        this.startTime = classSlot.getStartTime();
        this.dayOfWeek = classSlot.getDayOfWeek();
        this.location = classSlot.getLocation();
        this.slotType = classSlot.getSlotType().toString();
        this.instructor = classSlot.getInstructor();
        this.batches = classSlot.getBatch();
        this.cancelledDate = classSlot.getCancelledDate();
    }

    public AdminTimetableDTO(String courseCode, Time startTime, String dayOfWeek, String location, String slotType,
            String batches, Date cancelledDate) {
        this.courseCode = courseCode;
        this.startTime = startTime;
        this.dayOfWeek = dayOfWeek;
        this.location = location;
        this.slotType = slotType;
        this.batches = batches;
        this.cancelledDate = cancelledDate;
    }
}
