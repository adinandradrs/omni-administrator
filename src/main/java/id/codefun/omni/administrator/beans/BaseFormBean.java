package id.codefun.omni.administrator.beans;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ResponseStatusException;
import id.codefun.common.model.response.ValidationResponse;
import id.codefun.omni.administrator.util.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class BaseFormBean {

    @Value("#{param['id']}")
    protected Long id;
    
    @Value("#{param['mode']}")
    protected String mode;

    protected Boolean isEligibleFindDetail(){
        return (mode.equals(Constants.MODE_UPDATE) || mode.equals(Constants.MODE_READ)) && ObjectUtils.isNotEmpty(id);
    }

    protected ValidationResponse onAjaxError(Throwable ex){
        ResponseStatusException responseStatusException = (ResponseStatusException) ex;
        log.error("error = {}", ExceptionUtils.getRootCauseMessage(responseStatusException));
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, responseStatusException.getStatus().name(), responseStatusException.getMessage());
        FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        return ValidationResponse.builder().result(false).build();
    }
    
}
