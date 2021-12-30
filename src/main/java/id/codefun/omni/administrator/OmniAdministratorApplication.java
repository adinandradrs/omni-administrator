package id.codefun.omni.administrator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan({"id.codefun.omni", "id.codefun.service"})
@EntityScan ("id.codefun.omni.administrator.model.entity")
@EnableJpaRepositories ("id.codefun.omni.administrator.repository")
public class OmniAdministratorApplication {

    public static void main(String [] args){
        SpringApplication.run(OmniAdministratorApplication.class, args);
    }
    
}
