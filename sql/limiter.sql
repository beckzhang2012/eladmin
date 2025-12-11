-- 限流规则表
DROP TABLE IF EXISTS `sys_limiter`;
CREATE TABLE `sys_limiter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `limiter_key` varchar(255) NOT NULL COMMENT '限流键',
  `limiter_name` varchar(255) NOT NULL COMMENT '限流名称',
  `limiter_type` varchar(50) NOT NULL COMMENT '限流类型',
  `limiter_strategy` varchar(50) NOT NULL COMMENT '限流策略',
  `limiter_count` int(11) NOT NULL COMMENT '限流次数',
  `limiter_time_window` int(11) NOT NULL COMMENT '限流时间窗口(秒)',
  `limiter_message` varchar(255) DEFAULT NULL COMMENT '限流消息',
  `is_enabled` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_limiter_key` (`limiter_key`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='限流规则表';

-- 限流日志表
DROP TABLE IF EXISTS `sys_limiter_log`;
CREATE TABLE `sys_limiter_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `request_ip` varchar(20) NOT NULL COMMENT '请求IP',
  `request_path` varchar(255) NOT NULL COMMENT '请求路径',
  `request_method` varchar(10) NOT NULL COMMENT '请求方法',
  `limiter_key` varchar(255) NOT NULL COMMENT '限流键',
  `limiter_rule` text COMMENT '限流规则',
  `limiter_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '限流时间',
  `handle_result` varchar(50) COMMENT '处理结果',
  PRIMARY KEY (`id`),
  KEY `idx_request_ip` (`request_ip`),
  KEY `idx_request_path` (`request_path`),
  KEY `idx_limiter_time` (`limiter_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='限流日志表';

-- 初始化默认限流规则
INSERT INTO `sys_limiter` (`limiter_key`, `limiter_name`, `limiter_type`, `limiter_strategy`, `limiter_count`, `limiter_time_window`, `limiter_message`, `is_enabled`) VALUES 
('/auth/login:POST', '登录接口限流', 'IP', '固定窗口', 10, 60, '登录请求过于频繁，请1分钟后重试', b'1'),
('/auth/code:GET', '验证码接口限流', 'IP', '固定窗口', 10, 60, '验证码请求过于频繁，请1分钟后重试', b'1'),
('/api/code/resetEmail:POST', '重置邮箱验证码限流', 'IP', '固定窗口', 5, 60, '重置邮箱请求过于频繁，请1分钟后重试', b'1'),
('/api/code/email/resetPass:POST', '重置密码验证码限流', 'IP', '固定窗口', 5, 60, '重置密码请求过于频繁，请1分钟后重试', b'1');
