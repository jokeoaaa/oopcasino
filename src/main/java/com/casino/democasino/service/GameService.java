package com.casino.democasino.service;

import com.casino.democasino.dto.GamePlayRequest;
import com.casino.democasino.factory.GameFactory;
import com.casino.democasino.model.GameResult;
import com.casino.democasino.model.GameSession;
import com.casino.democasino.model.User;
import com.casino.democasino.model.interfaces.Game;
import com.casino.democasino.observer.GameEventListener;
import com.casino.democasino.repository.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@AllArgsConstructor
@Service
public class GameService {
    private final GameFactory gameFactory;
    private final GameSessionRepository gameSessionRepository;
    private final UserService userService;
    private final List<GameEventListener> eventListeners;


    @Transactional
    public GameSession playGame(GamePlayRequest request) {
        User user = userService.findById(request.getUserId());

        if (user.getBalance() < request.getBet()) {
            throw new RuntimeException("Insufficient balance");
        }

        Game game = gameFactory.createGame(request.getGameType());
        GameResult result = game.play(request.getOption(), request.getBet());

        GameSession session = new GameSession();
        session.setUser(user);
        session.setGameType(request.getGameType());
        session.setBet(request.getBet());
        session.setIsWin(result.isWin());
        session.setResult(result.getWinAmount());
        session.setOption(request.getOption());

        // Update user balance
        double balanceChange = result.isWin() ? result.getWinAmount() : -request.getBet();
        userService.updateBalance(user.getId(), balanceChange);

        GameSession savedSession = gameSessionRepository.save(session);

        // Notify observers
        notifyGameComplete(savedSession);

        return savedSession;
    }

    private void notifyGameComplete(GameSession gameSession) {
        eventListeners.forEach(listener -> listener.onGameComplete(gameSession));
    }

    public List<GameSession> getUserGameHistory(Long userId) {
        return gameSessionRepository.findByUserIdOrderByGameSessionIdDesc(userId);
    }
}
