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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统：任务重试")
@RestController
@RequestMapping("/api/task-retry")
@RequiredArgsConstructor
public class TaskRetryController {

    private final TaskService taskService;
    private final TaskWorker taskWorker;

    @ApiOperation("重试失败任务")
    @PostMapping(value = "/{taskId}")
    @PreAuthorize("@el.check('tasks:edit')")
    public ResponseEntity<Object> retryTask(@PathVariable Long taskId) {
        // 获取任务信息
        Task task = taskService.findById(taskId).get();
        
        // 检查任务状态是否为失败
        if (!"FAILED".equals(task.getTaskStatus())) {
            return new ResponseEntity<>("只有失败的任务才能重试", HttpStatus.BAD_REQUEST);
        }
        
        // 更新任务状态为待处理
        task.setTaskStatus("PENDING");
        task.setErrorMessage(null);
        taskService.update(task);
        
        // 异步处理任务
        taskWorker.processTask(taskId);
        
        return new ResponseEntity<>("任务已重新启动", HttpStatus.OK);
    }
}
