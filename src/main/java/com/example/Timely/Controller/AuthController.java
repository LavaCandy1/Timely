package com.example.Timely.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Timely.Models.dto.UserRequestDTO;
import com.example.Timely.Models.dto.UserResponseDTO;
import com.example.Timely.Service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerStudent(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createStudent(userRequestDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/registerTeacher")
    public ResponseEntity<UserResponseDTO> registerTeacher(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.createTeacher(userRequestDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserRequestDTO userRequestDTO) {

        try {
            String token = userService.verifyUser(userRequestDTO);
            return ResponseEntity.ok(token);

        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Authentication error: " + e.getMessage());
        }

    }
}
