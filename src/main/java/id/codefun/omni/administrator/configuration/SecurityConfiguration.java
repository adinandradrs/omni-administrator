package id.codefun.omni.administrator.configuration;

import id.codefun.service.util.CacheUtility;
import id.codefun.service.util.CodefunConstants;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSON;
import id.codefun.service.util.WebUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import id.codefun.omni.administrator.model.response.UserLoginResponse;
import id.codefun.omni.administrator.service.user.UserSecurityService;
import id.codefun.omni.administrator.util.Constants;
import id.codefun.omni.administrator.util.UserUtility;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserSecurityService userSecurityService;
	private UserUtility userUtility;
	private CacheUtility cacheUtility;
	private WebUtility webUtility;

    private static final String [] ignorePath = {
		"/org.richfaces.resources",
		"/javax.faces.resource",
		"/favicon.ico",
		"/assets",
		"/login.jsf",
	};

	@Value("${api.key.internal}")
	private String apiKeyInternal;

	@Value("${api.micro.path}")
	private String apiMicroPath;

	@Value("${app.web.path}")
	private String appWebPath;

	private static final String EXPIRED = "expired";

    public SecurityConfiguration(UserSecurityService userSecurityService, UserUtility userUtility, CacheUtility cacheUtility,
		WebUtility webUtility){
        this.userSecurityService = userSecurityService;
		this.userUtility = userUtility;
		this.cacheUtility = cacheUtility;
		this.webUtility = webUtility;
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
				request.setAttribute(EXPIRED, false);
				AtomicBoolean isPermitted = new AtomicBoolean(false);
				String apiKey = request.getHeader(CodefunConstants.REQ_HEADER_APIKEY);
				doValidateRequstURIAsIgnoredOrApiKeyBased(isPermitted, request, apiKey);
				if(isPermitted.get()){
					SecurityContextHolder.getContext().setAuthentication(null);
					filterChain.doFilter(request, response);
					return;
				}
				String userId = request.getHeader(CodefunConstants.REQ_HEADER_USERID);
				String jwt = StringUtils.isNotBlank(request.getHeader(HttpHeaders.AUTHORIZATION)) ? request.getHeader(HttpHeaders.AUTHORIZATION) :
						webUtility.getCookie(request, HttpHeaders.AUTHORIZATION);
				if(StringUtils.isEmpty(userId) || StringUtils.isEmpty(jwt)){
					log.error("userId or jwt is empty");
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, CodefunConstants.ERR_MSG_UNAUTHORIZED);
					return;
				}
				AtomicReference<String> decodedUsername = new AtomicReference<>();
				doValidateRequestURIAuthenticatedByJwtAndUserId(isPermitted,decodedUsername, jwt, userId, request);
				Boolean isExpired = (Boolean)request.getAttribute(EXPIRED);
				if(isExpired){
					log.error("userId or jwt is empty");
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, CodefunConstants.ERR_MSG_TOKEN_EXPIRED);
					return;
				}
				doProcessFinalPermitted(isPermitted, decodedUsername, filterChain, request, response);
			}
		};
	}

	private void doProcessFinalPermitted(AtomicBoolean isPermitted, AtomicReference<String> decodedUsername, FilterChain filterChain, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		if(isPermitted.get()) {
			UserDetails userDetails = userSecurityService.loadUserByUsername(decodedUsername.get());
			UsernamePasswordAuthenticationToken authentication
					= new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		}
		else{
			doValidateDecodedUsername(decodedUsername.get(), response);
		}
	}

	private void doValidateDecodedUsername(String username, HttpServletResponse response) throws IOException{
		if(StringUtils.isNotEmpty(username)){
			response.sendError(HttpServletResponse.SC_FORBIDDEN, CodefunConstants.ERR_MSG_UNAUTHORIZED);	
		}
		else{
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, CodefunConstants.ERR_MSG_UNAUTHORIZED);	
		}
	}

    private void doValidateRequstURIAsIgnoredOrApiKeyBased(AtomicBoolean isPermitted, HttpServletRequest request, String apiKey){
		Arrays.stream(ignorePath).filter(url -> request.getRequestURI().equals("/") 
			|| apiMicroPath.concat(request.getRequestURI()).contains(url) || appWebPath.concat(request.getRequestURI()).contains(url))
				.findAny()
				.ifPresentOrElse(data-> isPermitted.set(true),()-> isPermitted.set(StringUtils.isNotEmpty(apiKey) && apiKey.equals(apiKeyInternal)));
	}

	private void doValidateRequestURIAuthenticatedByJwtAndUserId(AtomicBoolean isPermitted,AtomicReference<String> decodedUsername, String jwt, String userId, HttpServletRequest request){
		try{
			userUtility.getJwtClaim(jwt);
			Observable<UserLoginResponse> userLoginDtoObservable = Observable.just(userUtility.getCurrentUserInfo(jwt, userId));
			userLoginDtoObservable.subscribe(data->{
				if(ObjectUtils.isEmpty(data)){
					isPermitted.set(false);
				}
				else if(ObjectUtils.isNotEmpty(data) && !jwt.equals(JSON.parseObject(cacheUtility.get(Constants.RDS_LOGIN, data.getEmail()), UserLoginResponse.class).getToken())){
					isPermitted.set(false);
				}
				else{
					data.getPermissions().stream().filter(url -> apiMicroPath.concat(request.getRequestURI()).contains(url))
					.findAny()
					.ifPresentOrElse(val ->{
						isPermitted.set(true);
						decodedUsername.set(data.getEmail());
					},
					()-> isPermitted.set(false));
				}
			});
			log.info("doValidateRequestURIAuthenticatedByJwtAndUserId.isPermitted = {}", isPermitted.get());
		}
		catch(ExpiredJwtException expe){
			log.error("Oops, but jwt has been expired");
			request.setAttribute(EXPIRED, true);
		}
	}
    
}