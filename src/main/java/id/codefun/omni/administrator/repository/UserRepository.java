package id.codefun.omni.administrator.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import id.codefun.omni.administrator.model.entity.User;
import id.codefun.omni.administrator.model.projection.UserLogin;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, 
    JpaSpecificationExecutor<User> {

    @Query(nativeQuery = true, value = "select * from users where email = :email and is_deleted = false")
    Optional<User> findByEmail(@Param("email") String email);

    @Query(nativeQuery = true, value = "select u.id, u.fullname, u.email, u.organization, u.department, r.id roleId, r.role_name role from users u inner join user_role ur on u.id = ur.user_id inner join roles r on r.id = ur.role_id where u.email = :email and u.is_deleted = false and u.status = 1")
    Optional<UserLogin> findUserLoginByEmail(@Param("email") String email);
    
}
