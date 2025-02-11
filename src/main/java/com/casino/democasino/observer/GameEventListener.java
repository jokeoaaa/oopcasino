package com.casino.democasino.observer;

import com.casino.democasino.model.GameSession;

public interface GameEventListener {
    void onGameComplete(GameSession gameSession);
}
