package id.codefun.web.administrator.model.request.user;

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
    
}
