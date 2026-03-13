package com.example.Timely.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.ExtraClassRequest;

@RestController
@RequestMapping("/requests")
public class RequestController {
    
    @PostMapping("/extraClassSubmit")
    public ResponseEntity<Void> createExtraClassRequest(@RequestBody ExtraClassRequest incomingRequestData) {
        // Logic to create a extraClassSubmit request
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Creating a extraClassSubmit request...");
        incomingRequestData.setInstructor(auth.getName());
        System.out.println(incomingRequestData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/extraClassSubmitRequests")
    public String getAllExtraClassRequests() {
        // Logic to retrieve extraClassSubmit requests
        System.out.println("Retrieving all extraClassSubmit requests...");
        return "List of extraClassSubmit requests";
    }



}
