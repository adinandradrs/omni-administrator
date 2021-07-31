package id.codefun.web.administrator.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.codefun.web.administrator.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, 
    JpaSpecificationExecutor<User> {

    @Query(nativeQuery = true, value = "select * from users where email = :email and is_deleted = false")
    public Optional<User> findByEmail(@Param("email") String email);
    
}
