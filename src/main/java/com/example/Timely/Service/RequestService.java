package com.example.Timely.Service;

import java.util.ArrayList;
import java.util.List;
import java.sql.Date;

import org.springframework.stereotype.Service;
import com.example.Timely.Models.ExtraClassRequest;
import com.example.Timely.Models.ExtraClassRequest.SlotType;

@Service
public class RequestService {

    private final List<ExtraClassRequest> dummyExtraClassRequests = new ArrayList<>();

    public RequestService() {
        dummyExtraClassRequests.add(createRequest(1L, SlotType.LECTURE, "CSET502", "Monday", "10:30", "Amritesh Kumar", "B40", "", "4th", "PENDING", "Topics left to teach before midsems.", "2022-05-12"));
        dummyExtraClassRequests.add(createRequest(2L, SlotType.LAB, "CSET211", "Wednesday", "14:00", "Dr. S. Sharma", "B12", "G1", "2nd", "APPROVED", "Extra practical time for project completion.", "2022-05-15"));
        dummyExtraClassRequests.add(createRequest(3L, SlotType.TUTORIAL, "MATH301", "Friday", "09:00", "Prof. Rajat Gupta", "B05", "", "3rd", "PENDING", "Doubt clearing session for Calculus module.", "2022-05-18"));
        dummyExtraClassRequests.add(createRequest(4L, SlotType.LECTURE, "CSET405", "Tuesday", "11:45", "Megha Verma", "B22", "", "4th", "REJECTED", "Makeup class for the missed holiday session.", "2022-05-20"));
        dummyExtraClassRequests.add(createRequest(5L, SlotType.LECTURE, "SOFT102", "Thursday", "15:30", "Ananya Singh", "B31", "G2", "1st", "PENDING", "Introduction to new software testing tools.", "2022-05-22"));
    }

    private ExtraClassRequest createRequest(Long id, SlotType type, String code, String day, String time, String instructor, String batch, String group, String year, String status, String reason, String date) {
        ExtraClassRequest req = new ExtraClassRequest();
        req.setId(id);
        req.setSlotType(type);
        req.setCourseCode(code);
        req.setDayOfWeek(day);
        req.setStartTime(time);
        req.setInstructor(instructor);
        req.setBatch(batch);
        req.setGroup(group);
        req.setYear(year);
        req.setStatus(status);
        req.setReason(reason);
        req.setCreatedAt(Date.valueOf(date)); // Format: YYYY-MM-DD
        return req;
    }

    public List<ExtraClassRequest> getAllExtraClassRequests() {
        System.out.println("Retrieving " + dummyExtraClassRequests.size() + " dummy requests...");
        return dummyExtraClassRequests;
    }
}