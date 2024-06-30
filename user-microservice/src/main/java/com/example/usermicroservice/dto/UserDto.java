package com.example.usermicroservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {

    @NotEmpty(message = "firstname is required")
    private String firstname;

    @NotEmpty(message = "lastname is required")
    private String lastname;

    @NotEmpty(message = "username is required")
    private String username;

    @NotEmpty(message = "email is required")
    @Email
    private String email;

    @NotEmpty(message = "password is required")
    private String password;
}
