package com.example.Timely.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.ExtraClassRequest;
import com.example.Timely.Models.Locations;
import com.example.Timely.Service.RequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor // for constructor injection of final fields
public class RequestController {

    private final RequestService requestService;
    
    // public RequestController(RequestService requestService){
    //     this.requestService = requestService;;
    // }
    
    @PostMapping("/extraClassSubmit")
    public ResponseEntity<Void> createExtraClassRequest(@RequestBody ExtraClassRequest incomingRequestData) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Creating a extraClassSubmit request...");
        incomingRequestData.setInstructor(auth.getName());
        requestService.submitExtraClassRequest(incomingRequestData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/extraClassRequests/{type}")
    public ResponseEntity<List<ExtraClassRequest>> getAllExtraClassRequests(@PathVariable String type) {
        
        if (type.equalsIgnoreCase("Extra Class")){
            System.out.println("Retrieving all extraClassSubmit requests...");

            List<ExtraClassRequest> requests = requestService.getAllExtraClassRequests();
            return ResponseEntity.ok(requests);
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/extraClassRequests/{id}/approve")
    public ResponseEntity<Void> approveExtraClassRequest(@PathVariable Long id) {
        
        requestService.approveExtraClassRequest(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/extraClassRequests/{id}/reject")
    public ResponseEntity<Void> rejectExtraClassRequest(@PathVariable Long id) {
        
        requestService.rejectExtraClassRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/extraClassRequests/{id}/availableLocations")
    public ResponseEntity<List<Locations>> getAvailableLocations(@PathVariable Long id) {
        
        List<Locations> available = requestService.getAvailableLocations(id);
        return ResponseEntity.ok(available); 
    }

}
