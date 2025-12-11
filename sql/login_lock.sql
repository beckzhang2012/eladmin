-- ----------------------------
-- Table structure for sys_login_lock
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_lock`;
CREATE TABLE `sys_login_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `username` varchar(180) NOT NULL COMMENT '用户名',
  `failed_count` int(11) DEFAULT 0 COMMENT '登录失败次数',
  `lock_reason` varchar(255) DEFAULT NULL COMMENT '锁定原因',
  `lock_time` datetime DEFAULT NULL COMMENT '锁定时间',
  `expire_time` datetime DEFAULT NULL COMMENT '到期时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_username` (`username`) USING BTREE,
  KEY `idx_expire_time` (`expire_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='登录失败锁定表';

-- ----------------------------
-- Records of sys_login_lock
-- ----------------------------
BEGIN;
COMMIT;
