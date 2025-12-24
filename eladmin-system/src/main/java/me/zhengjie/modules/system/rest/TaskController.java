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
import lombok.RequiredArgsConstructor;
import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.service.TaskService;
import me.zhengjie.modules.system.service.dto.TaskDto;
import me.zhengjie.modules.system.service.dto.TaskQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 任务控制器
 * @author Zheng Jie
 * @date 2025-12-24
 */
@RestController
@RequiredArgsConstructor
@Api(tags = "系统：任务管理")
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private static final String ENTITY_NAME = "task";

    @ApiOperation("查询任务")
    @GetMapping
    @PreAuthorize("@el.check('task:list')")
    public ResponseEntity<PageResult<TaskDto>> queryTask(TaskQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(taskService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("根据ID查询任务")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('task:list')")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("创建导出任务")
    @PostMapping("/export")
    @PreAuthorize("@el.check('task:create')")
    public ResponseEntity<Long> createExportTask(@RequestParam String entityType, @RequestBody Map<String, Object> params) {
        return new ResponseEntity<>(taskService.createExportTask(entityType, params), HttpStatus.CREATED);
    }

    @ApiOperation("创建导入任务")
    @PostMapping("/import")
    @PreAuthorize("@el.check('task:create')")
    public ResponseEntity<Long> createImportTask(@RequestParam String entityType, @RequestParam Long fileId, @RequestBody Map<String, Object> params) {
        return new ResponseEntity<>(taskService.createImportTask(entityType, fileId, params), HttpStatus.CREATED);
    }

    @ApiOperation("重试任务")
    @PutMapping("/{id}/retry")
    @PreAuthorize("@el.check('task:edit')")
    public ResponseEntity<Void> retryTask(@PathVariable Long id) {
        taskService.retryTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("取消任务")
    @PutMapping("/{id}/cancel")
    @PreAuthorize("@el.check('task:edit')")
    public ResponseEntity<Void> cancelTask(@PathVariable Long id) {
        taskService.cancelTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除任务")
    @DeleteMapping("/{id}")
    @PreAuthorize("@el.check('task:delete')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}