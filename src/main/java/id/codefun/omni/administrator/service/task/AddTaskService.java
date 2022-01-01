package id.codefun.omni.administrator.service.task;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.entity.Task;
import id.codefun.omni.administrator.model.request.task.AddTaskRequest;
import id.codefun.omni.administrator.repository.TaskRepository;
import id.codefun.omni.administrator.util.Constants;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AddTaskService implements BaseService<AddTaskRequest, ValidationResponse> {
    
    private TaskRepository taskRepository;
    private String taskData = StringUtils.EMPTY;
    private String taskDataBefore = StringUtils.EMPTY;
    private String taskType = StringUtils.EMPTY;

    public AddTaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    private void doValidateTaskBody(Object taskData, Object taskBefore, String taskType){
        if((taskType.equals(Constants.TASK_TYPE.CREATE.toString()) || taskType.equals(Constants.TASK_TYPE.UPDATE.toString())) && ObjectUtils.isEmpty(taskData)){
            log.error("Task data is null for taskType = {}", taskType);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task data cannot be empty");
        }
        if((taskType.equals(Constants.TASK_TYPE.UPDATE.toString()) || taskType.equals(Constants.TASK_TYPE.DELETE.toString())) && ObjectUtils.isEmpty(taskBefore)){
            log.error("Task data before is null for taskType = {}", taskType);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task data before cannot be empty");
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public ValidationResponse execute(AddTaskRequest request) {
        this.doValidateTaskBody(request.getTaskData(), request.getTaskDataBefore(), request.getTaskType());
        log.info("user {} is create a task with a type as {} for module {}", request.getCreatedBy(), request.getTaskType(), request.getModule());
        if(request.getTaskType().equals(Constants.TASK_TYPE.CREATE.toString())){
            taskType = Constants.TASK_TYPE.CREATE.toString();
            taskData = JSON.toJSONString(request.getTaskData());
        }
        else if (request.getTaskType().equals(Constants.TASK_TYPE.UPDATE.toString())){
            taskType = Constants.TASK_TYPE.UPDATE.toString();
            taskData = JSON.toJSONString(request.getTaskData());
            taskDataBefore = JSON.toJSONString(request.getTaskDataBefore());
        }
        else if (request.getTaskType().equals(Constants.TASK_TYPE.DELETE.toString())){
            taskType = Constants.TASK_TYPE.DELETE.toString();
            taskDataBefore = JSON.toJSONString(request.getTaskDataBefore());
        }
        else{
            log.error("task type is invalid = {}", request.getTaskType());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown task type is given");
        }
        this.taskRepository.save(
            Task.builder()
            .createdBy(request.getCreatedBy())
            .taskData(StringUtils.isNotEmpty(taskData) ? taskData : null)
            .taskDataBefore(StringUtils.isNotEmpty(taskDataBefore) ? taskDataBefore : null)
            .module(request.getModule())
            .taskType(taskType)
            .status(Constants.TASK_STATUS.PENDING.getValue())
            .build());
        return ValidationResponse.builder().result(true).build();
    }
    
}
