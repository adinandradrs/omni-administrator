package id.codefun.web.administrator.model.request.user;

import id.codefun.common.model.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;

public class BaseUserRequest extends BaseRequest {

    @ApiModelProperty(value = "User Email")
    private String email;
    @ApiModelProperty(value = "User Fullname")
    private String fullname;
    @ApiModelProperty(value = "User Organization Detail")
    private String organization;
    @ApiModelProperty(value = "User Department Detail")
    private String department;
    @ApiModelProperty(value = "User Role ID")
    private Long roleId;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullname() {
        return fullname;
    }
    
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
    
    public String getOrganization() {
        return organization;
    }
    
    public void setOrganization(String organization) {
        this.organization = organization;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public Long getRoleId() {
        return roleId;
    }
    
    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

}
