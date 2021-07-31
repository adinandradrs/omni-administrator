package id.codefun.web.administrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.codefun.web.administrator.model.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
    
}
