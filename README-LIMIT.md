# 限流功能使用说明

## 一、功能介绍

本项目实现了基于Redis+Lua的分布式限流功能，可以对接口的访问频率进行限制，防止恶意攻击和接口滥用。

## 二、实现原理

1. **限流算法**：采用令牌桶算法，通过Redis的原子操作和Lua脚本实现分布式限流
2. **限流类型**：支持基于IP和自定义key的限流
3. **限流粒度**：可以对单个接口或一组接口进行限流
4. **限流日志**：记录所有接口的访问次数和限流情况

## 三、使用方法

### 1. 注解方式限流

在需要限流的Controller方法上添加@Limit注解：

```java
@Limit(key = "login", period = 60, count = 10, name = "登录接口", prefix = "limit", limitType = LimitType.IP)
@PostMapping("/login")
public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
    // 登录逻辑
}
```

**注解参数说明**：
- `key`：限流的key，用于区分不同的限流规则
- `period`：时间窗口，单位秒
- `count`：时间窗口内的最大请求数
- `name`：接口名称，用于日志记录
- `prefix`：Redis key的前缀
- `limitType`：限流类型，支持CUSTOMER（自定义key）和IP（基于IP）

### 2. 数据库配置限流

可以通过数据库配置限流规则，动态调整限流参数：

```sql
INSERT INTO `sys_limit_rule` (`uri`, `period`, `count`, `description`) VALUES ('/auth/login', 60, 10, '登录接口限流');
```

**表字段说明**：
- `uri`：接口URI
- `period`：时间窗口，单位秒
- `count`：时间窗口内的最大请求数
- `description`：限流规则描述

### 3. 限流日志查询

可以通过以下接口查询限流日志：

```http
GET /api/limitLog?ip=192.168.1.1&uri=/auth/login
```

**返回参数说明**：
- `ip`：访问IP
- `uri`：接口URI
- `count`：请求次数
- `isLimited`：是否受限
- `createTime`：访问时间

## 四、注意事项

1. 限流功能依赖Redis，确保Redis服务正常运行
2. 限流规则的优先级：注解方式 > 数据库配置方式
3. 限流日志会占用数据库空间，建议定期清理
4. 当接口访问频率超过限制时，会返回"访问次数受限制"的错误信息
