package com.example.Timely.Events;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@Getter
public class ClassCancelEvent extends ApplicationEvent {

    private List<String> batches;
    private String status; // e.g., "CANCELLED", "RESCHEDULED"
    private String courseCode;
    private String dayOfWeek;
    private Time time;

    public ClassCancelEvent(Object source, List<String> batches,Date cancelledDate, String courseCode, String dayOfWeek, Time time) {
        super(source);
        this.batches = batches;
        this.status = (cancelledDate == null) ? "RESCHEDULED" : "CANCELLED";
        this.courseCode = courseCode;
        this.dayOfWeek = dayOfWeek;
        this.time = time;
    }
    
}
