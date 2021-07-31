package id.codefun.web.administrator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.codefun.web.administrator.model.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>, 
    JpaSpecificationExecutor<Role> {
    
    @Query(nativeQuery = true, value = "select * from roles where id = :roleId and is_deleted=false and status = 1")
    public Optional<Role> findByIdAndActive(@Param("roleId") Long roleId);

}
