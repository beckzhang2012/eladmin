# 登录失败锁定功能说明

## 功能概述

该功能实现了用户登录失败次数过多时自动锁定账号的机制，并提供了手动解锁和查询锁定列表的接口。

## 功能特性

1. **登录失败锁定**：当用户登录失败次数超过配置的阈值时，自动锁定账号一定时间。
2. **可配置参数**：登录失败次数阈值和锁定时间可配置。
3. **手动解锁**：管理员可以手动解锁被锁定的用户。
4. **查询锁定列表**：管理员可以查询当前锁定的用户列表，并按用户或时间过滤。

## 配置说明

在`application.yml`中配置以下参数：

```yaml
security:
  login:
    failed-attempts-threshold: 5 # 登录失败次数阈值，默认5次
    lock-duration: 900000 # 锁定时间，单位毫秒，默认15分钟（900000毫秒）
```

## 数据库表结构

### sys_user_lock 表

| 字段名          | 类型         | 描述           |
| --------------- | ------------ | -------------- |
| id              | bigint       | 主键           |
| user_id         | bigint       | 用户ID         |
| lock_reason     | varchar(255) | 锁定原因       |
| lock_expire_time| datetime     | 锁定到期时间   |
| is_locked       | bit          | 是否锁定       |
| create_time     | datetime     | 创建时间       |
| update_time     | datetime     | 更新时间       |

## 接口说明

### 查询锁定用户列表

```
GET /api/userLock
```

参数：
- `username`：用户名（可选）
- `nickName`：昵称（可选）
- `isLocked`：是否锁定（可选）
- `startTime`：开始时间（可选）
- `endTime`：结束时间（可选）
- `page`：页码（可选，默认1）
- `size`：每页条数（可选，默认10）

### 查询所有锁定用户

```
GET /api/userLock/all
```

参数：
- `username`：用户名（可选）
- `nickName`：昵称（可选）
- `isLocked`：是否锁定（可选）
- `startTime`：开始时间（可选）
- `endTime`：结束时间（可选）

### 手动解锁用户

```
PUT /api/userLock/unlock/{userId}
```

参数：
- `userId`：用户ID（必填）

### 批量解锁用户

```
PUT /api/userLock/unlock/batch
```

参数：
- `userIds`：用户ID列表（必填）

## 使用示例

### 配置示例

在`application.yml`中配置：

```yaml
security:
  login:
    failed-attempts-threshold: 3 # 登录失败3次锁定
    lock-duration: 300000 # 锁定5分钟（300000毫秒）
```

### 查询锁定用户列表

```
GET /api/userLock?username=test&page=1&size=10
```

### 手动解锁用户

```
PUT /api/userLock/unlock/1
```

### 批量解锁用户

```
PUT /api/userLock/unlock/batch
Content-Type: application/json

[1, 2, 3]
```
