package com.casino.democasino.controller;

import com.casino.democasino.dto.AuthRequest;
import com.casino.democasino.dto.AuthResponse;
import com.casino.democasino.dto.LoginRequest;
import com.casino.democasino.dto.UserRegistrationRequest;
import com.casino.democasino.model.User;
import com.casino.democasino.service.JwtService;
import com.casino.democasino.service.UserService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication management API")
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService,
                          JwtService jwtService,
                          AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        log.info("Received registration request for username: {}", request.getUsername());

        User user = userService.registerUser(request);
        String token = jwtService.generateToken(user.getUsername());

        AuthResponse response = new AuthResponse(token, user);

        log.info("Successfully registered user with ID: {}", user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Authenticate user")
    public ResponseEntity<AuthResponse> authenticate(@Valid @RequestBody AuthRequest request) {
        log.info("Authentication request received for user: {}", request.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userService.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getUsername());

        AuthResponse response = new AuthResponse(token, user);

        log.info("Authentication successful for user: {}", request.getUsername());
        return ResponseEntity.ok(response);
    }
}