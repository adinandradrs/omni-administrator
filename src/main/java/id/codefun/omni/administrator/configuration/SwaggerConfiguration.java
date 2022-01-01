package id.codefun.omni.administrator.configuration;

import java.util.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${swagger.host}")
    private String swaggerHost;
    
    private Docket getBaseDocket(){
        return new Docket(DocumentationType.SWAGGER_2)
            .host(swaggerHost)
            .select()
            .apis(RequestHandlerSelectors.basePackage("id.codefun.web.administrator.controller"))
            .build()
            .apiInfo(getDefaultApiInfo())
            .useDefaultResponseMessages(false);
    }
    
    private ApiInfo getDefaultApiInfo() {
        return new ApiInfo("Codefun (Administrator) Docs", "API sandbox for Codefun (Administrator) service. Only for development purpose and API discovery.", "v1.0", "http://swagger.io/terms/", 
    		new Contact("Adinandra Dharmasurya", "", "adinandra.dharmasurya@gmail.com"), 
    		"Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
	}

    @Bean
    public Docket docket() {
        return getBaseDocket()
            .select()
            .build();
    }
    
}
