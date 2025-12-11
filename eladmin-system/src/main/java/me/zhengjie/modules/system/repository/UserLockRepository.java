package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.UserLock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/**
 * @author admin
 * @date 2025-06-27
 */
public interface UserLockRepository extends JpaRepository<UserLock, Long>, JpaSpecificationExecutor<UserLock> {

    /**
     * 根据用户ID查询锁定记录
     * @param userId 用户ID
     * @return 锁定记录
     */
    Optional<UserLock> findByUserId(Long userId);
}
