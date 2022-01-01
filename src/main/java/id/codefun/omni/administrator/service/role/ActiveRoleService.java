package id.codefun.omni.administrator.service.role;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import id.codefun.common.model.request.BaseRequest;
import id.codefun.common.service.BaseService;
import id.codefun.omni.administrator.model.response.role.RoleListResponse;
import id.codefun.omni.administrator.model.response.role.RoleResponse;
import id.codefun.omni.administrator.repository.RoleRepository;

@Service
public class ActiveRoleService implements BaseService<BaseRequest, RoleListResponse> {

    private RoleRepository roleRepository;

    public ActiveRoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleListResponse execute(BaseRequest request) {
        return RoleListResponse.builder().roleList(
            roleRepository.findActiveRoles().stream().map(data-> RoleResponse.builder()
                .description(data.getRoleDescription())
                .name(data.getRoleName())
                .id(data.getId())
                .build())
            .collect(Collectors.toList())
        ).build();
    }
    
}
