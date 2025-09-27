package com.example.Timely.Models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.validation.constraints.Email;

@Setter
@Getter
@ToString
public class UserRequestDTO {

    @NotBlank(message = "Username cannot be blank")
    private String name;

    @Email
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
