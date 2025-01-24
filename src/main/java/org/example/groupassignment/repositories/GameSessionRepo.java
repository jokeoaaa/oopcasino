package org.example.groupassignment.repositories;
import org.example.groupassignment.models.GameSession;
import org.example.groupassignment.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameSessionRepo extends JpaRepository<GameSession, Long> {
    List<GameSession> findByUser(User user);
}
