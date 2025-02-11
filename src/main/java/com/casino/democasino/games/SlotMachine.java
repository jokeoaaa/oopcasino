package com.casino.democasino.games;

import com.casino.democasino.model.GameResult;
import com.casino.democasino.model.interfaces.Game;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.*;

@Component
public class SlotMachine implements Game {
    private final Random random = new SecureRandom();


    private static final List<Symbol> SYMBOLS = Arrays.asList(
            new Symbol("🍒", 1, new int[]{2, 5, 25}),      // Cherry
            new Symbol("🍋", 2, new int[]{2, 10, 50}),     // Lemon
            new Symbol("🍊", 3, new int[]{5, 20, 100}),    // Orange
            new Symbol("🔔", 4, new int[]{10, 40, 200}),   // Bell
            new Symbol("⭐", 5, new int[]{15, 60, 300}),   // Star
            new Symbol("💎", 6, new int[]{20, 80, 400}),   // Diamond
            new Symbol("7️⃣", 7, new int[]{25, 100, 750}), // Seven
            new Symbol("🎰", 8, new int[]{50, 200, 1000})  // Jackpot
    );

    private static final List<int[]> PAYLINES = Arrays.asList(
            new int[]{1, 1, 1}, // Middle horizontal
            new int[]{0, 0, 0}, // Top horizontal
            new int[]{2, 2, 2}, // Bottom horizontal
            new int[]{0, 1, 2}, // Diagonal top-left to bottom-right
            new int[]{2, 1, 0}  // Diagonal bottom-left to top-right
    );

    private static final int REELS = 3;
    private static final int ROWS = 3;

    @Override
    public GameResult play(String betType, Double betAmount) {
        Symbol[][] grid = generateGrid();
        List<WinningLine> winningLines = calculateWins(grid, betAmount);

        GameResult result = new GameResult();
        result.setWin(!winningLines.isEmpty());
        result.setWinAmount(winningLines.stream()
                .mapToDouble(WinningLine::getWinAmount)
                .sum());

        StringBuilder outcome = new StringBuilder();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < REELS; j++) {
                outcome.append(grid[i][j].getSymbol());
                if (j < REELS - 1) outcome.append(" | ");
            }
            if (i < ROWS - 1) outcome.append("\n");
        }
        result.setOutcome(outcome.toString());

        return result;
    }

    private Symbol[][] generateGrid() {
        Symbol[][] grid = new Symbol[ROWS][REELS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < REELS; j++) {
                grid[i][j] = getRandomSymbol();
            }
        }
        return grid;
    }

    private Symbol getRandomSymbol() {
        int totalWeight = SYMBOLS.stream()
                .mapToInt(Symbol::getWeight)
                .sum();

        int randomValue = random.nextInt(totalWeight);
        int weightSum = 0;

        for (Symbol symbol : SYMBOLS) {
            weightSum += symbol.getWeight();
            if (randomValue < weightSum) {
                return symbol;
            }
        }

        return SYMBOLS.get(0);
    }

    private List<WinningLine> calculateWins(Symbol[][] grid, double betAmount) {
        List<WinningLine> winningLines = new ArrayList<>();

        for (int i = 0; i < PAYLINES.size(); i++) {
            int[] payline = PAYLINES.get(i);
            List<Symbol> lineSymbols = new ArrayList<>();


            for (int j = 0; j < REELS; j++) {
                lineSymbols.add(grid[payline[j]][j]);
            }
            WinningLine win = checkLineWin(lineSymbols, i + 1, betAmount);
            if (win != null) {
                winningLines.add(win);
            }
        }

        return winningLines;
    }

    private WinningLine checkLineWin(List<Symbol> symbols, int paylineNumber, double betAmount) {
        Symbol firstSymbol = symbols.get(0);
        int count = 1;

        for (int i = 1; i < symbols.size(); i++) {
            if (symbols.get(i).equals(firstSymbol)) {
                count++;
            } else {
                break;
            }
        }

        if (count >= 2) {
            double multiplier = firstSymbol.getMultipliers()[count - 1];
            return new WinningLine(
                    paylineNumber,
                    firstSymbol,
                    count,
                    betAmount * multiplier
            );
        }

        return null;
    }

    @Data
    private static class Symbol {
        private final String symbol;
        private final int weight;
        private final int[] multipliers; // Multipliers for 2, 3, 4 matching symbols
    }

    @Data
    private static class WinningLine {
        private final int paylineNumber;
        private final Symbol symbol;
        private final int count;
        private final double winAmount;
    }
}
