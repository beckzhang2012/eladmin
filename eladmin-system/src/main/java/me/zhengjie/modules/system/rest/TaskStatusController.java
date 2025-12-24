package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.service.TaskService;
import me.zhengjie.modules.system.service.dto.TaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "系统：任务状态查询")
@RestController
@RequestMapping("/api/task-status")
@RequiredArgsConstructor
public class TaskStatusController {

    private final TaskService taskService;

    @ApiOperation("查询任务状态")
    @GetMapping(value = "/{taskId}")
    @PreAuthorize("@el.check('tasks:list')")
    public ResponseEntity<TaskDto> getTaskStatus(@PathVariable Long taskId) {
        TaskDto taskDto = taskService.findById(taskId);
        return new ResponseEntity<>(taskDto, HttpStatus.OK);
    }
}
