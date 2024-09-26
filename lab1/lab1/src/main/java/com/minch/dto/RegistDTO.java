package com.minch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RegistDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 16, message = "Username must be in [5,16] characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 16, message = "Password must be in range [5, 16] characters long")
    private String password;

    @NotBlank(message = "Email is required")
    @Size(min = 5, max = 255, message = "Email must be in range [5, 255] characters long")
    @Email(message = "Email format is incorrect")
    private String email;

    private Set<String> role;
}
