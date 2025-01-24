package org.example.groupassignment.controllers;

import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.groupassignment.services.UserService;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('USER')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public UserSummary getCurrentUser(@CurrentUser UserPrincipal currentUser) {
        // Возвращает информацию о текущем пользователе
        return null;
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@CurrentUser UserPrincipal currentUser) {
        // Возвращает баланс пользователя
        return null;
    }

    @GetMapping("/history")
    public ResponseEntity<?> getGameHistory(@CurrentUser UserPrincipal currentUser) {
        // Возвращает историю игр
        return null;
    }
}

