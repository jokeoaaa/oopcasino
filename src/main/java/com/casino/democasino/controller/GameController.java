package com.casino.democasino.controller;

import com.casino.democasino.dto.GamePlayRequest;
import com.casino.democasino.model.GameSession;
import com.casino.democasino.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/games")
@Api(tags = "Games")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/play")
    @ApiOperation("Play a game")
    public ResponseEntity<GameSession> playGame(@RequestBody GamePlayRequest request) {
        return ResponseEntity.ok(gameService.playGame(request));
    }

    @GetMapping("/history")
    @ApiOperation("Get user's game history")
    public ResponseEntity<?> getGameHistory(@RequestParam Long userId) {
        return ResponseEntity.ok(gameService.getUserGameHistory(userId));
    }
}
