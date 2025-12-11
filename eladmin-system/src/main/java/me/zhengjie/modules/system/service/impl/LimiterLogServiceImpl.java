package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.LimiterLog;
import me.zhengjie.modules.system.repository.LimiterLogRepository;
import me.zhengjie.modules.system.service.LimiterLogService;
import me.zhengjie.modules.system.service.dto.LimiterLogQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流日志Service实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LimiterLogServiceImpl implements LimiterLogService {

    private final LimiterLogRepository limiterLogRepository;

    public LimiterLogServiceImpl(LimiterLogRepository limiterLogRepository) {
        this.limiterLogRepository = limiterLogRepository;
    }

    @Override
    public Map<String, Object> queryAll(LimiterLogQueryCriteria criteria, Pageable pageable) {
        return PageUtil.toPage(limiterLogRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable));
    }

    @Override
    public void create(LimiterLog limiterLog) {
        limiterLogRepository.save(limiterLog);
    }
}
