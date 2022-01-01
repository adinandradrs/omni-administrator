package id.codefun.omni.administrator.model.response.role;

import java.util.List;
import id.codefun.common.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleListResponse extends BaseResponse {

    private List<RoleResponse> roleList;
    
}
