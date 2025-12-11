package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.service.UserLockService;
import me.zhengjie.modules.system.service.dto.UserLockDto;
import me.zhengjie.modules.system.service.dto.UserLockQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author admin
 * @date 2025-06-27
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userLock")
@Api(tags = "系统：用户锁定管理")
public class UserLockController {

    private final UserLockService userLockService;

    @ApiOperation("查询锁定用户列表")
    @GetMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> query(UserLockQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(userLockService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询所有锁定用户")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check()")
    public ResponseEntity<List<UserLockDto>> queryAll(UserLockQueryCriteria criteria) {
        return new ResponseEntity<>(userLockService.queryAll(criteria), HttpStatus.OK);
    }

    @ApiOperation("手动解锁用户")
    @PutMapping(value = "/unlock/{userId}")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> unlock(@PathVariable Long userId) {
        userLockService.unlock(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("批量解锁用户")
    @PutMapping(value = "/unlock/batch")
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> unlockBatch(@RequestBody List<Long> userIds) {
        userLockService.unlockBatch(userIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
