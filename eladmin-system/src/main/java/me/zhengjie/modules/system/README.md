# 任务管理系统

## 概述
任务管理系统是一个用于管理异步数据导出/导入任务的系统，它允许用户创建导出/导入任务，查询任务状态，重试失败的任务，取消正在进行的任务，以及删除任务。

## 功能特性

### 导出任务
- 支持按条件导出 users/roles/menus 等数据到 CSV 或 Excel 格式
- 使用流式导出，支持大数据量
- 后台异步处理，不阻塞用户操作
- 记录任务执行状态和结果

### 导入任务
- 支持从 CSV 或 Excel 文件导入数据
- 提供数据校验功能，确保数据的有效性
- 支持错误回滚，确保数据的一致性
- 支持批量导入和断点续传

### 任务管理
- 提供任务状态查询接口
- 支持管理员查看所有任务
- 支持重试失败的任务
- 支持取消正在进行的任务
- 支持删除任务

## 技术实现

### 异步任务处理
- 使用 Spring Boot 的 @Async 注解实现异步任务处理
- 配置了自定义的线程池，支持任务并发执行

### 流式数据导出
- 使用 Hutool 的 BigExcelWriter 实现流式 Excel 导出
- 支持大数据量导出，避免内存溢出

### 数据存储
- 使用 JPA 进行数据库操作
- 任务信息存储在 sys_task 表中

### 权限控制
- 集成了 Spring Security 进行权限控制
- 提供了任务管理的权限点：task:list, task:create, task:edit, task:delete

## API 接口

### 查询任务
```
GET /api/tasks
```
查询所有任务，支持分页和条件查询。

### 根据ID查询任务
```
GET /api/tasks/{id}
```
根据任务ID查询任务信息。

### 创建导出任务
```
POST /api/tasks/export
```
创建一个导出任务，参数包括：
- entityType：导出的数据类型（users/roles/menus等）
- params：查询条件参数

### 创建导入任务
```
POST /api/tasks/import
```
创建一个导入任务，参数包括：
- entityType：导入的数据类型（users/roles/menus等）
- fileId：文件ID
- params：导入参数

### 重试任务
```
PUT /api/tasks/{id}/retry
```
重试一个失败的任务。

### 取消任务
```
PUT /api/tasks/{id}/cancel
```
取消一个正在进行的任务。

### 删除任务
```
DELETE /api/tasks/{id}
```
删除一个任务。

## 数据库表结构

### sys_task 表
| 字段名 | 类型 | 描述 |
| ------ | ---- | ---- |
| id | bigint(20) | 任务ID |
| name | varchar(255) | 任务名称 |
| type | varchar(255) | 任务类型（导出/导入） |
| status | varchar(255) | 任务状态（等待中/处理中/成功/失败/已取消） |
| data_type | varchar(255) | 数据类型（users/roles/menus等） |
| file_path | varchar(255) | 文件路径 |
| file_format | varchar(255) | 文件格式（CSV/Excel） |
| record_count | bigint(20) | 处理记录数 |
| error_msg | text | 错误信息 |
| start_time | datetime | 开始时间 |
| end_time | datetime | 结束时间 |
| creator_id | bigint(20) | 创建者ID |
| creator_name | varchar(255) | 创建者名称 |
| create_by | varchar(255) | 创建者 |
| update_by | varchar(255) | 更新者 |
| create_time | datetime | 创建日期 |
| update_time | datetime | 更新时间 |

## 使用示例

### 创建导出任务
```java
// 创建导出任务
Long taskId = taskService.createExportTask("users", Map.of("status", "enabled"));
```

### 查询任务
```java
// 查询任务
TaskDto task = taskService.findById(taskId);
```

### 重试任务
```java
// 重试任务
taskService.retryTask(taskId);
```

### 取消任务
```java
// 取消任务
taskService.cancelTask(taskId);
```

### 删除任务
```java
// 删除任务
taskService.delete(taskId);
```

## 注意事项

1. 确保项目中已配置了异步任务执行器
2. 确保数据库中已创建了 sys_task 表
3. 确保用户具有相应的权限来执行任务管理操作
4. 导出任务的文件存储路径需要根据实际情况进行配置
5. 导入任务的文件需要先上传到系统中，获取文件ID后才能创建导入任务
