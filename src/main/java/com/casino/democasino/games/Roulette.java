package com.casino.democasino.games;

import com.casino.democasino.exceptions.GamePlayException;
import com.casino.democasino.model.GameResult;
import com.casino.democasino.model.interfaces.Game;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.*;

@Component
public class Roulette implements Game {
    private static final Map<String, Double> PAYOUT_RATIOS = new HashMap<>();
    private final Random random = new SecureRandom(); // Using SecureRandom for better randomization
    static {
        // Straight up (single number) - 35:1
        PAYOUT_RATIOS.put("straight", 35.0);
        // Split (two numbers) - 17:1
        PAYOUT_RATIOS.put("split", 17.0);
        // Street (three numbers) - 11:1
        PAYOUT_RATIOS.put("street", 11.0);
        // Corner (four numbers) - 8:1
        PAYOUT_RATIOS.put("corner", 8.0);
        // Line (six numbers) - 5:1
        PAYOUT_RATIOS.put("line", 5.0);
        // Dozen/Column (twelve numbers) - 2:1
        PAYOUT_RATIOS.put("dozen", 2.0);
        PAYOUT_RATIOS.put("column", 2.0);
        // Red/Black, Even/Odd, 1-18/19-36 - 1:1
        PAYOUT_RATIOS.put("outside", 1.0);
    }

    private static final List<Integer> RED_NUMBERS = Arrays.asList(
            1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    );

    @Override
    public GameResult play(String betType, Double betAmount) {
        // Generate winning number
        int winningNumber = random.nextInt(37);

        GameResult result = new GameResult();
        result.setOutcome(String.valueOf(winningNumber));

        boolean isWin = false;
        double winAmount = 0.0;

        try {
            BetDetails betDetails = parseBetType(betType);
            isWin = checkWin(winningNumber, betDetails);

            if (isWin) {
                double payoutRatio = PAYOUT_RATIOS.get(betDetails.getBetCategory());
                winAmount = betAmount * payoutRatio;
            }
        } catch (IllegalArgumentException e) {
            throw new GamePlayException("Invalid bet type: " + betType);
        }

        result.setWin(isWin);
        result.setWinAmount(winAmount);
        return result;
    }

    private BetDetails parseBetType(String betType) {
        String[] parts = betType.toLowerCase().split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid bet format");
        }
        return new BetDetails(parts[0], parts[1]);
    }

    private boolean checkWin(int winningNumber, BetDetails bet) {
        return switch (bet.getBetCategory()) {
            case "straight" -> Integer.parseInt(bet.getValue()) == winningNumber;
            case "split" -> checkSplitBet(winningNumber, bet.getValue());
            case "street" -> checkStreetBet(winningNumber, bet.getValue());
            case "corner" -> checkCornerBet(winningNumber, bet.getValue());
            case "outside" -> checkOutsideBet(winningNumber, bet.getValue());
            case "dozen" -> checkDozenBet(winningNumber, bet.getValue());
            case "column" -> checkColumnBet(winningNumber, bet.getValue());
            default -> throw new IllegalArgumentException("Unknown bet category");
        };
    }

    private boolean checkSplitBet(int winningNumber, String value) {
        String[] numbers = value.split(",");
        int num1 = Integer.parseInt(numbers[0]);
        int num2 = Integer.parseInt(numbers[1]);
        return winningNumber == num1 || winningNumber == num2;
    }

    private boolean checkStreetBet(int winningNumber, String value) {
        int streetStart = Integer.parseInt(value);
        return winningNumber >= streetStart && winningNumber < streetStart + 3;
    }

    private boolean checkCornerBet(int winningNumber, String value) {
        String[] numbers = value.split(",");
        List<Integer> cornerNumbers = Arrays.stream(numbers)
                .map(Integer::parseInt)
                .toList();
        return cornerNumbers.contains(winningNumber);
    }

    private boolean checkOutsideBet(int winningNumber, String value) {
        return switch (value) {
            case "red" -> RED_NUMBERS.contains(winningNumber);
            case "black" -> winningNumber != 0 && !RED_NUMBERS.contains(winningNumber);
            case "even" -> winningNumber != 0 && winningNumber % 2 == 0;
            case "odd" -> winningNumber != 0 && winningNumber % 2 == 1;
            case "1-18" -> winningNumber >= 1 && winningNumber <= 18;
            case "19-36" -> winningNumber >= 19 && winningNumber <= 36;
            default -> throw new IllegalArgumentException("Invalid outside bet value");
        };
    }

    private boolean checkDozenBet(int winningNumber, String value) {
        int dozen = Integer.parseInt(value);
        int start = (dozen - 1) * 12 + 1;
        return winningNumber >= start && winningNumber < start + 12;
    }

    private boolean checkColumnBet(int winningNumber, String value) {
        int column = Integer.parseInt(value);
        return winningNumber != 0 && winningNumber % 3 == column;
    }

    @Data
    private static class BetDetails {
        private final String betCategory;
        private final String value;
    }
}
