package gr.codingschool.iwg.web;

import com.sun.net.httpserver.Authenticator;
import gr.codingschool.iwg.model.*;
import gr.codingschool.iwg.model.game.Game;
import gr.codingschool.iwg.model.game.GameResult;
import gr.codingschool.iwg.model.game.GameTries;
import gr.codingschool.iwg.model.game.RateForm;
import gr.codingschool.iwg.model.user.LoginForm;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.model.user.UserWallet;
import gr.codingschool.iwg.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Random;

/**
 * 
 */
@RestController
public class AjaxController {
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private EventService eventService;
    @Autowired
    private GameService gameService;
    @Autowired
    private NotificationService notificationService;

    private static final int NUMBER_OF_TRIES = 2;

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = {"/modalLogin"})
    public ResponseEntity getSearchResultViaAjax(@RequestBody LoginForm loginForm, HttpSession session) {

        User existingUser = userService.authenticate(loginForm.getUsername(), loginForm.getPassword());

        if(existingUser == null)
        {
            User failedUser = userService.findByUsername(loginForm.getUsername());
            if(failedUser != null) {
                Event loginEvent = new Event();
                loginEvent.setUser(failedUser);
                loginEvent.setType("Failed Login");
                loginEvent.setInformation("The user failed to login");
                eventService.save(loginEvent);
            }

            return new ResponseEntity<Error>(HttpStatus.UNAUTHORIZED);
        }

        securityService.authenticateUser(existingUser);

        Event loginEvent = new Event();
        loginEvent.setUser(existingUser);
        loginEvent.setType("Login");
        loginEvent.setInformation("The user logged in");
        eventService.save(loginEvent);

        session.setAttribute("user", existingUser);

        return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = {"/user/game/rate"})
    public ResponseEntity getRateResultViaAjax(@RequestBody RateForm rateForm, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        User user = userService.findByUsername(loggedInUser.getUsername());

        Game game = gameService.findGameById(rateForm.getId());

        user.getRatedGames().add(game);
        userService.updateUser(user);

        rateGame(game, rateForm.getValue());

        Event rateEvent = new Event();
        rateEvent.setUser(user);
        rateEvent.setType("Rate game");
        rateEvent.setInformation("The user rated the game with '" + rateForm.getValue());
        eventService.save(rateEvent);

        return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = {"/user/game/favourite"})
    public ResponseEntity addToFavouritesViaAjax(@RequestParam("gameId") int gameId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        User user = userService.findByUsername(loggedInUser.getUsername());

        Game game = gameService.findGameById(gameId);

        user.getFavouriteGames().add(game);
        userService.updateUser(user);

        Event favouriteEvent = new Event();
        favouriteEvent.setUser(user);
        favouriteEvent.setType("Favourite game");
        favouriteEvent.setInformation("The user added the game with name '" + game.getName() + "' as favourite");
        eventService.save(favouriteEvent);

        return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = {"/user/game/unFavourite"})
    public ResponseEntity removeFromFavouritesViaAjax(@RequestParam("gameId") int gameId, HttpSession session) {
        User loggedInUser = (User) session.getAttribute("user");
        User user = userService.findByUsername(loggedInUser.getUsername());

        Game game = gameService.findGameById(gameId);

        user.getFavouriteGames().remove(game);
        userService.updateUser(user);

        Event favouriteEvent = new Event();
        favouriteEvent.setUser(user);
        favouriteEvent.setType("Favourite game");
        favouriteEvent.setInformation("The user removed the game with name '" + game.getName() + "' from favourites");
        eventService.save(favouriteEvent);

        return new ResponseEntity<Authenticator.Success>(HttpStatus.OK);
    }

    @RequestMapping(value = {"/user/game/try"}, produces = "application/json")
    public @ResponseBody int getGameTriesPerUserViaAjax(@RequestParam("gameId") int gameId, HttpSession session){
        User loggedInUser = (User) session.getAttribute("user");

        GameTries foundGameTries = gameService.findTriesByUserIdAndGameId(loggedInUser.getId(), gameId);

        if(foundGameTries != null){
            if(foundGameTries.getTries() == 0){
                return 0;
            }
            else{
                foundGameTries.setTries(foundGameTries.getTries() - 1);

                GameTries savedGameTries = gameService.saveGameTries(foundGameTries);
                Game game = gameService.findGameById(gameId);

                Event tryEvent = new Event();
                tryEvent.setUser(loggedInUser);
                tryEvent.setType("Try game");
                tryEvent.setInformation("The user tried the game with name: '" + game.getName() + "'");
                eventService.save(tryEvent);

                return findRandomOddsForTryingMode(game.getOdds());
            }
        }
        else {
            Game game = gameService.findGameById(gameId);

            GameTries gameTries = new GameTries();
            gameTries.setGame(game);
            gameTries.setUser(loggedInUser);
            gameTries.setTries(NUMBER_OF_TRIES);

            GameTries savedGameTries = gameService.saveGameTries(gameTries);

            Event tryEvent = new Event();
            tryEvent.setUser(loggedInUser);
            tryEvent.setType("Try game");
            tryEvent.setInformation("The user tried the game with name '" + game.getName() + "'");
            eventService.save(tryEvent);

            return findRandomOddsForTryingMode(game.getOdds());
        }

    }

    @RequestMapping(value = {"/user/game/play"})
    public ResponseEntity<GameResult> playGameViaAjax(@RequestParam("gameId") int gameId, HttpSession session){
        User loggedInUser = (User) session.getAttribute("user");
        Game game = gameService.findGameById(gameId);
        UserWallet wallet = userService.getWalletForUser(loggedInUser.getUsername());

        boolean isBalanced = hasEnoughBalance(wallet, game);

        if(isBalanced) {
            boolean isAWin = findRandomOddsForPlayingMode(game.getOdds());

            GameResult gameResult = new GameResult();
            gameResult.setEnoughBalance(true);
            gameResult.setResult(isAWin);
            gameResult.setOldBalance(wallet.getBalance());
            gameResult.setNewBalance(calculateAndUpdateBalance(isAWin, wallet, game));

            gameService.saveGamePlay(loggedInUser,game,isAWin);

            Event playEvent = new Event();
            playEvent.setUser(loggedInUser);
            playEvent.setType("Play game");
            playEvent.setInformation("The user played the game with name '" + game.getName() + "' and " + (isAWin? "won" : "lost"));
            eventService.save(playEvent);

            return new ResponseEntity<>(gameResult, HttpStatus.OK);
        }
        else {
            GameResult gameResult = new GameResult();
            gameResult.setEnoughBalance(false);
            gameResult.setResult(false);
            gameResult.setOldBalance(wallet.getBalance());
            return new ResponseEntity<>(gameResult,  HttpStatus.OK);
        }
    }

    private boolean findRandomOddsForPlayingMode(int odds){
        Random myRandom = new Random( System.currentTimeMillis() );
        int randomInt = myRandom.nextInt(100) + 1;

        System.out.println(randomInt);

        return (randomInt <= odds);
    }

    private int findRandomOddsForTryingMode(int odds){
        Random myRandom = new Random( System.currentTimeMillis() );
        int randomInt = myRandom.nextInt(100) + 1;

        System.out.println(randomInt);

        if(randomInt <= odds){
            return 1;
        }
        else{
            return -1;
        }
    }

    private boolean hasEnoughBalance(UserWallet wallet, Game game){
        return ((wallet.getBalance()) >= (game.getPrice()));
    }

    private int calculateAndUpdateBalance(boolean isWin, UserWallet wallet, Game game){
        int newBalance;

        if(isWin){
            newBalance = wallet.getBalance() + (game.getPrize() - game.getPrice());
        }
        else{
            newBalance = wallet.getBalance() - game.getPrice();
        }

        wallet.setBalance(newBalance);

        userService.saveWallet(wallet);

        return wallet.getBalance();
    }

    private void rateGame(Game game, double value){
        game.setRating((double)((game.getRating() * game.getUsersRated()) + value) / (double) (game.getUsersRated() + 1));
        game.setUsersRated(game.getUsersRated() + 1);

        gameService.saveGame(game);
    }
}
