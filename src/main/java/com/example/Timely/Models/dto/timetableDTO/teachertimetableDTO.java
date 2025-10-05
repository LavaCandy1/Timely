package com.example.Timely.Models.dto.timetableDTO;

import java.sql.Time;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class teachertimetableDTO {

    String courseCode;
    Time startTime;
    String dayOfWeek;
    String location;
    String slotType;
    String batches;
    
}
