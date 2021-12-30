package id.codefun.omni.administrator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import id.codefun.omni.administrator.model.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    
    @Query(nativeQuery = true, value = "select * from tasks where id = :taskId and status = 0")
    Optional<Task> findPendingById(@Param("taskId") Long taskId);

}
