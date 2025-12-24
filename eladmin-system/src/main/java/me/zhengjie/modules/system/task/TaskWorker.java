package me.zhengjie.modules.system.task;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.domain.TaskLog;
import me.zhengjie.modules.system.service.DataExportService;
import me.zhengjie.modules.system.service.DataImportService;
import me.zhengjie.modules.system.service.TaskLogService;
import me.zhengjie.modules.system.service.TaskService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TaskWorker {

    private final TaskService taskService;
    private final TaskLogService taskLogService;
    private final DataExportService dataExportService;
    private final DataImportService dataImportService;

    @Async("taskExecutor")
    public void processTask(Long taskId) {
        try {
            // 更新任务状态为处理中
            taskService.updateTaskStatus(taskId, "PROCESSING");
            
            // 记录任务开始日志
            TaskLog log = new TaskLog();
            log.setTaskId(taskId);
            log.setLogType("INFO");
            log.setLogMessage("任务开始处理");
            taskLogService.create(log);
            
            // 获取任务信息
            Task task = taskService.findById(taskId).get();
            
            // 根据任务类型处理不同的任务
            switch (task.getTaskType()) {
                case "EXPORT":
                    processExportTask(task);
                    break;
                case "IMPORT":
                    processImportTask(task);
                    break;
                default:
                    throw new IllegalArgumentException("未知的任务类型: " + task.getTaskType());
            }
            
            // 更新任务状态为完成
            taskService.updateTaskStatus(taskId, "SUCCESS");
            
            // 记录任务完成日志
            log = new TaskLog();
            log.setTaskId(taskId);
            log.setLogType("INFO");
            log.setLogMessage("任务处理完成");
            taskLogService.create(log);
            
        } catch (Exception e) {
            // 更新任务状态为失败
            taskService.updateTaskStatus(taskId, "FAILED");
            
            // 记录任务失败日志
            TaskLog log = new TaskLog();
            log.setTaskId(taskId);
            log.setLogType("ERROR");
            log.setLogMessage("任务处理失败: " + e.getMessage());
            taskLogService.create(log);
            
            // 更新任务错误信息
            taskService.updateTaskError(taskId, e.getMessage());
        }
    }

    private void processExportTask(Task task) {
        // 实现数据导出逻辑
        String filePath = dataExportService.exportUserData(task);
        
        // 获取文件大小
        File file = new File(filePath);
        Long fileSize = file.length();
        
        // 更新任务结果
        taskService.updateTaskResult(task.getId(), filePath, file.getName(), fileSize);
    }

    private void processImportTask(Task task) {
        // 实现数据导入逻辑
        String filePath = task.getFilePath();
        dataImportService.importUserData(task, filePath);
    }
}
