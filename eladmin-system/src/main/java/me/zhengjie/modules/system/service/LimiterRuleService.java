package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.LimiterRule;
import me.zhengjie.modules.system.service.dto.LimiterRuleQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Service
 */
public interface LimiterRuleService {

    /**
     * 分页查询限流规则
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    Map<String, Object> queryAll(LimiterRuleQueryCriteria criteria, Pageable pageable);

    /**
     * 根据URI查询限流规则
     * @param uri 请求URI
     * @return 限流规则
     */
    LimiterRule findByUri(String uri);

    /**
     * 创建限流规则
     * @param limiterRule 限流规则
     */
    void create(LimiterRule limiterRule);

    /**
     * 更新限流规则
     * @param limiterRule 限流规则
     */
    void update(LimiterRule limiterRule);

    /**
     * 删除限流规则
     * @param id 规则ID
     */
    void delete(Long id);
}
