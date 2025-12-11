package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.LimiterRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Repository
 */
public interface LimiterRuleRepository extends JpaRepository<LimiterRule, Long>, JpaSpecificationExecutor<LimiterRule> {

    /**
     * 根据URI查询限流规则
     * @param uri 请求URI
     * @return 限流规则
     */
    Optional<LimiterRule> findByUri(String uri);
}
