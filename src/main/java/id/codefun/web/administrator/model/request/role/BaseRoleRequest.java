package id.codefun.web.administrator.model.request.role;

import java.util.List;

import id.co.bankmandiri.kum.user.model.request.shared.SessionRequest;
import io.swagger.annotations.ApiModelProperty;

public class BaseRoleRequest extends SessionRequest {

    @ApiModelProperty(example = "System Administrator", value="Role Name")
    private String name;
	@ApiModelProperty(example = "System administrator as guardian of the system", value="Role Description")
    private String description;
	@ApiModelProperty(value="Menu IDs")
    private List<Long> menuIds;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Long> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<Long> menuIds) {
        this.menuIds = menuIds;
    }

    
}