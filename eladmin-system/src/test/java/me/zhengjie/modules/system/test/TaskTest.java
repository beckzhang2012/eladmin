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
package me.zhengjie.modules.system.test;

import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.repository.TaskRepository;
import me.zhengjie.modules.system.service.TaskService;
import me.zhengjie.utils.SecurityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * 任务管理系统测试类
 * @author Zheng Jie
 * @date 2025-12-24
 */
@SpringBootTest
public class TaskTest {

    @Resource
    private TaskService taskService;

    @Resource
    private TaskRepository taskRepository;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testCreateExportTask() {
        // 创建导出任务
        Long taskId = taskService.createExportTask("users", Map.of("status", "enabled"));
        System.out.println("创建的导出任务ID: " + taskId);

        // 查询任务
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            System.out.println("任务名称: " + task.getName());
            System.out.println("任务类型: " + task.getType());
            System.out.println("任务状态: " + task.getStatus());
            System.out.println("数据类型: " + task.getDataType());
            System.out.println("创建者: " + task.getCreatorName());
        }
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testCreateImportTask() {
        // 创建导入任务
        Long taskId = taskService.createImportTask("users", 1L, Map.of("overwrite", true));
        System.out.println("创建的导入任务ID: " + taskId);

        // 查询任务
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            System.out.println("任务名称: " + task.getName());
            System.out.println("任务类型: " + task.getType());
            System.out.println("任务状态: " + task.getStatus());
            System.out.println("数据类型: " + task.getDataType());
            System.out.println("创建者: " + task.getCreatorName());
        }
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testRetryTask() {
        // 创建一个失败的任务
        Task task = new Task();
        task.setName("测试任务");
        task.setType("导出");
        task.setStatus("失败");
        task.setDataType("users");
        task.setFileFormat("Excel");
        task.setCreatorId(1L);
        task.setCreatorName("admin");
        task.setStartTime(new Date());
        task.setEndTime(new Date());
        task.setErrorMsg("测试错误信息");
        task = taskRepository.save(task);

        // 重试任务
        taskService.retryTask(task.getId());

        // 查询任务
        Task retriedTask = taskRepository.findById(task.getId()).orElse(null);
        if (retriedTask != null) {
            System.out.println("重试后的任务状态: " + retriedTask.getStatus());
            System.out.println("重试后的错误信息: " + retriedTask.getErrorMsg());
        }
    }

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void testCancelTask() {
        // 创建一个任务
        Task task = new Task();
        task.setName("测试任务");
        task.setType("导出");
        task.setStatus("处理中");
        task.setDataType("users");
        task.setFileFormat("Excel");
        task.setCreatorId(1L);
        task.setCreatorName("admin");
        task.setStartTime(new Date());
        task = taskRepository.save(task);

        // 取消任务
        taskService.cancelTask(task.getId());

        // 查询任务
        Task canceledTask = taskRepository.findById(task.getId()).orElse(null);
        if (canceledTask != null) {
            System.out.println("取消后的任务状态: " + canceledTask.getStatus());
            System.out.println("取消后的结束时间: " + canceledTask.getEndTime());
        }
    }
}