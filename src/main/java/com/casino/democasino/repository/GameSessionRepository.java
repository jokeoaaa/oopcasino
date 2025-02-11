package com.casino.democasino.repository;

import com.casino.democasino.model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    List<GameSession> findByUserIdOrderByGameSessionIdDesc(Long userId);
}