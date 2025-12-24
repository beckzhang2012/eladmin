package me.zhengjie.modules.system.service;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.domain.TaskLog;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.repository.UserRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DataExportService {

    private final UserRepository userRepository;
    private final TaskLogService taskLogService;

    public String exportUserData(Task task) {
        try {
            // 获取用户数据
            List<User> users = userRepository.findAll();
            
            // 创建Excel文件
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("用户数据");
            
            // 创建表头
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("用户名");
            headerRow.createCell(2).setCellValue("昵称");
            headerRow.createCell(3).setCellValue("邮箱");
            headerRow.createCell(4).setCellValue("手机号");
            headerRow.createCell(5).setCellValue("状态");
            headerRow.createCell(6).setCellValue("创建时间");
            
            // 填充数据
            int rowNum = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getNickName());
                row.createCell(3).setCellValue(user.getEmail());
                row.createCell(4).setCellValue(user.getPhone());
                row.createCell(5).setCellValue(user.getEnabled() ? "启用" : "禁用");
                row.createCell(6).setCellValue(user.getCreatedAt());
            }
            
            // 调整列宽
            for (int i = 0; i < 7; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 创建临时文件
            String fileName = "用户数据_" + System.currentTimeMillis() + ".xlsx";
            String filePath = "D:\\tmp\\" + fileName;
            File file = new File(filePath);
            FileOutputStream outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            outputStream.close();
            workbook.close();
            
            // 记录日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("INFO");
            log.setLogMessage("成功导出" + users.size() + "条用户数据");
            taskLogService.create(log);
            
            return filePath;
        } catch (Exception e) {
            // 记录错误日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("ERROR");
            log.setLogMessage("导出用户数据失败: " + e.getMessage());
            taskLogService.create(log);
            throw new RuntimeException("导出用户数据失败", e);
        }
    }

    public String exportCsvData(Task task) {
        try {
            // 获取用户数据
            List<User> users = userRepository.findAll();
            
            // 创建CSV文件
            String fileName = "用户数据_" + System.currentTimeMillis() + ".csv";
            String filePath = "D:\\tmp\\" + fileName;
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);
            
            // 写入表头
            writer.write("ID,用户名,昵称,邮箱,手机号,状态,创建时间\n");
            
            // 写入数据
            for (User user : users) {
                writer.write(user.getId() + ","
                        + user.getUsername() + ","
                        + user.getNickName() + ","
                        + user.getEmail() + ","
                        + user.getPhone() + ","
                        + (user.getEnabled() ? "启用" : "禁用") + ","
                        + user.getCreatedAt() + "\n");
            }
            
            writer.close();
            
            // 记录日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("INFO");
            log.setLogMessage("成功导出" + users.size() + "条用户数据到CSV文件");
            taskLogService.create(log);
            
            return filePath;
        } catch (Exception e) {
            // 记录错误日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("ERROR");
            log.setLogMessage("导出CSV文件失败: " + e.getMessage());
            taskLogService.create(log);
            throw new RuntimeException("导出CSV文件失败", e);
        }
    }
}
