package id.codefun.omni.administrator.beans;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import javax.annotation.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.annotation.SessionScope;
import id.codefun.omni.administrator.util.Constants;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ManagedBean
@SessionScope
@Data
@Slf4j
public class BaseSessionBean {

    private HttpServletRequest servletRequest;
    private List<String> languagePackList;

    public BaseSessionBean(HttpServletRequest servletRequest){
        this.servletRequest = servletRequest;
    }

    public void doFetchSession(){
        FacesContext.getCurrentInstance().getViewRoot().setLocale(new Locale(
            StringUtils.isEmpty(servletRequest.getParameter("language")) ? "en" : servletRequest.getParameter("language")
        ));
    }

    public String getCurrentDateInBahasa(){
        Calendar calendar = Calendar.getInstance();
        Integer date = calendar.get(Calendar.DATE);
        Integer year = calendar.get(Calendar.YEAR);
        String month = Constants.MONTH_MAP_INDONESIA.get(calendar.get(Calendar.MONTH));
        String day = Constants.DAY_MAP_INDONESIA.get(calendar.get(Calendar.DAY_OF_WEEK));
        return day.concat(", ").concat(date.toString()).concat(StringUtils.SPACE).concat(month).concat(StringUtils.SPACE).concat(year.toString());
    }
    
}
