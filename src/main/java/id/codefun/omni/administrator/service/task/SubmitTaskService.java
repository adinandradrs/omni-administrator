package id.codefun.omni.administrator.service.task;

import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.request.DeleteRequest;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.request.task.SubmitTaskRequest;
import id.codefun.omni.administrator.model.request.user.AddUserRequest;
import id.codefun.omni.administrator.model.request.user.UpdateUserRequest;
import id.codefun.omni.administrator.repository.TaskRepository;
import id.codefun.omni.administrator.service.user.AddUserService;
import id.codefun.omni.administrator.service.user.DeleteUserService;
import id.codefun.omni.administrator.service.user.UpdateUserService;
import id.codefun.omni.administrator.util.Constants;
import id.codefun.service.util.CodefunConstants;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class SubmitTaskService implements BaseService<SubmitTaskRequest, ValidationResponse> {
    
    private static final String ERR_MSG_ACTION_TYPE_INVALID = "Action type is invalid";

    private TaskRepository taskRepository;
    private AddUserService addUserService;
    private UpdateUserService updateUserService;
    private DeleteUserService deleteUserService;

    public SubmitTaskService(TaskRepository taskRepository, AddUserService addUserService, UpdateUserService updateUserService,
        DeleteUserService deleteUserService){
        this.taskRepository = taskRepository;
        this.addUserService = addUserService;
        this.updateUserService = updateUserService;
        this.deleteUserService = deleteUserService;
    }

    @Override
    public ValidationResponse execute(SubmitTaskRequest request) {
        log.info("User submit task [start] = {}", request.getActionBy());
        return this.taskRepository.findPendingById(request.getTaskId()).map(data -> {
            data.setActionBy(request.getActionBy());
            data.setStatus(request.getStatus());
            this.taskRepository.save(data);
            if(request.getStatus().compareTo(Constants.TASK_STATUS.APPROVED.getValue()) == 0){
                return this.getTaskApprovedResult(data.getModule(), data.getTaskType(), data.getTaskData());
            }
            else if (request.getStatus().compareTo(Constants.TASK_STATUS.REJECTED.getValue()) == 0) {
                log.info("User submit task [end] = {} with reject", data.getModule());
                return ValidationResponse.builder().result(true).build();
            }
            else{
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Submitted task status is not valid");
            }
        }).orElseThrow(()->{
            log.error("Failed to submit task, data is not found = taskId[{}] by {}", request.getTaskId(), request.getActionBy());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, CodefunConstants.ERR_MSG_DATA_NOT_FOUND);
        });
    }

    private ValidationResponse getTaskApprovedResult(String module, String taskType, String jsonData){
        log.info("User submit task [end] = {} with approve", module);
        if(module.equals(Constants.TASK_MODULE.USER.toString())){
            return this.getTaskApprovedResultOnUser(taskType, jsonData);
        }
        else{
            log.error("Submitted module is not valid for taskType {} and module {}", taskType, module);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Submitted module is not found in system");
        }
    }

    private ValidationResponse getTaskApprovedResultOnUser(String taskType, String jsonData){
        if(taskType.equals(Constants.TASK_TYPE.CREATE.toString())){
            return this.addUserService.execute(JSON.parseObject(jsonData, AddUserRequest.class));
        }
        else if(taskType.equals(Constants.TASK_TYPE.UPDATE.toString())){
            return this.updateUserService.execute(JSON.parseObject(jsonData, UpdateUserRequest.class));
        }
        else if(taskType.equals(Constants.TASK_TYPE.DELETE.toString())){
            return this.deleteUserService.execute(JSON.parseObject(jsonData, DeleteRequest.class));
        }
        else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ERR_MSG_ACTION_TYPE_INVALID);
        }
    }
    
}
