package id.codefun.front.admin.model.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginResponse implements Serializable {
    
    private Long loggedIn;
    private String username;
    private String fullname;
    private String role;

}
