-- 限流规则表
CREATE TABLE `sys_limit_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uri` varchar(255) NOT NULL COMMENT '接口URI',
  `period` int(11) NOT NULL COMMENT '时间窗口(秒)',
  `count` int(11) NOT NULL COMMENT '最大请求数',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uri` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='限流规则表';

-- 限流日志表
CREATE TABLE `sys_limit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ip` varchar(255) NOT NULL COMMENT 'IP地址',
  `uri` varchar(255) NOT NULL COMMENT '接口URI',
  `count` int(11) NOT NULL COMMENT '请求次数',
  `is_limited` tinyint(1) NOT NULL COMMENT '是否受限',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `ip` (`ip`),
  KEY `uri` (`uri`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='限流日志表';

-- 插入初始限流规则
INSERT INTO `sys_limit_rule` (`uri`, `period`, `count`, `description`) VALUES ('/auth/login', 60, 10, '登录接口限流');
INSERT INTO `sys_limit_rule` (`uri`, `period`, `count`, `description`) VALUES ('/auth/code', 60, 10, '验证码接口限流');
