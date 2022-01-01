package id.codefun.omni.administrator.model.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import id.codefun.common.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends BaseResponse {
    
    private Long id;
    private String email;
    private String fullname;
    private Long roleId;
    private String organization;
    private String department;
    private Integer status;

}
