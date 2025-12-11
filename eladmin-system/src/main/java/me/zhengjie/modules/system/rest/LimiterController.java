package me.zhengjie.modules.system.rest;

import me.zhengjie.modules.system.domain.Limiter;
import me.zhengjie.modules.system.service.LimiterService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则Controller
 */
@RestController
@RequestMapping("/api/limiter")
public class LimiterController {

    private final LimiterService limiterService;

    public LimiterController(LimiterService limiterService) {
        this.limiterService = limiterService;
    }

    @GetMapping
    public ResponseEntity<Object> queryAll(Limiter limiter, Pageable pageable) {
        return ResponseEntity.ok(limiterService.queryAll(limiter, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable Long id) {
        return ResponseEntity.ok(limiterService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Limiter limiter) {
        limiterService.create(limiter);
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Limiter limiter) {
        limiterService.update(limiter);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        limiterService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/enable/{id}")
    public ResponseEntity<Object> enable(@PathVariable Long id) {
        limiterService.enable(id);
        return ResponseEntity.ok().build();
    }
}
