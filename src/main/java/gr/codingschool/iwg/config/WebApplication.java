package gr.codingschool.iwg.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = {
       SecurityAutoConfiguration.class,ErrorMvcAutoConfiguration.class})
@ComponentScan({"gr.codingschool.iwg"})
@EntityScan("gr.codingschool.iwg.model")
@EnableJpaRepositories("gr.codingschool.iwg.repository")
public class WebApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
