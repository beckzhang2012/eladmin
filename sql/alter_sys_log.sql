-- 添加module字段
ALTER TABLE `sys_log` ADD COLUMN `module` varchar(50) DEFAULT NULL COMMENT '模块';

-- 添加action字段
ALTER TABLE `sys_log` ADD COLUMN `action` varchar(50) DEFAULT NULL COMMENT '动作：新增/修改/删除';

-- 添加target_id字段
ALTER TABLE `sys_log` ADD COLUMN `target_id` varchar(255) DEFAULT NULL COMMENT '目标ID';

-- 添加索引
ALTER TABLE `sys_log` ADD INDEX `idx_module` (`module`);
ALTER TABLE `sys_log` ADD INDEX `idx_action` (`action`);
ALTER TABLE `sys_log` ADD INDEX `idx_target_id` (`target_id`);