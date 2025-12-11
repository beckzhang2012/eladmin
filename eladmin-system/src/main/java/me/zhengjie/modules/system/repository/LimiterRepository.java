package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.Limiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Repository
 */
public interface LimiterRepository extends JpaRepository<Limiter, Long>, JpaSpecificationExecutor<Limiter> {

    /**
     * 根据限流键查询限流规则
     */
    Limiter findByLimiterKey(String limiterKey);
}
