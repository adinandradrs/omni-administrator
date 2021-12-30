package id.codefun.omni.administrator.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.request.user.UpdateUserRequest;
import id.codefun.omni.administrator.repository.RoleRepository;
import id.codefun.omni.administrator.repository.UserRepository;
import id.codefun.service.util.CodefunConstants;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UpdateUserService implements BaseService <UpdateUserRequest, ValidationResponse> {
    
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public UpdateUserService(UserRepository userRepository, RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ValidationResponse execute(UpdateUserRequest request) {
        log.info("update user [start]");
        return this.userRepository.findById(request.getId()).map(data-> {
            data.setDepartment(request.getDepartment());
            data.setFullname(request.getFullname());
            data.setOrganization(request.getOrganization());
            data.setUpdatedBy(request.getLoggedUserUpdate());
            data.setRole(this.roleRepository.findByIdAndActive(request.getRoleId()).map(role -> role).orElseThrow(()-> {
                log.error("update user failed, role is not found = {}", request.getRoleId());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role ID is not valid for current user");
            }));
            data.setStatus(request.getStatus());
            this.userRepository.save(data);
            log.info("update user [end]");
            return ValidationResponse.builder().build();
        }).orElseThrow(()-> {
            log.error("update user failed, user is not found = {}", request.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CodefunConstants.ERR_MSG_DATA_NOT_FOUND);
        });
    }
    
}
