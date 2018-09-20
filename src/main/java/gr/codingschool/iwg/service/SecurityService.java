package gr.codingschool.iwg.service;

import gr.codingschool.iwg.model.user.CurrentUser;
import gr.codingschool.iwg.model.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private void clearAuthentication(){
        SecurityContextHolder.clearContext();
    }

    public void authenticateUser(User user){
        clearAuthentication();
        CurrentUser currentUser = new CurrentUser(user);
        Authentication auth = new UsernamePasswordAuthenticationToken(currentUser.getUsername(), currentUser.getPassword(),
                currentUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
