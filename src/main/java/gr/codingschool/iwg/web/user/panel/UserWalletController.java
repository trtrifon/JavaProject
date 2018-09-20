package gr.codingschool.iwg.web.user.panel;

import gr.codingschool.iwg.model.Coupon;
import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.model.user.WithdrawAmount;
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
public class UserWalletController {
    @Autowired
    private EventService eventService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = {"/user/panel/wallet"}, method = RequestMethod.GET)
    public ModelAndView userWallet(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());

        Event viewHistoryEvent = new Event();
        viewHistoryEvent.setUser(user);
        viewHistoryEvent.setType("Viewed wallet");
        viewHistoryEvent.setInformation("The user viewed his wallet");
        eventService.save(viewHistoryEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.setViewName("user/panel/wallet");
        return modelAndView;
    }

    @RequestMapping(value = {"/user/panel/wallet/add"}, method = RequestMethod.GET)
    public ModelAndView userWalletAdd(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());
        Coupon coupon = new Coupon();

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("coupon", coupon);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.setViewName("user/panel/wallet/add");
        return modelAndView;
    }

    @RequestMapping(value = {"/user/panel/wallet/add"}, method = RequestMethod.POST)
    public ModelAndView userWalletAddCoupon(Coupon coupon, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        coupon = userService.getCouponIfAvailable(coupon.getCode());
        if(coupon == null){
            User user = userService.findByUsername(loggedInUser.getUsername());
            modelAndView.addObject("unreadNotifications", unreadNotifications);
            modelAndView.addObject("user", user);
            modelAndView.addObject("coupon", new Coupon());
            modelAndView.addObject("wallet", user.getWallet());
            modelAndView.addObject("successMessage", "This coupon has already been used");
            modelAndView.setViewName("user/panel/wallet/add");
            return modelAndView;
        }
        userService.addBalance(loggedInUser.getUsername(),coupon.getValue());
        userService.useCoupon(coupon.getCode());
        User user = userService.findByUsername(loggedInUser.getUsername());

        Event addedBalanceEvent = new Event();
        addedBalanceEvent.setUser(user);
        addedBalanceEvent.setType("Added coupon");
        addedBalanceEvent.setInformation("The user added the coupon with code '" + coupon.getCode() + "' to his balance (value " + coupon.getValue() + ")");
        eventService.save(addedBalanceEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("successMessage", "You have successfully added " + coupon.getValue() + " euros to your balance");
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.setViewName("user/panel/wallet");
        return modelAndView;
    }

    @RequestMapping(value = {"/user/panel/wallet/withdraw"}, method = RequestMethod.GET)
    public ModelAndView userWalletWithdraw(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();
        User user = userService.findByUsername(loggedInUser.getUsername());
        WithdrawAmount amount = new WithdrawAmount();

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("amount", amount);
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.setViewName("user/panel/wallet/withdraw");
        return modelAndView;
    }

    @RequestMapping(value = {"/user/panel/wallet/withdraw"}, method = RequestMethod.POST)
    public ModelAndView userWalletWithdrawAmount(WithdrawAmount amount, HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        int unreadNotifications = notificationService.findUnreadNotificationsByUser(loggedInUser).size();

        Boolean isWithdrawn = userService.withdrawBalance(loggedInUser.getUsername(),amount.getAmount());
        User user = userService.findByUsername(loggedInUser.getUsername());

        if(!isWithdrawn){
            modelAndView.addObject("unreadNotifications", unreadNotifications);
            modelAndView.addObject("user", user);
            modelAndView.addObject("amount", new WithdrawAmount());
            modelAndView.addObject("wallet", user.getWallet());
            modelAndView.addObject("successMessage", "The amount you entered could not be withdrawn");
            modelAndView.setViewName("user/panel/wallet/withdraw");
            return modelAndView;
        }

        Event withdrewBalanceEvent = new Event();
        withdrewBalanceEvent.setUser(user);
        withdrewBalanceEvent.setType("Withdrew balance");
        withdrewBalanceEvent.setInformation("The user withdrew " + amount.getAmount() + " euros from hist account");
        eventService.save(withdrewBalanceEvent);

        modelAndView.addObject("unreadNotifications", unreadNotifications);
        modelAndView.addObject("user", user);
        modelAndView.addObject("successMessage", "You have successfully withdrawn " + amount.getAmount() + " euros from your balance");
        modelAndView.addObject("wallet", user.getWallet());
        modelAndView.setViewName("user/panel/wallet");
        return modelAndView;
    }
}
