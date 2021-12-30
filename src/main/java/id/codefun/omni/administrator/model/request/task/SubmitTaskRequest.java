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
public class SubmitTaskRequest extends BaseRequest {

    private Long taskId;
    private Integer status;
    private String actionBy;
    
}
