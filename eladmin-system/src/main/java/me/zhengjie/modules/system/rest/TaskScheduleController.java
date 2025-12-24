package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.service.TaskService;
import me.zhengjie.modules.system.task.TaskWorker;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统：任务调度")
@RestController
@RequestMapping("/api/task-schedule")
@RequiredArgsConstructor
public class TaskScheduleController {

    private final TaskService taskService;
    private final TaskWorker taskWorker;

    @ApiOperation("创建并触发导出任务")
    @PostMapping("/export")
    @PreAuthorize("@el.check('tasks:add')")
    public ResponseEntity<Object> createExportTask(@RequestBody Task task) {
        task.setTaskType("EXPORT");
        task.setTaskStatus("PENDING");
        Task savedTask = taskService.create(task);
        
        // 异步处理任务
        taskWorker.processTask(savedTask.getId());
        
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }

    @ApiOperation("创建并触发导入任务")
    @PostMapping("/import")
    @PreAuthorize("@el.check('tasks:add')")
    public ResponseEntity<Object> createImportTask(@RequestBody Task task) {
        task.setTaskType("IMPORT");
        task.setTaskStatus("PENDING");
        Task savedTask = taskService.create(task);
        
        // 异步处理任务
        taskWorker.processTask(savedTask.getId());
        
        return new ResponseEntity<>(savedTask, HttpStatus.CREATED);
    }
}
