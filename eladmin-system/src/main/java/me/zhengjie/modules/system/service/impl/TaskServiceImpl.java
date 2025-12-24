package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.base.PageResult;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.repository.TaskRepository;
import me.zhengjie.modules.system.service.TaskService;
import me.zhengjie.modules.system.service.dto.TaskDto;
import me.zhengjie.modules.system.service.dto.TaskQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.TaskMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.utils.SecurityUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public PageResult<TaskDto> queryAll(TaskQueryCriteria criteria, Pageable pageable) {
        Page<Task> page = taskRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, criteria, cb), pageable);
        return PageUtil.toPage(page.map(taskMapper::toDto));
    }

    @Override
    public List<TaskDto> queryAll(TaskQueryCriteria criteria) {
        return taskMapper.toDto(taskRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, criteria, cb)));
    }

    @Override
    public TaskDto findById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new BadRequestException("任务不存在"));
        return taskMapper.toDto(task);
    }

    @Override
    public Task create(Task task) {
        task.setCreatedBy(SecurityUtils.getUsername());
        task.setCreatedAt(new Date());
        task.setUpdatedBy(SecurityUtils.getUsername());
        task.setUpdatedAt(new Date());
        return taskRepository.save(task);
    }

    @Override
    public void update(Task task) {
        Task oldTask = taskRepository.findById(task.getId()).orElseThrow(() -> new BadRequestException("任务不存在"));
        task.setCreatedBy(oldTask.getCreatedBy());
        task.setCreatedAt(oldTask.getCreatedAt());
        task.setUpdatedBy(SecurityUtils.getUsername());
        task.setUpdatedAt(new Date());
        taskRepository.save(task);
    }

    @Override
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void deleteAll(Long[] ids) {
        Arrays.stream(ids).forEach(taskRepository::deleteById);
    }

    @Override
    public void download(List<TaskDto> taskDtos, HttpServletResponse response) throws IOException {
        // 实现下载功能
    }

    @Override
    public void updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadRequestException("任务不存在"));
        task.setTaskStatus(status);
        task.setUpdatedBy(SecurityUtils.getUsername());
        task.setUpdatedAt(new Date());
        taskRepository.save(task);
    }

    @Override
    public void updateTaskProgress(Long taskId, Long total, Long success, Long error) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadRequestException("任务不存在"));
        task.setTotalRecords(total);
        task.setSuccessRecords(success);
        task.setErrorRecords(error);
        task.setUpdatedBy(SecurityUtils.getUsername());
        task.setUpdatedAt(new Date());
        taskRepository.save(task);
    }

    @Override
    public void updateTaskResult(Long taskId, String filePath, String fileName, Long fileSize) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadRequestException("任务不存在"));
        task.setFilePath(filePath);
        task.setFileName(fileName);
        task.setFileSize(fileSize);
        task.setUpdatedBy(SecurityUtils.getUsername());
        task.setUpdatedAt(new Date());
        taskRepository.save(task);
    }

    @Override
    public void updateTaskError(Long taskId, String errorMessage) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new BadRequestException("任务不存在"));
        task.setErrorMessage(errorMessage);
        task.setUpdatedBy(SecurityUtils.getUsername());
        task.setUpdatedAt(new Date());
        taskRepository.save(task);
    }
}
