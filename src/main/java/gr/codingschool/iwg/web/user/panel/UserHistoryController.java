package gr.codingschool.iwg.web.user.panel;

import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.service.EventService;
import gr.codingschool.iwg.service.NotificationService;
import gr.codingschool.iwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserHistoryController {
    @Autowired
    private EventService eventService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/user/panel/history"}, method = RequestMethod.GET)
    public ModelAndView userHistory(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        List<Event> events = eventService.findHistoryByUser(loggedInUser);
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());

        Event viewHistoryEvent = new Event();
        viewHistoryEvent.setUser(loggedInUser);
        viewHistoryEvent.setType("Viewed history");
        viewHistoryEvent.setInformation("The user viewed his history");
        eventService.save(viewHistoryEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.addObject("events", events);
        modelAndView.setViewName("user/panel/history");
        return modelAndView;
    }
}
