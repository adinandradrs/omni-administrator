package id.codefun.omni.administrator.model.request.user;

import id.codefun.common.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInUserRequest extends BaseRequest {

    private String email;
    private String password;
    private Boolean isRemember;
    
}
