package id.codefun.omni.administrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import id.codefun.omni.administrator.model.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
}
