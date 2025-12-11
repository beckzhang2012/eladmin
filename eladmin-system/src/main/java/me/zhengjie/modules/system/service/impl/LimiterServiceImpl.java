package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Limiter;
import me.zhengjie.modules.system.repository.LimiterRepository;
import me.zhengjie.modules.system.service.LimiterService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Service实现类
 */
@Service
@RequiredArgsConstructor
public class LimiterServiceImpl implements LimiterService {

    private final LimiterRepository limiterRepository;

    @Override
    public Page<Limiter> queryAll(Limiter limiter, Pageable pageable) {
        // 可以根据需要添加查询条件
        return limiterRepository.findAll(pageable);
    }

    @Override
    public List<Limiter> queryAll(Limiter limiter) {
        // 可以根据需要添加查询条件
        return limiterRepository.findAll();
    }

    @Override
    public Limiter findById(Long id) {
        return limiterRepository.findById(id).orElse(null);
    }

    @Override
    public Limiter findByLimiterKey(String limiterKey) {
        return limiterRepository.findByLimiterKey(limiterKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Limiter limiter) {
        limiter.setCreateTime(new Timestamp(System.currentTimeMillis()));
        limiter.setIsEnabled(true);
        limiterRepository.save(limiter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Limiter limiter) {
        limiter.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        limiterRepository.save(limiter);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        limiterRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enable(Long id) {
        Limiter limiter = limiterRepository.findById(id).orElse(null);
        if (limiter != null) {
            limiter.setIsEnabled(!limiter.getIsEnabled());
            limiterRepository.save(limiter);
        }
    }
}
