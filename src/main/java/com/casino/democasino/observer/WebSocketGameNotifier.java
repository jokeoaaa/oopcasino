package com.casino.democasino.observer;

import com.casino.democasino.model.GameSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Component
@AllArgsConstructor
public class WebSocketGameNotifier implements GameEventListener {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void onGameComplete(GameSession gameSession) {
        messagingTemplate.convertAndSendToUser(
                gameSession.getUser().getUsername(),
                "/queue/game-results",
                gameSession
        );
    }
}