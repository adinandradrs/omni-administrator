package id.codefun.omni.administrator.model.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserRequest extends BaseUserRequest {

    @ApiModelProperty(value = "User Email")
    private String email;
    
    @JsonIgnore
    private String loggedUserCreate;

}
