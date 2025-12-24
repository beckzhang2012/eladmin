# 任务管理API文档

## 1. 任务管理API

### 1.1 查询任务列表
**接口地址**：`GET /api/tasks`
**请求参数**：
- `taskName`：任务名称（模糊查询）
- `taskType`：任务类型（EXPORT/IMPORT）
- `taskStatus`：任务状态（PENDING/PROCESSING/SUCCESS/FAILED）
- `createTime`：创建时间范围（数组，格式：["开始时间", "结束时间"]）
- `startTime`：开始时间范围（数组，格式：["开始时间", "结束时间"]）
- `endTime`：结束时间范围（数组，格式：["开始时间", "结束时间"]）
- `createdBy`：创建人

**响应结果**：
```json
{
  "data": [
    {
      "id": 1,
      "taskName": "导出用户数据",
      "taskType": "EXPORT",
      "taskStatus": "SUCCESS",
      "taskParams": "{\"dataType\": \"user\"}",
      "filePath": "D:\\tmp\\用户数据_1234567890.xlsx",
      "fileName": "用户数据_1234567890.xlsx",
      "fileSize": 102400,
      "totalRecords": 1000,
      "successRecords": 1000,
      "errorRecords": 0,
      "errorMessage": null,
      "startTime": "2025-12-24 10:00:00",
      "endTime": "2025-12-24 10:00:10",
      "createdBy": "admin",
      "createdAt": "2025-12-24 10:00:00",
      "updatedBy": "admin",
      "updatedAt": "2025-12-24 10:00:10"
    }
  ],
  "total": 1,
  "code": 0,
  "msg": "success"
}
```

### 1.2 查询单个任务
**接口地址**：`GET /api/tasks/{id}`
**请求参数**：
- `id`：任务ID

**响应结果**：
```json
{
  "id": 1,
  "taskName": "导出用户数据",
  "taskType": "EXPORT",
  "taskStatus": "SUCCESS",
  "taskParams": "{\"dataType\": \"user\"}",
  "filePath": "D:\\tmp\\用户数据_1234567890.xlsx",
  "fileName": "用户数据_1234567890.xlsx",
  "fileSize": 102400,
  "totalRecords": 1000,
  "successRecords": 1000,
  "errorRecords": 0,
  "errorMessage": null,
  "startTime": "2025-12-24 10:00:00",
  "endTime": "2025-12-24 10:00:10",
  "createdBy": "admin",
  "createdAt": "2025-12-24 10:00:00",
  "updatedBy": "admin",
  "updatedAt": "2025-12-24 10:00:10"
}
```

### 1.3 创建任务
**接口地址**：`POST /api/tasks`
**请求参数**：
```json
{
  "taskName": "导出用户数据",
  "taskType": "EXPORT",
  "taskStatus": "PENDING",
  "taskParams": "{\"dataType\": \"user\"}"
}
```

**响应结果**：
```json
{
  "id": 1,
  "taskName": "导出用户数据",
  "taskType": "EXPORT",
  "taskStatus": "PENDING",
  "taskParams": "{\"dataType\": \"user\"}",
  "filePath": null,
  "fileName": null,
  "fileSize": null,
  "totalRecords": null,
  "successRecords": null,
  "errorRecords": null,
  "errorMessage": null,
  "startTime": null,
  "endTime": null,
  "createdBy": "admin",
  "createdAt": "2025-12-24 10:00:00",
  "updatedBy": "admin",
  "updatedAt": "2025-12-24 10:00:00"
}
```

### 1.4 修改任务
**接口地址**：`PUT /api/tasks`
**请求参数**：
```json
{
  "id": 1,
  "taskName": "导出用户数据",
  "taskType": "EXPORT",
  "taskStatus": "PENDING",
  "taskParams": "{\"dataType\": \"user\"}"
}
```

**响应结果**：无

### 1.5 删除任务
**接口地址**：`DELETE /api/tasks/{id}`
**请求参数**：
- `id`：任务ID

**响应结果**：无

### 1.6 批量删除任务
**接口地址**：`DELETE /api/tasks`
**请求参数**：
```json
[1, 2, 3]
```

**响应结果**：无

