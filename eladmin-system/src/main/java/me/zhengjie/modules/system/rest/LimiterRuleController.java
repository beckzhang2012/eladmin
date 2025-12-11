package me.zhengjie.modules.system.rest;

import me.zhengjie.modules.system.domain.LimiterRule;
import me.zhengjie.modules.system.service.LimiterRuleService;
import me.zhengjie.modules.system.service.dto.LimiterRuleQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Controller
 */
@RestController
@RequestMapping("/api/limiter/rule")
public class LimiterRuleController {

    private final LimiterRuleService limiterRuleService;

    public LimiterRuleController(LimiterRuleService limiterRuleService) {
        this.limiterRuleService = limiterRuleService;
    }

    @GetMapping
    public ResponseEntity<Object> queryAll(LimiterRuleQueryCriteria criteria, Pageable pageable) {
        return ResponseEntity.ok(limiterRuleService.queryAll(criteria, pageable));
    }

    @GetMapping("/{uri}")
    public ResponseEntity<Object> findByUri(@PathVariable String uri) {
        return ResponseEntity.ok(limiterRuleService.findByUri(uri));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody LimiterRule limiterRule) {
        limiterRuleService.create(limiterRule);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody LimiterRule limiterRule) {
        limiterRuleService.update(limiterRule);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        limiterRuleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
