package gr.codingschool.iwg.web.user;

import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.service.NotificationService;
import gr.codingschool.iwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class UserPanelController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/user/panel"}, method = RequestMethod.GET)
    public ModelAndView userPanel(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());
        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.setViewName("user/panel");
        return modelAndView;
    }
}
