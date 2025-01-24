package org.example.groupassignment.controllers;

import com.sun.security.auth.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.groupassignment.services.GameService;

@RestController
@RequestMapping("/api/game")
@PreAuthorize("hasRole('USER')")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping("/slots")
    public ResponseEntity<?> playSlots(@CurrentUser UserPrincipal currentUser,
                                       @Valid @RequestBody BetRequest betRequest) {
        // Логика игры в "Слоты"
    }

    @PostMapping("/blackwhite")
    public ResponseEntity<?> playBlackWhite(@CurrentUser UserPrincipal currentUser,
                                            @Valid @RequestBody BlackWhiteRequest blackWhiteRequest) {
        // Логика игры в "Черное, белое"
    }
}

