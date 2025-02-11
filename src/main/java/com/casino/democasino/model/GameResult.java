package com.casino.democasino.model;

import lombok.Data;

@Data
public class GameResult {
    private boolean isWin;
    private double winAmount;
    private String outcome;
}
