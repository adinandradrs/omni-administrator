package id.codefun.web.administrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"id.codefun.web", "id.codefun.service"})
@EntityScan ("id.codefun.web.administrator.model.entity")
@EnableJpaRepositories ("id.codefun.web.administrator.repository")
public class WebAdministratorApplication {

    public static void main(String [] args){
        SpringApplication.run(WebAdministratorApplication.class, args);
    }
    
}
