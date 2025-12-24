-- 任务管理表
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '任务名称',
  `type` varchar(255) DEFAULT NULL COMMENT '任务类型（导出/导入）',
  `status` varchar(255) DEFAULT NULL COMMENT '任务状态（等待中/处理中/成功/失败/已取消）',
  `data_type` varchar(255) DEFAULT NULL COMMENT '数据类型（users/roles/menus等）',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件路径',
  `file_format` varchar(255) DEFAULT NULL COMMENT '文件格式（CSV/Excel）',
  `record_count` bigint(20) DEFAULT NULL COMMENT '处理记录数',
  `error_msg` text DEFAULT NULL COMMENT '错误信息',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `creator_name` varchar(255) DEFAULT NULL COMMENT '创建者名称',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT COMMENT='任务管理表';
