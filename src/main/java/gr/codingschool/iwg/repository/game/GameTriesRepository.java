package gr.codingschool.iwg.repository.game;

import gr.codingschool.iwg.model.game.GameTries;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 
 */
public interface GameTriesRepository extends JpaRepository<GameTries, Long> {
    GameTries findTriesByUserIdAndGameId(int userId, int gameId);
    GameTries save(GameTries gameTries);
}
