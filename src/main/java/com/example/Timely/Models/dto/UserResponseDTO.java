package com.example.Timely.Models.dto;

import com.example.Timely.Models.User;

import lombok.Getter;

@Getter
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }
}
