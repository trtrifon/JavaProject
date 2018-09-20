package gr.codingschool.iwg.web.user;

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

@Controller
public class EditDetailsController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @RequestMapping(value = {"/user/details"}, method = RequestMethod.GET)
    public ModelAndView userDetails(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.setViewName("user/details");
        return modelAndView;
    }

    @RequestMapping(value = "/user/details", method = RequestMethod.POST)
    public ModelAndView updateUser(User user, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();

        User sessionUser = (User) session.getAttribute("user");
        User loggedInUser = userService.findByUsername(sessionUser.getUsername());

        loggedInUser.setUsername(user.getUsername());
        loggedInUser.setFirstName(user.getFirstName());
        loggedInUser.setLastName(user.getLastName());
        loggedInUser.setAddress(user.getAddress());
        loggedInUser.setPhoneNumber(user.getPhoneNumber());
        loggedInUser.setEmail(user.getEmail());

        User updatedUser = userService.updateUser(loggedInUser);

        int unreadNotifications = notificationService.findUnreadNotificationsByUser(updatedUser).size();

        Event editDetailsEvent = new Event();
        editDetailsEvent.setUser(updatedUser);
        editDetailsEvent.setType("Edit Details");
        editDetailsEvent.setInformation("The user changed his/her details");
        eventService.save(editDetailsEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", updatedUser);
        modelAndView.addObject("wallet", updatedUser.getWallet());
        modelAndView.setViewName("user/details");

        return modelAndView;
    }
}
