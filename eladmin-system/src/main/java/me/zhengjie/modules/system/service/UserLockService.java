package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.service.dto.UserLockDto;
import me.zhengjie.modules.system.service.dto.UserLockQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * @author admin
 * @date 2025-06-27
 */
public interface UserLockService {

    /**
     * 查询锁定用户列表
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return Page<UserLockDto>
     */
    Object queryAll(UserLockQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有锁定用户
     * @param criteria 查询条件
     * @return List<UserLockDto>
     */
    List<UserLockDto> queryAll(UserLockQueryCriteria criteria);

    /**
     * 手动解锁用户
     * @param userId 用户ID
     */
    void unlock(Long userId);

    /**
     * 批量解锁用户
     * @param userIds 用户ID列表
     */
    void unlockBatch(List<Long> userIds);

    /**
     * 处理登录失败
     * @param username 用户名
     * @param maxFailedAttempts 最大失败尝试次数
     * @return 是否锁定用户
     */
    boolean handleLoginFailure(String username, int maxFailedAttempts);

    /**
     * 处理登录成功
     * @param username 用户名
     */
    void handleLoginSuccess(String username);

    /**
     * 检查用户是否被锁定
     * @param username 用户名
     * @return 是否锁定
     */
    boolean isLocked(String username);
}