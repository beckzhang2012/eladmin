package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.base.PageResult;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.domain.TaskLog;
import me.zhengjie.modules.system.service.TaskLogService;
import me.zhengjie.modules.system.service.TaskService;
import me.zhengjie.modules.system.service.dto.TaskDto;
import me.zhengjie.modules.system.service.dto.TaskLogDto;
import me.zhengjie.modules.system.service.dto.TaskQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "系统：任务管理")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskLogService taskLogService;

    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('tasks:list')")
    public void download(HttpServletResponse response, TaskQueryCriteria criteria) throws IOException {
        taskService.download(taskService.queryAll(criteria), response);
    }

    @ApiOperation("查询任务列表")
    @GetMapping
    @PreAuthorize("@el.check('tasks:list')")
    public ResponseEntity<PageResult<TaskDto>> query(TaskQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(taskService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询所有任务")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('tasks:list')")
    public ResponseEntity<List<TaskDto>> queryAll(TaskQueryCriteria criteria) {
        return new ResponseEntity<>(taskService.queryAll(criteria), HttpStatus.OK);
    }

    @ApiOperation("查询单个任务")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('tasks:list')")
    public ResponseEntity<TaskDto> findById(@PathVariable Long id) {
        return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
    }

    @ApiOperation("创建任务")
    @PostMapping
    @PreAuthorize("@el.check('tasks:add')")
    public ResponseEntity<Object> create(@RequestBody Task task) {
        taskService.create(task);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改任务")
    @PutMapping
    @PreAuthorize("@el.check('tasks:edit')")
    public ResponseEntity<Object> update(@RequestBody Task task) {
        taskService.update(task);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除任务")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('tasks:del')")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("批量删除任务")
    @DeleteMapping
    @PreAuthorize("@el.check('tasks:del')")
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        taskService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("查询任务日志")
    @GetMapping(value = "/{id}/logs")
    @PreAuthorize("@el.check('tasks:list')")
    public ResponseEntity<PageResult<TaskLogDto>> queryLogs(@PathVariable Long id, Pageable pageable) {
        return new ResponseEntity<>(taskLogService.queryAll(id, pageable), HttpStatus.OK);
    }

    @ApiOperation("查询所有任务日志")
    @GetMapping(value = "/{id}/logs/all")
    @PreAuthorize("@el.check('tasks:list')")
    public ResponseEntity<List<TaskLogDto>> queryAllLogs(@PathVariable Long id) {
        return new ResponseEntity<>(taskLogService.queryAll(id), HttpStatus.OK);
    }

    @ApiOperation("创建任务日志")
    @PostMapping(value = "/{id}/logs")
    @PreAuthorize("@el.check('tasks:add')")
    public ResponseEntity<Object> createLog(@PathVariable Long id, @RequestBody TaskLog taskLog) {
        taskLog.setTaskId(id);
        taskLogService.create(taskLog);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("删除任务日志")
    @DeleteMapping(value = "/logs/{id}")
    @PreAuthorize("@el.check('tasks:del')")
    public ResponseEntity<Object> deleteLog(@PathVariable Long id) {
        taskLogService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
