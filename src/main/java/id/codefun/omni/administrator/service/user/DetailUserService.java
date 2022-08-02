package id.codefun.omni.administrator.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.request.FindByIdRequest;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.response.user.UserResponse;
import id.codefun.omni.administrator.repository.UserRepository;
import id.codefun.service.util.Constants;

@Service
public class DetailUserService implements BaseService<FindByIdRequest, UserResponse> {

    private UserRepository userRepository;

    public DetailUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse execute(FindByIdRequest request) {
        return userRepository.findById(request.getId()).map(user -> UserResponse
        .builder()
        .department(user.getDepartment())
        .email(user.getEmail())
        .roleId(user.getRole().getId())
        .fullname(user.getFullname())
        .status(user.getStatus())
        .organization(user.getOrganization())
        .build())
        .orElseThrow(()->{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, Constants.ERR_MSG_DATA_NOT_FOUND);
        });
    }
    
}
