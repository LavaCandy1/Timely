package com.example.Timely.Models.dto.timetableDTO;

import com.example.Timely.Models.ClassSlot;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.example.Timely.Models.ClassSlot.SlotType;

import java.sql.Time;
import java.time.LocalTime;

@Data
public class addClassDTO {

    @NotBlank(message = "Course code is required")
    private String courseCode;

    @NotBlank(message = "Instructor name is required")
    private String instructor;

    private String batch;

    @NotBlank(message = "Year is required")
    private String year;

    private String group;

    @NotNull(message = "Slot type is required")
    private SlotType slotType; // Matches your Enum

    @NotBlank(message = "Day of week is required")
    private String dayOfWeek; // e.g., "Monday"

    @NotNull(message = "Start time is required")
    @JsonFormat(pattern = "HH:mm") // parsers "08:30" automatically
    private LocalTime startTime;

    private String location;

    public ClassSlot toEntity() {
        ClassSlot slot = new ClassSlot();
        
        slot.setCourseCode(this.courseCode);
        slot.setInstructor(this.instructor);
        slot.setBatch(this.batch);
        slot.setYear(this.year);
        slot.setGroup(this.group);
        slot.setSlotType(this.slotType);
        slot.setDayOfWeek(this.dayOfWeek);
        slot.setLocation(this.location);

        // Handle Time Conversion
        if (this.startTime != null) {
            slot.setStartTime(Time.valueOf(this.startTime));
            // default to 1 hour duration
            slot.setEndTime(Time.valueOf(this.startTime.plusHours(1))); 
        }

        return slot;
    }
}
