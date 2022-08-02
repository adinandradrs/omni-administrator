package id.codefun.omni.administrator.service.task;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import id.codefun.common.model.request.SpecificationRequest;
import id.codefun.common.model.response.PageResponse;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.entity.Task;
import id.codefun.omni.administrator.model.response.task.TaskResponse;
import id.codefun.omni.administrator.repository.TaskRepository;

@Service
public class SearchTaskService implements BaseService<SpecificationRequest, PageResponse> {

    private TaskRepository taskRepository;

    public SearchTaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public PageResponse execute(SpecificationRequest request) {
        Page<Task> taskPage = this.taskRepository.findAll(request.getSpecification(), request.getPageable());
        return PageResponse.builder()
        .page(
            taskPage.map(data->
                TaskResponse.builder()
                .id(data.getId())
                .module(data.getModule())
                .status(data.getStatus())
                .taskType(data.getTaskType())
                .createdBy(data.getCreatedBy())
                .build()
        )).build();
        
    }
    
}
