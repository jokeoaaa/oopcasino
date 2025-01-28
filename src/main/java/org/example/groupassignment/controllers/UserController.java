package org.example.groupassignment.controllers;

import org.example.groupassignment.models.User;
import org.example.groupassignment.payload.UserSummary;
import org.example.groupassignment.security.CurrentUser;
import org.example.groupassignment.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        User user = userService.getUserById(currentUser.getId());
        return new UserSummary(user.getId(), user.getUsername(), user.getEmail());
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@CurrentUser UserPrincipal currentUser) {
        User user = userService.getUserById(currentUser.getId());
        return ResponseEntity.ok(user.getBalance());
    }

    @GetMapping("/history")
    public ResponseEntity<?> getGameHistory(@CurrentUser UserPrincipal currentUser) {
        // Implement logic to retrieve game history
        return ResponseEntity.ok(/* game history */);
    }
}