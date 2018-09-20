package gr.codingschool.iwg.model.user;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Column;

/**
 * 
 */
public class LoginForm {
    @Column(name = "username")
    @NotEmpty(message = "*Please provide a username")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "*Please provide a password")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
