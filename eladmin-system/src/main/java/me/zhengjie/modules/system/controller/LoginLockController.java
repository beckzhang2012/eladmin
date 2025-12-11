/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.system.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.service.LoginLockService;
import me.zhengjie.modules.system.service.dto.LoginLockDto;
import me.zhengjie.modules.system.service.dto.LoginLockQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2025-01-23
 */
@RestController
@RequestMapping("/api/loginLock")
@Api(tags = "系统：登录锁定管理")
public class LoginLockController {

    private final LoginLockService loginLockService;

    public LoginLockController(LoginLockService loginLockService) {
        this.loginLockService = loginLockService;
    }

    @GetMapping
    @ApiOperation("查询登录锁定记录")
    @PreAuthorize("@el.check('loginLock:list')")
    public ResponseEntity<PageResult<LoginLockDto>> query(LoginLockQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(loginLockService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    @ApiOperation("查询登录锁定记录列表")
    @PreAuthorize("@el.check('loginLock:list')")
    public ResponseEntity<List<LoginLockDto>> list(LoginLockQueryCriteria criteria) {
        return new ResponseEntity<>(loginLockService.queryAll(criteria), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    @ApiOperation("查询登录锁定记录")
    @PreAuthorize("@el.check('loginLock:list')")
    public ResponseEntity<LoginLockDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(loginLockService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ApiOperation("删除登录锁定记录")
    @PreAuthorize("@el.check('loginLock:delete')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        loginLockService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/unlock/{username}")
    @ApiOperation("解锁用户")
    @PreAuthorize("@el.check('loginLock:unlock')")
    public ResponseEntity<Object> unlock(@PathVariable String username) {
        loginLockService.unlock(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}