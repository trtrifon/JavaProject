package gr.codingschool.iwg.web.admin;

import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.game.Game;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.service.EventService;
import gr.codingschool.iwg.service.GameService;
import gr.codingschool.iwg.web.PageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AdminGamesController {
    @Autowired
    private GameService gameService;
    @Autowired
    private EventService eventService;

    @RequestMapping(value = {"/admin/games"}, method = RequestMethod.GET)
    public ModelAndView adminGames(Pageable pageable, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        Page<Game> gamePage = gameService.findAllGames(pageable);
        PageWrapper<Game> page = new PageWrapper<Game>(gamePage, "/admin/games");

        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("list", page.getContent());
        modelAndView.addObject("page", page);
        modelAndView.setViewName("admin/games");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/games/edit"}, method = RequestMethod.GET)
    public ModelAndView adminEditGame(@RequestParam("id") int id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        Game game = gameService.findGameById(id);
        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("game", game);
        modelAndView.setViewName("admin/games/edit");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/games/add"}, method = RequestMethod.GET)
    public ModelAndView adminAddGame(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        Game game = new Game();
        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("game", game);
        modelAndView.setViewName("admin/games/add");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/games/add"}, method = RequestMethod.POST)
    public ModelAndView adminAddGame(Game game, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        gameService.saveGame(game);

        Event addGameEvent = new Event();
        addGameEvent.setUser(loggedInUser);
        addGameEvent.setType("Add game");
        addGameEvent.setInformation("Added a game with name: " + game.getName());
        eventService.save(addGameEvent);

        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("game", game);
        modelAndView.setViewName("admin/games/add");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/games/edit"}, method = RequestMethod.POST)
    public ModelAndView adminSaveGame(Game game, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser;
        loggedInUser = (User) session.getAttribute("user");
        gameService.saveGame(game);

        Event editGameEvent = new Event();
        editGameEvent.setUser(loggedInUser);
        editGameEvent.setType("Edit game");
        editGameEvent.setInformation("Edited the game with name: " + game.getName());
        eventService.save(editGameEvent);

        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("game", game);
        modelAndView.setViewName("admin/games/edit");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/games/delete"}, method = RequestMethod.GET)
    public ModelAndView adminDeleteGame(@RequestParam("id") int id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        Game game = gameService.findGameById(id);
        gameService.deleteGameById(id);
        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("game", game);
        modelAndView.setViewName("admin/games/deleted");
        return modelAndView;
    }
}
