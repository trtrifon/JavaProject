package gr.codingschool.iwg.service;

import gr.codingschool.iwg.model.game.Game;
import gr.codingschool.iwg.model.game.GamePlay;
import gr.codingschool.iwg.model.game.GameTries;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.repository.game.GamePlayRepository;
import gr.codingschool.iwg.repository.game.GameRepository;
import gr.codingschool.iwg.repository.game.GameTriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 */
@Service("gameService")
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameTriesRepository gameTriesRepository;
    @Autowired
    private GamePlayRepository gamePlayRepository;


    public Page<Game> findAllGames(Pageable pageable){
        return gameRepository.findAll(pageable);
    }

    public Game findGameById(int id){
        return gameRepository.findById(id);
    }

    @Transactional
    public Game saveGame(Game game){
        return gameRepository.save(game);
    }

    @Transactional
    public int deleteGameById(int id) {
        return gameRepository.deleteById(id);
    }

    public GameTries findTriesByUserIdAndGameId(int userId, int gameId) {
        return gameTriesRepository.findTriesByUserIdAndGameId(userId, gameId);
    }

    @Transactional
    public GameTries saveGameTries(GameTries gameTries) {
        return gameTriesRepository.save(gameTries);
    }

    public List<GamePlay> findRecentlyPlayedByUser(User user) {
        return gamePlayRepository.findTop4ByUserOrderByDate(user);
    }

    @Transactional
    public GamePlay saveGamePlay(User user, Game game, Boolean outcome) {
        GamePlay gamePlay = new GamePlay();
        gamePlay.setUser(user);
        gamePlay.setGame(game);
        gamePlay.setOutcome(outcome);
        return gamePlayRepository.save(gamePlay);
    }
}
