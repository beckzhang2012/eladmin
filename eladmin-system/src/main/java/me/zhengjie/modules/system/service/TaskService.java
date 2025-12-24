package me.zhengjie.modules.system.service;

import me.zhengjie.base.PageResult;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.service.dto.TaskDto;
import me.zhengjie.modules.system.service.dto.TaskQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface TaskService {

    PageResult<TaskDto> queryAll(TaskQueryCriteria criteria, Pageable pageable);

    List<TaskDto> queryAll(TaskQueryCriteria criteria);

    TaskDto findById(Long id);

    Task create(Task task);

    void update(Task task);

    void delete(Long id);

    void deleteAll(Long[] ids);

    void download(List<TaskDto> taskDtos, HttpServletResponse response) throws IOException;

    void updateTaskStatus(Long taskId, String status);

    void updateTaskProgress(Long taskId, Long total, Long success, Long error);

    void updateTaskResult(Long taskId, String filePath, String fileName, Long fileSize);

    void updateTaskError(Long taskId, String errorMessage);
}
