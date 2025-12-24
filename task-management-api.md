# 任务管理API文档

## 1. 任务管理接口

### 1.1 查询任务列表
- **接口地址**: GET /api/tasks
- **请求参数**: 
  - `page` (int): 页码，默认1
  - `size` (int): 每页大小，默认10
  - `sort` (string): 排序字段，默认id,desc
  - `taskName` (string): 任务名称（可选）
  - `taskType` (string): 任务类型（可选）
  - `taskStatus` (string): 任务状态（可选）
- **返回结果**: 分页的任务列表

### 1.2 查询单个任务
- **接口地址**: GET /api/tasks/{id}
- **请求参数**: 
  - `id` (long): 任务ID
- **返回结果**: 任务详情

### 1.3 创建任务
- **接口地址**: POST /api/tasks
- **请求参数**: 
  - `taskName` (string): 任务名称
  - `taskType` (string): 任务类型
  - `taskParams` (string): 任务参数（可选）
- **返回结果**: 创建的任务详情

### 1.4 修改任务
- **接口地址**: PUT /api/tasks
- **请求参数**: 
  - `id` (long): 任务ID
  - `taskName` (string): 任务名称（可选）
  - `taskType` (string): 任务类型（可选）
  - `taskParams` (string): 任务参数（可选）
- **返回结果**: 修改后的任务详情

### 1.5 删除任务
- **接口地址**: DELETE /api/tasks/{id}
- **请求参数**: 
  - `id` (long): 任务ID
- **返回结果**: 成功信息

### 1.6 批量删除任务
- **接口地址**: DELETE /api/tasks
- **请求参数**: 
  - `ids` (array): 任务ID数组
- **返回结果**: 成功信息

## 2. 任务调度接口

### 2.1 创建并触发导出任务
- **接口地址**: POST /api/task-schedule/export
- **请求参数**: 
  - `taskName` (string): 任务名称
  - `taskParams` (string): 任务参数（可选）
- **返回结果**: 创建的任务详情

### 2.2 创建并触发导入任务
- **接口地址**: POST /api/task-schedule/import
- **请求参数**: 
  - `taskName` (string): 任务名称
  - `filePath` (string): 文件路径
- **返回结果**: 创建的任务详情

## 3. 任务状态查询接口

### 3.1 查询任务状态
- **接口地址**: GET /api/task-status/{taskId}
- **请求参数**: 
  - `taskId` (long): 任务ID
- **返回结果**: 任务状态详情

## 4. 任务重试接口

### 4.1 重试失败任务
- **接口地址**: POST /api/task-retry/{taskId}
- **请求参数**: 
  - `taskId` (long): 任务ID
- **返回结果**: 成功信息

## 5. 文件下载接口

### 5.1 下载文件
- **接口地址**: GET /api/files/download/{fileName}
- **请求参数**: 
  - `fileName` (string): 文件名
- **返回结果**: 文件流

## 6. 文件上传接口

### 6.1 上传文件
- **接口地址**: POST /api/files/upload
- **请求参数**: 
  - `file` (multipart): 上传的文件
- **返回结果**: 文件路径

## 7. 任务日志接口

### 7.1 查询任务日志
- **接口地址**: GET /api/tasks/{id}/logs
- **请求参数**: 
  - `id` (long): 任务ID
  - `page` (int): 页码，默认1
  - `size` (int): 每页大小，默认10
- **返回结果**: 分页的任务日志列表

### 7.2 查询所有任务日志
- **接口地址**: GET /api/tasks/{id}/logs/all
- **请求参数**: 
  - `id` (long): 任务ID
- **返回结果**: 所有任务日志列表

### 7.3 创建任务日志
- **接口地址**: POST /api/tasks/{id}/logs
- **请求参数**: 
  - `id` (long): 任务ID
  - `logType` (string): 日志类型
  - `logMessage` (string): 日志内容
- **返回结果**: 创建的任务日志详情

### 7.4 删除任务日志
- **接口地址**: DELETE /api/tasks/logs/{id}
- **请求参数**: 
  - `id` (long): 任务日志ID
- **返回结果**: 成功信息

## 8. 任务类型说明

- `EXPORT_USER_EXCEL`: 导出用户数据到Excel
- `EXPORT_USER_CSV`: 导出用户数据到CSV
- `IMPORT_USER_EXCEL`: 从Excel导入用户数据
- `IMPORT_USER_CSV`: 从CSV导入用户数据

## 9. 任务状态说明

- `PENDING`: 待处理
- `PROCESSING`: 处理中
- `SUCCESS`: 成功
- `FAILED`: 失败