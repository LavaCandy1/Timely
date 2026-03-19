package com.example.Timely.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.ExtraClassRequest;
import com.example.Timely.Service.RequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;
    
    // public RequestController(RequestService requestService){
    //     this.requestService = requestService;;
    // }
    
    @PostMapping("/extraClassSubmit")
    public ResponseEntity<Void> createExtraClassRequest(@RequestBody ExtraClassRequest incomingRequestData) {
        // Logic to create a extraClassSubmit request
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Creating a extraClassSubmit request...");
        incomingRequestData.setInstructor(auth.getName());
        System.out.println(incomingRequestData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/extraClassRequests")
    public ResponseEntity<List<ExtraClassRequest>> getAllExtraClassRequests() {
        
        List<ExtraClassRequest> requests = requestService.getAllExtraClassRequests();
        System.out.println("Retrieving all extraClassSubmit requests...");
        return ResponseEntity.ok(requests);
    }



}
