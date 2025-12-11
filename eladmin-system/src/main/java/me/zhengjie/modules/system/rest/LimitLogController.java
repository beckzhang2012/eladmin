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
package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.service.LimitLogService;
import me.zhengjie.service.dto.LimitLogQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author /
 */
@RestController
@Api(tags = "系统：限流日志管理")
@RequestMapping("/api/limitLog")
public class LimitLogController {

    @Resource
    private LimitLogService limitLogService;

    @ApiOperation("查询限流日志")
    @GetMapping
    public ResponseEntity<Object> query(LimitLogQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(limitLogService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("删除限流日志")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        limitLogService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
