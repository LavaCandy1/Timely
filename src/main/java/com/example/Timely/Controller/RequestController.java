package com.example.Timely.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.RescheduleRequest;

@RestController
@RequestMapping("/requests")
public class RequestController {
    
    @PostMapping("/reschedule")
    public ResponseEntity<Void> createRescheduleRequest(@RequestBody RescheduleRequest incomingRequestData) {
        // Logic to create a reschedule request
        System.out.println("Creating a reschedule request...");
        System.out.println(incomingRequestData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rescheduleRequests")
    public String getAllRescheduleRequests() {
        // Logic to retrieve reschedule requests
        System.out.println("Retrieving all reschedule requests...");
        return "List of reschedule requests";
    }



}
