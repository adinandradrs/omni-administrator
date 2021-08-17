package id.codefun.web.administrator.model.request.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import id.codefun.common.model.request.BaseRequest;

public class SessionRequest extends BaseRequest {

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long loggedUserId;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private String loggedUser;

    public Long getLoggedUserId() {
        return loggedUserId;
    }

    public void setLoggedUserId(Long loggedUserId) {
        this.loggedUserId = loggedUserId;
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }    
    
}