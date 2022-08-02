package id.codefun.omni.administrator.service.user;

import java.util.Date;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.request.user.SignInUserRequest;
import id.codefun.omni.administrator.model.response.user.UserLoginResponse;
import id.codefun.omni.administrator.repository.PermissionRepository;
import id.codefun.omni.administrator.repository.UserRepository;
import id.codefun.omni.administrator.util.Constants;
import id.codefun.omni.administrator.util.UserUtility;
import id.codefun.service.util.CacheUtility;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SignInUserService implements BaseService<SignInUserRequest, UserLoginResponse> {

    @Value("${app.redis.userexpiration}")
    private Integer userCacheExpiration;

    private UserUtility userUtility;
    private UserRepository userRepository;
    private CacheUtility cacheUtility;
    private PermissionRepository permissionRepository;

    public SignInUserService(UserUtility userUtility, UserRepository userRepository, CacheUtility cacheUtility,
        PermissionRepository permissionRepository){
        this.userUtility = userUtility;
        this.userRepository = userRepository;
        this.cacheUtility = cacheUtility;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserLoginResponse execute(SignInUserRequest request) {
        Authentication authentication = this.userUtility.getAuthentication(request);
        if(authentication.isAuthenticated()){
            String newJwtToken = this.userUtility.getNewJwt(authentication, false);
            String newRefreshToken = this.userUtility.getNewJwt(authentication, true);
            return this.userRepository.findUserLoginByEmail(request.getEmail()).map(userLogin ->{
                UserLoginResponse userLoginResponse = UserLoginResponse.builder()
                .department(userLogin.getDepartment())
                .email(userLogin.getEmail())
                .fullname(userLogin.getFullname())
                .id(userLogin.getId())
                .loggedTime(new Date().getTime())
                .role(userLogin.getRole())
                .roleId(userLogin.getRoleId())
                .token(newJwtToken)
                .refreshToken(newRefreshToken)
                .permissions(permissionRepository.findPermissionPathsByRoleId(userLogin.getRoleId()))
                .build();
                this.cacheUtility.set(Constants.RDS_ADMINISTRATOR_LOGIN, userLogin.getId().toString(), JSON.toJSONString(userLoginResponse) , userCacheExpiration);
                log.info("User is logged in with email = {}", userLoginResponse.getEmail());
                return userLoginResponse;
            }).orElseThrow(()->{
                log.error("user is not elibile to login for email = {}", request.getEmail());
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, id.codefun.service.util.Constants.ERR_MSG_UNAUTHORIZED);
            });
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, id.codefun.service.util.Constants.ERR_MSG_UNAUTHORIZED);
    }
    
}
