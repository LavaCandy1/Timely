package com.example.Timely.Service;

import com.example.Timely.Models.User;
import com.example.Timely.Models.dto.UserRequestDTO;
import com.example.Timely.Models.dto.UserResponseDTO;
import com.example.Timely.Repository.UserRepo;

import com.example.Timely.Service.Security.JWTService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserResponseDTO createStudent(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode((userRequestDTO.getPassword())));
        user.setRole(User.RoleEnum.STUDENT);
        User savedUser = userRepo.save(user);

        return new UserResponseDTO(savedUser);
    }

    public UserResponseDTO createTeacher(UserRequestDTO userRequestDTO) {

        if (userRepo.existsByEmail(userRequestDTO.getEmail())) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode((userRequestDTO.getPassword())));
        user.setRole(User.RoleEnum.TEACHER);
        User savedUser = userRepo.save(user);

        return new UserResponseDTO(savedUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepo.findAll().stream().map(UserResponseDTO::new).toList();
    }

    public UserResponseDTO getUserById(Long id) {
        User foundUser = userRepo.findById(id).orElse(null);
        if (foundUser != null) {
            return new UserResponseDTO(foundUser);
        } else
            return null;
    }

    public boolean deleteUser(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        Optional<User> existingUser = userRepo.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setName(userRequestDTO.getName());
            updatedUser.setEmail(userRequestDTO.getEmail());
            updatedUser.setPassword(userRequestDTO.getPassword());
            User updatedUser2 = userRepo.save(updatedUser);
            return new UserResponseDTO(updatedUser2);
        }
        return null;
    }

    public String verifyUser(UserRequestDTO userRequestDTO) throws BadCredentialsException, Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequestDTO.getEmail(), userRequestDTO.getPassword()));

            // Fetch user roles from DB
            User user = userRepo.findByEmail(userRequestDTO.getEmail())
                    .orElseThrow(() -> new Exception("User not found"));

            String roles = user.getRole().toString();

            return jwtService.generateToken(user.getEmail(), roles);

        } catch (BadCredentialsException ex) {

            throw ex;
        } catch (Exception ex) {

            ex.printStackTrace();
            throw ex;
        }
    }

}
