package com.example.Timely.Repository.Projections;

import java.time.LocalTime;
import java.time.LocalDate;

public interface AdminTimetableProjection {

    String getIds();
    String getCourseCode();
    LocalTime getStartTime();
    String getDayOfWeek();
    String getLocation();
    String getSlotType();
    String getInstructor();
    String getBatches();
    LocalDate getCancelledDate();
}