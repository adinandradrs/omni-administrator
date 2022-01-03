package id.codefun.omni.administrator.model.projection;

import java.util.Date;

public interface TaskSearch {
    
    public Long getId();
    public String getModule();
    public String getTaskType();
    public String getCreatedBy();
    public Date getCreatedDate();

}
