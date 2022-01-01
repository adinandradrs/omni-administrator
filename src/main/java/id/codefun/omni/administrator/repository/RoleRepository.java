package id.codefun.omni.administrator.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import id.codefun.omni.administrator.model.entity.Role;
import id.codefun.omni.administrator.model.projection.ActiveRole;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, 
    JpaSpecificationExecutor<Role> {
    
    @Query(nativeQuery = true, value = "select * from roles where id = :roleId and is_deleted=false and status = 1")
    Optional<Role> findByIdAndActive(@Param("roleId") Long roleId);

    @Query(nativeQuery = true, value = "select id, role_name roleName, role_description roleDescription from roles where is_deleted = false and status= 1")
    List<ActiveRole> findActiveRoles();

}
