package com.casino.democasino.dto;

import com.casino.democasino.enums.GameType;
import lombok.Data;

@Data
public class GamePlayRequest {
        private Long userId;
        private GameType gameType;
        private Double bet;
        private String option;
}
