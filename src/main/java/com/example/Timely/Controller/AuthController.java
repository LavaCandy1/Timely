package com.example.Timely.Controller;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserRequestDTO userRequestDTO) {

        Map<String, String> response = new HashMap<>();

        try {
            String token = userService.verifyUser(userRequestDTO);

            response.put("token", token);
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException ex) {
            System.out.println(ex.getMessage());
            response.put("error", ex.getMessage());
            return ResponseEntity.status(401).body(response);

        } catch (Exception e) {
            response.put("error", "Authentication error: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }

    }
}
