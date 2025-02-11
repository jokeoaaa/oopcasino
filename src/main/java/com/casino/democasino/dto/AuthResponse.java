package com.casino.democasino.dto;

import com.casino.democasino.model.User;
import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private Long userId;
    private String username;
    private String email;
    private Double balance;

    public AuthResponse(String token, User user) {
        this.token = token;
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.balance = user.getBalance();
    }
}
