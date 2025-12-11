package me.zhengjie.modules.system.rest;

import me.zhengjie.modules.system.service.LimiterLogService;
import me.zhengjie.modules.system.service.dto.LimiterLogQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流日志Controller
 */
@RestController
@RequestMapping("/api/limiter/log")
public class LimiterLogController {

    private final LimiterLogService limiterLogService;

    public LimiterLogController(LimiterLogService limiterLogService) {
        this.limiterLogService = limiterLogService;
    }

    @GetMapping
    public ResponseEntity<Object> queryAll(LimiterLogQueryCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(limiterLogService.queryAll(criteria, pageable));
    }
}
