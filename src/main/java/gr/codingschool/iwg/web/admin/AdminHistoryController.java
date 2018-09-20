package gr.codingschool.iwg.web.admin;

import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.service.EventService;
import gr.codingschool.iwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class AdminHistoryController {
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @RequestMapping(value = {"/admin/history"}, method = RequestMethod.GET)
    public ModelAndView adminHistory(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        List<User> users = userService.findAll();
        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("list", users);
        modelAndView.setViewName("admin/history");
        return modelAndView;
    }

    @RequestMapping(value = {"/admin/history/user"}, method = RequestMethod.GET)
    public ModelAndView adminViewHistory(@RequestParam("username") String username, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        User userHistory = userService.findByUsername(username);
        List<Event> events = eventService.findHistoryByUser(userHistory);

        if(!userHistory.getUsername().equals(loggedInUser.getUsername())) {
            Event viewUserHistoryEvent = new Event();
            viewUserHistoryEvent.setUser(loggedInUser);
            viewUserHistoryEvent.setType("User history");
            viewUserHistoryEvent.setInformation("Viewed the history of user: " + userHistory.getUsername());
            eventService.save(viewUserHistoryEvent);
        }

        modelAndView.addObject("user", loggedInUser);
        modelAndView.addObject("userHistory", userHistory);
        modelAndView.addObject("events", events);
        modelAndView.setViewName("admin/history/user");
        return modelAndView;
    }
}
