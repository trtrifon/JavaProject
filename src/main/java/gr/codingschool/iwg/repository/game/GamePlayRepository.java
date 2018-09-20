package gr.codingschool.iwg.repository.game;

import gr.codingschool.iwg.model.game.Game;
import gr.codingschool.iwg.model.game.GamePlay;
import gr.codingschool.iwg.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GamePlayRepository extends JpaRepository<GamePlay, Long> {
    List<GamePlay> findByUserAndGame(User user, Game game);
    List<GamePlay> findTop4ByUserOrderByDate(User user);
    GamePlay save(GamePlay gamePlay);
}
