package com.casino.democasino.model;

import com.casino.democasino.enums.GameType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "game_sessions")
public class GameSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long gameSessionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private GameType gameType;

    private Double bet;
    private Boolean isWin;
    private Double result;
    private String option;
}