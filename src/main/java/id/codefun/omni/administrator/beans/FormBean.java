package id.codefun.omni.administrator.beans;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;

import id.codefun.omni.administrator.util.Constants;
import lombok.Data;

@Data
public class FormBean {

    @Value("#{param['id']}")
    protected Long id;
    
    @Value("#{param['mode']}")
    protected String mode;

    protected Boolean isEligibleFindDetail(){
        return (mode.equals(Constants.MODE_UPDATE) || mode.equals(Constants.MODE_READ)) && ObjectUtils.isNotEmpty(id);
    }
    
}
