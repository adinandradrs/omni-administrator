package id.codefun.omni.administrator.service.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.request.DeleteRequest;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.repository.UserRepository;
import id.codefun.service.util.CodefunConstants;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DeleteUserService implements BaseService<DeleteRequest, ValidationResponse> {
    
    private UserRepository userRepository;

    public DeleteUserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public ValidationResponse execute(DeleteRequest request) {
        return userRepository.findById(request.getId()).map(data -> {
            data.setIsDeleted(true);
            data.setUpdatedBy(request.getLoggedUser());
            data.setStatus(CodefunConstants.COMMON_STATUS.INACTIVE.getValue());
            return ValidationResponse.builder().result(true).build();
        }).orElseThrow(() -> {
            log.error("User to delete is not found = {} by {}", request.getId(), request.getLoggedUser());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CodefunConstants.ERR_MSG_DATA_NOT_FOUND);
        });
    }
    
}
