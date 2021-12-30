package id.codefun.omni.administrator.model.request.task;

import id.codefun.common.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddTaskRequest extends BaseRequest {

    private String module;
    private String taskType;
    private Object taskData;
    private Object taskDataBefore;
    private String createdBy;
    
}
