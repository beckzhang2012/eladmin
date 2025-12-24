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
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DataImportService {

    private final UserRepository userRepository;
    private final TaskLogService taskLogService;

    @Transactional(rollbackFor = Exception.class)
    public void importUserData(Task task, String filePath) {
        try {
            // 创建文件输入流
            FileInputStream inputStream = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            
            // 记录日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("INFO");
            log.setLogMessage("开始导入用户数据");
            taskLogService.create(log);
            
            // 读取数据
            List<User> users = new ArrayList<>();
            int rowCount = sheet.getPhysicalNumberOfRows();
            
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                
                User user = new User();
                user.setUsername(row.getCell(1).getStringCellValue());
                user.setNickName(row.getCell(2).getStringCellValue());
                user.setEmail(row.getCell(3).getStringCellValue());
                user.setPhone(row.getCell(4).getStringCellValue());
                user.setEnabled("启用".equals(row.getCell(5).getStringCellValue()));
                
                // 数据校验
                if (user.getUsername() == null || user.getUsername().isEmpty()) {
                    throw new RuntimeException("第" + i + "行用户名不能为空");
                }
                if (user.getEmail() == null || user.getEmail().isEmpty()) {
                    throw new RuntimeException("第" + i + "行邮箱不能为空");
                }
                
                users.add(user);
            }
            
            // 批量导入
            userRepository.saveAll(users);
            
            // 记录日志
            log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("INFO");
            log.setLogMessage("成功导入" + users.size() + "条用户数据");
            taskLogService.create(log);
            
            workbook.close();
            inputStream.close();
        } catch (Exception e) {
            // 记录错误日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("ERROR");
            log.setLogMessage("导入用户数据失败: " + e.getMessage());
            taskLogService.create(log);
            throw new RuntimeException("导入用户数据失败", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void importCsvData(Task task, String filePath) {
        try {
            // 创建文件输入流
            FileReader reader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(reader);
            
            // 记录日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("INFO");
            log.setLogMessage("开始导入CSV用户数据");
            taskLogService.create(log);
            
            // 读取数据
            List<User> users = new ArrayList<>();
            String line;
            int lineNumber = 0;
            
            while ((line = bufferedReader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) continue; // 跳过表头
                
                String[] fields = line.split(",");
                if (fields.length < 6) {
                    throw new RuntimeException("第" + lineNumber + "行数据格式不正确");
                }
                
                User user = new User();
                user.setUsername(fields[1]);
                user.setNickName(fields[2]);
                user.setEmail(fields[3]);
                user.setPhone(fields[4]);
                user.setEnabled("启用".equals(fields[5]));
                
                // 数据校验
                if (user.getUsername() == null || user.getUsername().isEmpty()) {
                    throw new RuntimeException("第" + lineNumber + "行用户名不能为空");
                }
                if (user.getEmail() == null || user.getEmail().isEmpty()) {
                    throw new RuntimeException("第" + lineNumber + "行邮箱不能为空");
                }
                
                users.add(user);
            }
            
            // 批量导入
            userRepository.saveAll(users);
            
            // 记录日志
            log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("INFO");
            log.setLogMessage("成功导入" + users.size() + "条CSV用户数据");
            taskLogService.create(log);
            
            bufferedReader.close();
            reader.close();
        } catch (Exception e) {
            // 记录错误日志
            TaskLog log = new TaskLog();
            log.setTaskId(task.getId());
            log.setLogType("ERROR");
            log.setLogMessage("导入CSV用户数据失败: " + e.getMessage());
            taskLogService.create(log);
            throw new RuntimeException("导入CSV用户数据失败", e);
        }
    }
}
