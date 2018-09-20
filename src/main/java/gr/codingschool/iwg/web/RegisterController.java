package gr.codingschool.iwg.web;

import gr.codingschool.iwg.model.Event;
import gr.codingschool.iwg.model.user.User;
import gr.codingschool.iwg.service.EventService;
import gr.codingschool.iwg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;
    @Autowired
    private EventService eventService;

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {
            userService.saveUser(user);

            userService.initWalletForUser(user.getUsername());

            Event registerEvent = new Event();
            registerEvent.setUser(user);
            registerEvent.setType("Registration");
            registerEvent.setInformation("The user registered");
            eventService.save(registerEvent);

            modelAndView.addObject("successRegister", true);
            modelAndView.setViewName("redirect:/login");
        }
        return modelAndView;
    }
}
