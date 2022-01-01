package id.codefun.omni.administrator.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import id.codefun.omni.administrator.model.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Query(value = "select p.permission_path from permissions p inner join role_permission rp on p.id = rp.role_id where rp.role_id = :roleId", nativeQuery = true)
    List<String> findPermissionPathsByRoleId(@Param("roleId") Long roleId);
    
}
