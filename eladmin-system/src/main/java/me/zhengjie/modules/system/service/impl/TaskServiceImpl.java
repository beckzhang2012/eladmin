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
package me.zhengjie.modules.system.service.impl;

import me.zhengjie.utils.PageResult;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.repository.TaskRepository;
import me.zhengjie.modules.system.service.TaskService;
import me.zhengjie.modules.system.service.dto.TaskDto;
import me.zhengjie.modules.system.service.dto.TaskQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.TaskMapper;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.File;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.utils.StringUtils;

/**
 * 任务服务实现类
 * @author Zheng Jie
 * @date 2025-12-24
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Resource
    private TaskRepository taskRepository;

    @Resource
    private TaskMapper taskMapper;

    @Override
    public PageResult<TaskDto> queryAll(TaskQueryCriteria criteria, Pageable pageable) {
        Page<Task> page = taskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(taskMapper::toDto));
    }

    @Override
    public List<TaskDto> queryAll(TaskQueryCriteria criteria) {
        List<Task> tasks = taskRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return taskMapper.toDto(tasks);
    }

    @Override
    public TaskDto findById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("任务不存在"));
        return taskMapper.toDto(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createExportTask(String entityType, Map<String, Object> params) {
        Task task = new Task();
        task.setName("导出" + entityType);
        task.setType("导出");
        task.setStatus("等待中");
        task.setDataType(entityType);
        task.setFileFormat("Excel");
        task.setCreatorId(SecurityUtils.getCurrentUserId());
        task.setCreatorName(SecurityUtils.getCurrentUsername());
        task.setStartTime(new Timestamp(new Date().getTime()));
        task = taskRepository.save(task);
        executeTask(task);
        return task.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createImportTask(String entityType, Long fileId, Map<String, Object> params) {
        Task task = new Task();
        task.setName("导入" + entityType);
        task.setType("导入");
        task.setStatus("等待中");
        task.setDataType(entityType);
        task.setCreatorId(SecurityUtils.getCurrentUserId());
        task.setCreatorName(SecurityUtils.getCurrentUsername());
        task.setStartTime(new Timestamp(new Date().getTime()));
        task = taskRepository.save(task);
        executeTask(task);
        return task.getId();
    }

    @Override
    @Async
    public void executeTask(Task task) {
        try {
            task.setStatus("处理中");
            taskRepository.save(task);

            // 模拟任务处理
            Thread.sleep(5000);

            // 假设任务成功完成
            task.setStatus("成功");
            task.setRecordCount(100L);
            task.setEndTime(new Timestamp(new Date().getTime()));
            taskRepository.save(task);
        } catch (Exception e) {
            task.setStatus("失败");
            task.setErrorMsg(e.getMessage());
            task.setEndTime(new Timestamp(new Date().getTime()));
            taskRepository.save(task);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void retryTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("等待中");
        task.setErrorMsg(null);
        task.setStartTime(new Timestamp(new Date().getTime()));
        task.setEndTime(null);
        taskRepository.save(task);
        executeTask(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("任务不存在"));
        task.setStatus("已取消");
        task.setEndTime(new Timestamp(new Date().getTime()));
        taskRepository.save(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public void download(Long id, HttpServletResponse response) throws IOException {
        // 实现下载逻辑
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("任务不存在"));
        // 假设文件路径存储在task.getFilePath()中
        String filePath = task.getFilePath();
        if (StringUtils.isBlank(filePath)) {
            throw new RuntimeException("文件路径不存在");
        }
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("文件不存在");
        }
        // 下载文件
        FileUtil.downloadFile(response, file);
    }

    @Override
    public void download(List<TaskDto> tasks, HttpServletResponse response) throws IOException {
        // 实现批量下载逻辑
        // 这里可以将任务列表导出为Excel或CSV文件
        List<Map<String, Object>> list = new ArrayList<>();
        for (TaskDto task : tasks) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("type", task.getType());
            map.put("status", task.getStatus());
            map.put("dataType", task.getDataType());
            map.put("fileFormat", task.getFileFormat());
            map.put("recordCount", task.getRecordCount());
            map.put("startTime", task.getStartTime());
            map.put("endTime", task.getEndTime());
            map.put("creatorName", task.getCreatorName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}