package gr.codingschool.iwg.repository.game;

import gr.codingschool.iwg.model.game.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    Page<Game> findAll(Pageable pageable);
    Game findById(int id);
    Game save(Game game);
    int deleteById(int id);
}
