package gr.codingschool.iwg.web.user.panel;

import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.Notification;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.service.EventService;
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

@Controller
public class UserNotificationsController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/user/panel/notifications"}, method = RequestMethod.GET)
    public ModelAndView userNotifications(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        List<Notification> notifications = notificationService.findNotificationsByUser(loggedInUser);
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());

        Event viewNotificationsEvent = new Event();
        viewNotificationsEvent.setUser(loggedInUser);
        viewNotificationsEvent.setType("Viewed notifications");
        viewNotificationsEvent.setInformation("The user viewed his notifications");
        eventService.save(viewNotificationsEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.addObject("notifications", notifications);
        modelAndView.setViewName("user/panel/notifications");
        return modelAndView;
    }

    @RequestMapping(value = {"/user/panel/notifications/read"}, method = RequestMethod.GET)
    public ModelAndView userReadNotification(@RequestParam("id") int id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        Notification readNotification = notificationService.readNotification(id);
        List<Notification> notifications = notificationService.findNotificationsByUser(loggedInUser);
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());

        Event readNotificationEvent = new Event();
        readNotificationEvent.setUser(loggedInUser);
        readNotificationEvent.setType("Read a notification");
        readNotificationEvent.setInformation("The user marked the notification with message: '" + readNotification.getMessage() + "' as read");
        eventService.save(readNotificationEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.addObject("notifications", notifications);
        modelAndView.setViewName("user/panel/notifications");
        return modelAndView;
    }

    @RequestMapping(value = {"/user/panel/notifications/unread"}, method = RequestMethod.GET)
    public ModelAndView userUnreadNotification(@RequestParam("id") int id, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        Notification unreadNotification = notificationService.unreadNotification(id);
        List<Notification> notifications = notificationService.findNotificationsByUser(loggedInUser);
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());

        Event unreadNotificationEvent = new Event();
        unreadNotificationEvent.setUser(loggedInUser);
        unreadNotificationEvent.setType("Unread a notifications");
        unreadNotificationEvent.setInformation("The user marked the notification with message: '" + unreadNotification.getMessage() + "' as unread");
        eventService.save(unreadNotificationEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.addObject("notifications", notifications);
        modelAndView.setViewName("user/panel/notifications");
        return modelAndView;
    }
}
