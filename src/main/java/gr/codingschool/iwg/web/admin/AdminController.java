package gr.codingschool.iwg.web.admin;

import gr.codingschool.iwg.model.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class AdminController {
    @RequestMapping(value = {"/admin","/admin/home"}, method = RequestMethod.GET)
    public ModelAndView adminHome(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView();
        User loggedInUser = (User) session.getAttribute("user");
        modelAndView.addObject("user", loggedInUser);
        modelAndView.setViewName("admin/index");
        return modelAndView;
    }
}
