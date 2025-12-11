package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.LimiterLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流日志Repository
 */
public interface LimiterLogRepository extends JpaRepository<LimiterLog, Long>, JpaSpecificationExecutor<LimiterLog> {

    /**
     * 分页查询限流日志
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<LimiterLog> findAll(Pageable pageable);
}
