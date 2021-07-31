package id.codefun.web.administrator.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.codefun.common.model.entity.BaseEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Table(name = "users", indexes = {
    @Index(name = "USERS_INDEX_0", columnList = "status"),
    @Index(name = "USERS_INDEX_1", columnList = "email"),
    @Index(name = "USERS_INDEX_2", columnList = "is_deleted")
})
@Entity
public class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_user", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_user")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name="email")
    private String email;

    @Column(name="fullname")
    private String fullname;

    @Column(name="organization", length = 100)
    private String organization;

    @Column(name="department", length = 100)
    private String department;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "wrong_password")
    private Integer wrongPassword;

    @Column(name = "last_updated_password_date")
    private Date lastUpdatedPasswordDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "id") }, inverseJoinColumns = {
        @JoinColumn(name = "role_id", referencedColumnName = "id") })
    @JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
    @JsonBackReference
    private Role role;
    
}
