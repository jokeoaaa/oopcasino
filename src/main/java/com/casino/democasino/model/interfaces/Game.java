package com.casino.democasino.model.interfaces;

import com.casino.democasino.model.GameResult;

public interface Game {
    GameResult play(String option, Double bet);
}
