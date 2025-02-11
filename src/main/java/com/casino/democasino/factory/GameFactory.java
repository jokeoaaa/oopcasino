package com.casino.democasino.factory;

import com.casino.democasino.enums.GameType;
import com.casino.democasino.games.Roulette;
import com.casino.democasino.games.SlotMachine;
import com.casino.democasino.model.interfaces.Game;
import org.springframework.stereotype.Component;


@Component
public class GameFactory {
    private final Roulette roulette;
    private final SlotMachine slotMachine;

    public GameFactory(Roulette roulette, SlotMachine slotMachine) {
        this.roulette = roulette;
        this.slotMachine = slotMachine;
    }

    public Game createGame(GameType gameType) {
        return switch (gameType) {
            case ROULETTE -> roulette;
            case SLOT_MACHINE -> slotMachine;
            default -> throw new IllegalArgumentException("Unknown game type: " + gameType);
        };
    }
}