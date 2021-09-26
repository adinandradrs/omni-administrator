package id.codefun.web.administrator.configuration;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import id.codefun.web.administrator.service.user.UserSecurityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserSecurityService userSecurityService;

    private static final String [] ignorePath = {
		"/login.jsf"
	};

    public SecurityConfiguration(UserSecurityService userSecurityService){
        this.userSecurityService = userSecurityService;
    }

    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

    @Override
	public void configure(final AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(this.userSecurityService).passwordEncoder(passwordEncoder());
	}

    @Override
	protected void configure(HttpSecurity http) throws Exception {		
		http.cors().and().csrf().disable().authorizeRequests().antMatchers("/apps/**").authenticated();
        http.addFilterBefore(serviceFilter(), UsernamePasswordAuthenticationFilter.class);
	}

    private Filter serviceFilter() {
		return new OncePerRequestFilter(){
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
				
			}
		};
	}

    private void doValidateRequstURIAsIgnoredOrApiKeyBased(AtomicBoolean isPermitted, HttpServletRequest request, String apiKey){
		Arrays.stream(ignorePath).filter(url -> request.getRequestURI().equals("/") || microPath.concat(request.getRequestURI()).contains(url))
				.findAny()
				.ifPresentOrElse(data-> isPermitted.set(true),()-> isPermitted.set(StringUtils.isNotEmpty(apiKey) && apiKey.equals(epicApiKeyInternal)));
	}
    
}
