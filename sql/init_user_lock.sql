-- 初始化用户锁定表数据
INSERT INTO sys_user_lock (user_id, lock_reason, lock_expire_time, is_locked, create_time, update_time) VALUES 
(1, '登录失败次数过多', DATE_ADD(NOW(), INTERVAL 15 MINUTE), 1, NOW(), NOW()),
(2, '登录失败次数过多', DATE_ADD(NOW(), INTERVAL 15 MINUTE), 1, NOW(), NOW());