### 1.7 查询任务日志
**接口地址**：`GET /api/tasks/{id}/logs`
**请求参数**：
- `id`：任务ID

**响应结果**：
```json
{
  "data": [
    {
      "id": 1,
      "taskId": 1,
      "logType": "INFO",
      "logMessage": "任务开始处理",
      "logTime": "2025-12-24 10:00:00",
      "createdBy": "admin",
      "createdAt": "2025-12-24 10:00:00"
    },
    {
      "id": 2,
      "taskId": 1,
      "logType": "INFO",
      "logMessage": "成功导出1000条用户数据",
      "logTime": "2025-12-24 10:00:10",
      "createdBy": "admin",
      "createdAt": "2025-12-24 10:00:10"
    }
  ],
  "total": 2,
  "code": 0,
  "msg": "success"
}
```

## 2. 任务调度API

### 2.1 创建并触发导出任务
**接口地址**：`POST /api/task-schedule/export`
**请求参数**：
```json
{
  "taskName": "导出用户数据",
  "taskParams": "{\"dataType\": \"user\"}"
}
```

**响应结果**：
```json
{
  "id": 1,
  "taskName": "导出用户数据",
  "taskType": "EXPORT",
  "taskStatus": "PENDING",
  "taskParams": "{\"dataType\": \"user\"}",
  "filePath": null,
  "fileName": null,
  "fileSize": null,
  "totalRecords": null,
  "successRecords": null,
  "errorRecords": null,
  "errorMessage": null,
  "startTime": null,
  "endTime": null,
  "createdBy": "admin",
  "createdAt": "2025-12-24 10:00:00",
  "updatedBy": "admin",
  "updatedAt": "2025-12-24 10:00:00"
}
```

### 2.2 创建并触发导入任务
**接口地址**：`POST /api/task-schedule/import`
**请求参数**：
```json
{
  "taskName": "导入用户数据",
  "filePath": "D:\\tmp\\用户数据.xlsx"
}
```

**响应结果**：
```json
{
  "id": 2,
  "taskName": "导入用户数据",
  "taskType": "IMPORT",
  "taskStatus": "PENDING",
  "filePath": "D:\\tmp\\用户数据.xlsx",
  "fileName": "用户数据.xlsx",
  "fileSize": 102400,
  "totalRecords": null,
  "successRecords": null,
  "errorRecords": null,
  "errorMessage": null,
  "startTime": null,
  "endTime": null,
  "createdBy": "admin",
  "createdAt": "2025-12-24 10:00:00",
  "updatedBy": "admin",
  "updatedAt": "2025-12-24 10:00:00"
}
```

## 3. 文件下载API

### 3.1 下载文件
**接口地址**：`GET /api/files/download/{fileName}`
**请求参数**：
- `fileName`：文件名

**响应结果**：文件流

## 4. 文件上传API

### 4.1 上传文件
**接口地址**：`POST /api/files/upload`
**请求参数**：
- `file`：文件（MultipartFile）

**响应结果**：
```json
"D:\\tmp\\用户数据.xlsx"
```

## 5. 任务状态查询API

### 5.1 查询任务状态
**接口地址**：`GET /api/task-status/{taskId}`
**请求参数**：
- `taskId`：任务ID

**响应结果**：
```json
{
  "id": 1,
  "taskName": "导出用户数据",
  "taskType": "EXPORT",
  "taskStatus": "SUCCESS",
  "taskParams": "{\"dataType\": \"user\"}",
  "filePath": "D:\\tmp\\用户数据_1234567890.xlsx",
  "fileName": "用户数据_1234567890.xlsx",
  "fileSize": 102400,
  "totalRecords": 1000,
  "successRecords": 1000,
  "errorRecords": 0,
  "errorMessage": null,
  "startTime": "2025-12-24 10:00:00",
  "endTime": "2025-12-24 10:00:10",
  "createdBy": "admin",
  "createdAt": "2025-12-24 10:00:00",
  "updatedBy": "admin",
  "updatedAt": "2025-12-24 10:00:10"
}
```

## 6. 任务重试API

### 6.1 重试失败任务
**接口地址**：`POST /api/task-retry/{taskId}`
**请求参数**：
- `taskId`：任务ID

**响应结果**：
```json
"任务已重新启动"
```
