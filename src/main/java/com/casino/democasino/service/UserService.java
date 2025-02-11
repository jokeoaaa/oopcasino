package com.casino.democasino.service;

import com.casino.democasino.dto.UserRegistrationRequest;
import com.casino.democasino.exceptions.UserAlreadyExistsException;
import com.casino.democasino.model.User;
import com.casino.democasino.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {
    private static final Double INITIAL_BALANCE = 2000.;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User processOAuthPostLogin(String email, String name) {
        log.info("Processing OAuth login for email: {}", email);

        Optional<User> existingUser = userRepository.findByEmail(email);

        if (existingUser.isPresent()) {
            log.info("Existing user found with email: {}", email);
            return existingUser.get();
        }

        log.info("Creating new user for email: {}", email);
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setUsername(generateUniqueUsername(name));
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        newUser.setBalance(1000.0);
        newUser.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);
        log.info("New user created with ID: {}", savedUser.getId());

        return savedUser;
    }

    private String generateUniqueUsername(String name) {
        String baseName = name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        if (baseName.length() < 3) {
            baseName = "user" + baseName;
        }

        String username = baseName;
        int suffix = 1;


        while (userRepository.existsByUsername(username)) {
            username = baseName + suffix;
            suffix++;
        }

        return username;
    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User updateBalance(Long userId, Double amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        double newBalance = user.getBalance() + amount;
        if (newBalance < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        user.setBalance(newBalance);
        return userRepository.save(user);
    }

    @Transactional
    public User registerUser(UserRegistrationRequest registrationRequest) {
        log.info("Starting user registration process for username: {}", registrationRequest.getUsername());

        // Validate if username already exists
        if (userRepository.existsByUsername(registrationRequest.getUsername())) {
            log.warn("Username already exists: {}", registrationRequest.getUsername());
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Validate if email already exists
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            log.warn("Email already exists: {}", registrationRequest.getEmail());
            throw new UserAlreadyExistsException("Email already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setBalance(INITIAL_BALANCE);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);
        log.info("Successfully registered new user with ID: {}", savedUser.getId());

        return savedUser;
    }

    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }
}