package id.codefun.omni.administrator.beans.user;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.ManagedBean;
import com.alibaba.fastjson.JSON;
import org.springframework.web.context.annotation.RequestScope;
import id.codefun.common.model.request.BaseRequest;
import id.codefun.common.model.request.FindByIdRequest;
import id.codefun.omni.administrator.beans.FormBean;
import id.codefun.omni.administrator.model.request.task.AddTaskRequest;
import id.codefun.omni.administrator.model.request.user.AddUserRequest;
import id.codefun.omni.administrator.model.request.user.UpdateUserRequest;
import id.codefun.omni.administrator.model.response.role.RoleResponse;
import id.codefun.omni.administrator.model.response.user.UserResponse;
import id.codefun.omni.administrator.service.role.ActiveRoleService;
import id.codefun.omni.administrator.service.task.AddTaskService;
import id.codefun.omni.administrator.service.user.DetailUserService;
import id.codefun.omni.administrator.service.user.ValidateUserService;
import id.codefun.omni.administrator.util.Constants;
import id.codefun.service.util.CodefunConstants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean
@RequestScope
@Data
@Slf4j
public class FormUserBean extends FormBean {

    private AddUserRequest addUserRequest = AddUserRequest.builder().build();
    private UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().build();
    private UserResponse userResponse = UserResponse.builder().build();
    private UserResponse originalDataUser = UserResponse.builder().build();
    private ValidateUserService validateUserService;
    private AddTaskService addTaskService;
    private ActiveRoleService activeRoleService;
    private DetailUserService detailUserService;
    
    public FormUserBean(ValidateUserService validateUserService, AddTaskService addTaskService, ActiveRoleService activeRoleService,
        DetailUserService detailUserService){
        this.validateUserService = validateUserService;
        this.addTaskService = addTaskService;
        this.activeRoleService = activeRoleService;
        this.detailUserService = detailUserService;
    }

    public Map<Integer, String> getStatusMap() {
        Map<Integer, String> statusMap = new HashMap<>();
        Arrays.asList(CodefunConstants.USER_STATUS.values()).forEach(data->
            statusMap.put(data.getValue(), data.getMsg())
        ); 
        return statusMap;
    }

    public List<RoleResponse> getActiveRoles(){
        return this.activeRoleService.execute(new BaseRequest()).getRoleList();
    }

    public UserResponse getUserDetail(){
        if(isEligibleFindDetail()){
            originalDataUser = this.detailUserService.execute(FindByIdRequest.builder().id(id).build());
            userResponse = originalDataUser;
            log.info("user detail to read or update = {}", JSON.toJSONString(userResponse));
            return userResponse;
        }
        return null;
    }

    public void doSave(){
        if(mode.equals(Constants.MODE_ADD)){
            log.info("addUserRequest = {} with mode {}",JSON.toJSONString(addUserRequest), mode, id);
            this.validateUserService.execute(null, addUserRequest.getEmail(), addUserRequest.getFullname(), addUserRequest.getRoleId());
            this.addTaskService.execute(
                AddTaskRequest.builder()
                .module(Constants.TASK_MODULE.USER.toString())
                .taskData(JSON.toJSONString(addUserRequest))
                .taskType(Constants.TASK_TYPE.CREATE.toString())
                .build()
            );
        }
        else if (mode.equals(Constants.MODE_UPDATE)){
            log.info("updateUserRequest = {} with mode {}",JSON.toJSONString(userResponse), mode, id);
            this.validateUserService.execute(id, userResponse.getEmail(), userResponse.getFullname(), userResponse.getRoleId());
            this.updateUserRequest.setDepartment(userResponse.getDepartment());
            this.updateUserRequest.setFullname(userResponse.getFullname());
            this.updateUserRequest.setRoleId(userResponse.getRoleId());
            this.updateUserRequest.setOrganization(userResponse.getOrganization());
            this.updateUserRequest.setStatus(userResponse.getStatus());
            this.addTaskService.execute(
                AddTaskRequest.builder()
                .module(Constants.TASK_MODULE.USER.toString())
                .taskData(JSON.toJSONString(addUserRequest))
                .taskDataBefore(JSON.toJSONString(originalDataUser))
                .taskType(Constants.TASK_TYPE.CREATE.toString())
                .build()
            );
        }
        
    }
    
}