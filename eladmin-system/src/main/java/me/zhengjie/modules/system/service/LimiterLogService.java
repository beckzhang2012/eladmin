package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.LimiterLog;
import me.zhengjie.modules.system.service.dto.LimiterLogQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Map;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流日志Service
 */
public interface LimiterLogService {

    /**
     * 分页查询限流日志
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    Map<String, Object> queryAll(LimiterLogQueryCriteria criteria, Pageable pageable);

    /**
     * 创建限流日志
     * @param limiterLog 限流日志
     */
    void create(LimiterLog limiterLog);
}
