package id.codefun.web.administrator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import id.codefun.web.administrator.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, 
    JpaSpecificationExecutor<User> {
    
}
