package id.codefun.web.administrator.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.common.service.BaseService;
import id.codefun.web.administrator.model.entity.User;
import id.codefun.web.administrator.model.request.user.AddUserRequest;
import id.codefun.web.administrator.repository.RoleRepository;
import id.codefun.web.administrator.repository.UserRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AddUserService implements BaseService<AddUserRequest, ValidationResponse> {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ValidateUserService validateUserService;

    public AddUserService(UserRepository userRepository, ValidateUserService validateUserService, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.validateUserService = validateUserService;
        this.roleRepository = roleRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ValidationResponse execute(AddUserRequest request) {
        if(validateUserService.execute(null, request.getEmail(), request.getFullname(), request.getRoleId()).getResult()){
            this.roleRepository.findByIdAndActive(request.getRoleId()).ifPresentOrElse(data-> 
                {
                    User newUser = new User();
                    newUser.setEmail(request.getEmail());
                    newUser.setRole(data);
                    newUser.setFullname(request.getFullname());
                    newUser.setOrganization(request.getOrganization());
                    newUser.setDepartment(request.getDepartment());
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
