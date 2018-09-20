package gr.codingschool.iwg.web.user;

import gr.codingschool.iwg.model.game.Game;
import gr.codingschool.iwg.model.game.GamePlay;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.service.GameService;
import gr.codingschool.iwg.service.NotificationService;
import gr.codingschool.iwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 
 */
@Controller
public class UserGameController {
    @Autowired
    private GameService gameService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/user/game", "/user/favourites/game"}, method = RequestMethod.GET)
    public ModelAndView home(@RequestParam("id") int id , HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        User loggedInUser = (User) session.getAttribute("user");
        User user = userService.findByUsername(loggedInUser.getUsername());

        int unreadNotifications = notificationService.findUnreadNotificationsByUser(user).size();

        Game game = gameService.findGameById(id);
        List<GamePlay> recentGames = gameService.findRecentlyPlayedByUser(user);

        modelAndView.addObject("user", user);
        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.addObject("game", game);
        modelAndView.addObject("isFavourite", isFavouriteGame(user, game));
        modelAndView.addObject("isRated", isRatedGame(user, game));
        modelAndView.addObject("recent", recentGames);

        modelAndView.setViewName("user/game");

        return modelAndView;
    }

    private boolean isFavouriteGame(User user, Game game){
        return user.getListOfFavouriteGameIds().contains(game.getId());
    }

    private boolean isRatedGame(User user, Game game){
        return user.getListOfRatedGameIds().contains(game.getId());
    }
}
