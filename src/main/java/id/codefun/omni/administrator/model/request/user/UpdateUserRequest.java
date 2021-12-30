package id.codefun.omni.administrator.model.request.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest extends BaseUserRequest {

    @ApiModelProperty(value="User ID to Update")
    private Long id;

    @ApiModelProperty(value = "User Status")
    private Integer status;

    @JsonIgnore
    private String loggedUserUpdate;
    
}
