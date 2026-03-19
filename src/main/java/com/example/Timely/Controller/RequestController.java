package com.example.Timely.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/extraClassRequests/{type}")
    public ResponseEntity<List<ExtraClassRequest>> getAllExtraClassRequests(@PathVariable String type) {
        
        System.out.println(type);
        if (type.equalsIgnoreCase("Extra Class")){
            System.out.println("Retrieving all extraClassSubmit requests...");

            List<ExtraClassRequest> requests = requestService.getAllExtraClassRequests();
            return ResponseEntity.ok(requests);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }



}
