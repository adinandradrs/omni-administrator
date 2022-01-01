package id.codefun.omni.administrator.model.response.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import id.codefun.common.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
public class TaskResponse extends BaseResponse {

    private Long id;
    private String taskData;
    private String taskDataBefore;
    private String module;
    private String createdBy;
    private Long createdDate;
    private String actionBy;
    private Long actionDate;
    private String taskType;
    private Integer status;
    
}
