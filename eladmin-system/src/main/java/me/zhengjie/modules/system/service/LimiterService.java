package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.Limiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Service接口
 */
public interface LimiterService {

    /**
     * 分页查询限流规则
     */
    Page<Limiter> queryAll(Limiter limiter, Pageable pageable);

    /**
     * 查询所有限流规则
     */
    List<Limiter> queryAll(Limiter limiter);

    /**
     * 根据ID查询限流规则
     */
    Limiter findById(Long id);

    /**
     * 根据限流键查询限流规则
     */
    Limiter findByLimiterKey(String limiterKey);

    /**
     * 创建限流规则
     */
    void create(Limiter limiter);

    /**
     * 更新限流规则
     */
    void update(Limiter limiter);

    /**
     * 删除限流规则
     */
    void delete(Long id);

    /**
     * 启用/禁用限流规则
     */
    void enable(Long id);
}
