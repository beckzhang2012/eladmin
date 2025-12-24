package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long>, JpaSpecificationExecutor<TaskLog> {

    @Query("SELECT tl FROM TaskLog tl WHERE tl.taskId = :taskId ORDER BY tl.logTime DESC")
    List<TaskLog> findByTaskId(@Param("taskId") Long taskId);
}
