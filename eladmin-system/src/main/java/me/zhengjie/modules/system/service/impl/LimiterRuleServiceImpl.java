package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.LimiterRule;
import me.zhengjie.modules.system.repository.LimiterRuleRepository;
import me.zhengjie.modules.system.service.LimiterRuleService;
import me.zhengjie.modules.system.service.dto.LimiterRuleQueryCriteria;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Service实现
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class LimiterRuleServiceImpl implements LimiterRuleService {

    private final LimiterRuleRepository limiterRuleRepository;

    public LimiterRuleServiceImpl(LimiterRuleRepository limiterRuleRepository) {
        this.limiterRuleRepository = limiterRuleRepository;
    }

    @Override
    public Map<String, Object> queryAll(LimiterRuleQueryCriteria criteria, Pageable pageable) {
        return PageUtil.toPage(limiterRuleRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable));
    }

    @Override
    public LimiterRule findByUri(String uri) {
        return limiterRuleRepository.findByUri(uri).orElse(null);
    }

    @Override
    public void create(LimiterRule limiterRule) {
        limiterRuleRepository.save(limiterRule);
    }

    @Override
    public void update(LimiterRule limiterRule) {
        LimiterRule existingRule = limiterRuleRepository.findById(limiterRule.getId()).orElseThrow(() -> new RuntimeException("限流规则不存在"));
        existingRule.setUri(limiterRule.getUri());
        existingRule.setMaxRequests(limiterRule.getMaxRequests());
        existingRule.setTimeWindow(limiterRule.getTimeWindow());
        existingRule.setEnabled(limiterRule.getEnabled());
        limiterRuleRepository.save(existingRule);
    }

    @Override
    public void delete(Long id) {
        limiterRuleRepository.deleteById(id);
    }
}
