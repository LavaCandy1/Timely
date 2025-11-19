package com.example.Timely.Models.dto.timetableDTO;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import com.example.Timely.Models.ClassSlot.SlotType;

import lombok.Data;

@Data
public class deleteClassDTO {
    String courseCode;
    Time startTime;
    String dayOfWeek;
    String location;
    SlotType slotType;
    String instructor;
    List<String> batches;
    Date cancelledDate;
}
