package org.example.groupassignment.payload;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    // Getters and Setters
}