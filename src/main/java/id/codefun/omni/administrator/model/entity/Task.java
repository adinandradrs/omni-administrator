package id.codefun.omni.administrator.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tasks")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @SequenceGenerator(name = "seq_task", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_task")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "module", length = 20)
    private String module;

    @Column(name = "task_type", length = 10)
    private String taskType;

    @Column(name = "status")
    private Integer status;

    @Column(name = "task_data", columnDefinition = "text")
    private String taskData;

    @Column(name = "task_data_before", columnDefinition = "text")
    private String taskDataBefore;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date", length = 19)
    private Date createdDate;

    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "action_date", length = 19)
    private Date actionDate;

    @Column(name = "action_by")
    private String actionBy;
    
}
