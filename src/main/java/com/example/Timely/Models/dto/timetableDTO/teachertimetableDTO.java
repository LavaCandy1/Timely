package com.example.Timely.Models.dto.timetableDTO;

import java.sql.Date;
import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TeacherTimetableDTO {

    String courseCode;
    Time startTime;
    String dayOfWeek;
    String location;
    String slotType;
    String batches;
    Date cancelledDate;
    
}
