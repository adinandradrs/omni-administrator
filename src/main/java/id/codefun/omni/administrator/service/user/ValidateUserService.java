package id.codefun.omni.administrator.service.user;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.request.user.AddUserRequest;
import id.codefun.omni.administrator.repository.UserRepository;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ValidateUserService implements BaseService<AddUserRequest, ValidationResponse> {

    private UserRepository userRepository;

    public ValidateUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public ValidationResponse execute(AddUserRequest request) {
        throw new UnsupportedOperationException();
    }

    public ValidationResponse execute(Long userId, String email, String fullname, Long roleId){
        if(ObjectUtils.isEmpty(userId) && ObjectUtils.isEmpty(email)){
            log.error("Email is empty for user = {}", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if(StringUtils.isEmpty(fullname)){
            log.error("Fullname is empty for user = {}", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Fullname is required");
        }
        if(ObjectUtils.isEmpty(roleId)){
            log.error("Role ID is empty for user = {}", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role is required");
        }
        doValidateIsEmailDuplicated(userId, email);
        return ValidationResponse.builder().result(true).build();
    }

    private void doValidateIsEmailDuplicated(Long userId, String email){
        if(ObjectUtils.isEmpty(userId) && userRepository.findByEmail(email).isPresent()){
            log.error("duplicated email for new user = {}", email);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to add a new user, email is already exists for another user");
        }
        else if(ObjectUtils.isNotEmpty(userId)){
            userRepository.findByEmail(email).ifPresent(data->{
                if(data.getId().compareTo(userId) != 0){
                    log.error("duplicated email for existing user = {}", email);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update an existing user, email is already exists for another user");
                }
            });
        }
    }
    
}
