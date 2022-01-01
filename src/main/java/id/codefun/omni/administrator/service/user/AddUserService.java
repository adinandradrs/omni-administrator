package id.codefun.omni.administrator.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.entity.User;
import id.codefun.omni.administrator.model.request.user.AddUserRequest;
import id.codefun.omni.administrator.repository.RoleRepository;
import id.codefun.omni.administrator.repository.UserRepository;
import id.codefun.service.util.CodefunConstants;
import lombok.extern.log4j.Log4j2;
@Service
@Log4j2
public class AddUserService implements BaseService<AddUserRequest, ValidationResponse> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ValidateUserService validateUserService;
    private PasswordEncoder passwordEncoder;
    @Value("app.user.defaultpassword")
    private String defaultPassword;

    public AddUserService(UserRepository userRepository, ValidateUserService validateUserService, RoleRepository roleRepository,
        PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.validateUserService = validateUserService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ValidationResponse execute(AddUserRequest request) {
        if(validateUserService.execute(null, request.getEmail(), request.getFullname(), request.getRoleId()).getResult()){
            this.roleRepository.findByIdAndActive(request.getRoleId()).ifPresentOrElse(data-> 
                {
                    User newUser = User.builder()
                        .email(request.getEmail())
                        .role(data)
                        .fullname(request.getFullname())
                        .organization(request.getOrganization())
                        .password(passwordEncoder.encode(defaultPassword))
                        .isDeleted(false)
                        .status(CodefunConstants.COMMON_STATUS.ACTIVE.getValue())
                        .department(request.getDepartment())
                    .build();
                    newUser.setCreatedBy(request.getLoggedUserCreate());
                    userRepository.save(newUser);
                },
                ()->{
                    log.error("Role ID {} is not found for user = {}", request.getRoleId(), request.getEmail());
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ID is not valid for new user");
                }
            );
        }
        return ValidationResponse.builder().result(true).build();
    }
    
}
