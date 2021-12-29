package id.codefun.omni.administrator.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSON;
import id.codefun.service.util.CacheUtility;
import id.codefun.omni.administrator.model.response.UserLoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
* @author  Adinandra Dharmasurya
* @version 1.0
* @since   2020-09-19
*/
@Component
@Slf4j
public class UserUtility {

    @Value("${app.jwt.expiration}")
    private Integer epicJwtExpiration;

    @Value("${app.jwt.refreshexpiration}")
    private Integer epicRefreshExpiration;
    
    @Value("${app.jwt.secret}")
    private String epicJwtSecret;

    @Value("${app.jwt.refreshsecret}")
    private String epicRefreshSecret;

    @Value("${app.user.regexPasswd}")
    private String regexPasswd;

    private CacheUtility cacheUtility;

    public UserUtility(CacheUtility cacheUtility){
        this.cacheUtility = cacheUtility;
    }

    public Boolean isValidPasswordStrength(String password){
        Pattern pattern = Pattern.compile(regexPasswd);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches()
                && !password.toLowerCase().contains("password")
                && !password.toLowerCase().contains("p@ssw0rd")
                && !password.toLowerCase().contains("admin")
                && !password.toLowerCase().contains("administrator");
    }

    public String getNewJwt(Authentication authentication, Boolean isRefreshToken) {
    	UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();
    	Base64 base64 = new Base64();
    	String username = new String(base64.encode(userPrincipal.getUsername().getBytes()));
        Date expiredOn = new Date((new Date()).getTime() + ((isRefreshToken) ? epicRefreshExpiration : epicJwtExpiration) * 1000);
        log.info("username JWT {} will be applied until {}", username);
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(expiredOn)
            .signWith(SignatureAlgorithm.HS512, (isRefreshToken) ? epicRefreshSecret : epicJwtSecret)
            .compact();
    }

    public String getNewJwt(String username){
        Base64 base64 = new Base64();
        username = new String(base64.encode(username.getBytes()));
        Date expiredOn = new Date((new Date()).getTime() + epicJwtExpiration * 1000);
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(expiredOn)
            .signWith(SignatureAlgorithm.HS512, epicJwtSecret)
            .compact();
    }

    public Jws<Claims> getOptJwtClaim(String token) {
		return Jwts.parser().setSigningKey(this.epicJwtSecret).parseClaimsJws(token);
	}

    public Jws<Claims> getJwtClaim(String token){
        if(StringUtils.isEmpty(token)){
            return null;
        }
        return Jwts.parser().setSigningKey(epicJwtSecret).parseClaimsJws(token);
    }

    public Jws<Claims> getRefreshClaim(String refreshToken){
        if(StringUtils.isEmpty(refreshToken)){
            return null;
        }
        return Jwts.parser().setSigningKey(epicRefreshSecret).parseClaimsJws(refreshToken);
    }

    public String getUsernameByRefreshToken(String refreshToken){
        Jws<Claims> claims = this.getRefreshClaim(refreshToken);
        if(ObjectUtils.isEmpty(claims)){
            log.error("getCurrentUserInfo.error = JWT claim is null");
            return null;
        }
        String usernameEncode = claims.getBody().getSubject();
        Base64 base64 = new Base64();
		return new String(base64.decode(usernameEncode.getBytes()));
    }

    public UserLoginResponse getCurrentUserInfo(String token, String headerUserId){
        Long userId = Long.valueOf(headerUserId);
        UserLoginResponse currentUserLogin;
        Jws<Claims> claims = this.getJwtClaim(token);
        if(ObjectUtils.isEmpty(claims)){
            log.error("getCurrentUserInfo.error = JWT claim is null");
            return null;
        }
        String usernameEncode = claims.getBody().getSubject();
        Base64 base64 = new Base64();
		String decodedUsername = new String(base64.decode(usernameEncode.getBytes()));
        String userLoginCache = cacheUtility.get(Constants.RDS_LOGIN, decodedUsername);
        if(StringUtils.isEmpty(userLoginCache)){
            log.error("getCurrentUserInfo.error = userLoginCache is empty");
            return null;
        }
        currentUserLogin = JSON.parseObject(userLoginCache, UserLoginResponse.class);
        if(!currentUserLogin.getId().equals(userId)){
            log.error("getCurrentUserInfo.error = mismatch header");
            return null;
        }
        return currentUserLogin;
    }
    
}