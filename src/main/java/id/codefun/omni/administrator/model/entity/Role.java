package id.codefun.omni.administrator.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import id.codefun.common.model.entity.BaseEntity;
import lombok.Data;

@Data
@Entity
@Table(name = "roles", indexes = {
    @Index(name = "ROLES_INDX_0", columnList = "role_name"),
    @Index(name = "ROLES_INDX_1", columnList = "status")
})
public class Role extends BaseEntity {

    @Id
    @SequenceGenerator(name = "seq_role", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "seq_role")
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "role_name", length = 100)
    private String roleName;

    @Column(name="role_description")
    private String roleDescription;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "status")
    private Integer status;

    @Column(name = "is_primary")
    private Boolean isPrimary;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="role_permission",joinColumns= { @JoinColumn(name="role_id")},inverseJoinColumns= {@JoinColumn(name="permission_id")})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<Permission> permissions = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
        joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Set<User> users;

}