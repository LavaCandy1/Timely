package com.example.Timely.Models.dto.timetableDTO;


import java.sql.Date;
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
public class cancelClassDTO {

    List<String> batches;
    Date cancelledDate;
    String courseCode;
    String dayOfWeek;
    String location;
    String slotType;
    Time startTime;
    
}
