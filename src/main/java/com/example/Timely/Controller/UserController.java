package com.example.Timely.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.dto.UserResponseDTO;
import com.example.Timely.Service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {

        return ResponseEntity.ok(userService.getAllUsers());

        
    }
    
}
