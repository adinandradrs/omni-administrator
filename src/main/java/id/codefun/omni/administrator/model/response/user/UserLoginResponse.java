package id.codefun.omni.administrator.model.response.user;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import id.codefun.common.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLoginResponse extends BaseResponse {

    private Long id;
    private String email;
    private String fullname;
    private Long loggedTime;
    private Boolean isRemember;
    private Long roleId;
    private String role;
    private List<String> permissions;
    private String token;
    private String refreshToken;
    private String organization;
    private String department;
    
}
