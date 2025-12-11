package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UserLock;
import me.zhengjie.modules.system.repository.UserLockRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.UserLockService;
import me.zhengjie.modules.system.service.dto.UserLockDto;
import me.zhengjie.modules.system.service.dto.UserLockQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.UserLockMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author admin
 * @date 2025-06-27
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserLockServiceImpl implements UserLockService {

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserLockMapper userLockMapper;

    @Override
    @Transactional(readOnly = true)
    public Object queryAll(UserLockQueryCriteria criteria, Pageable pageable) {
        return PageUtil.toPage(userLockRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable)
                .map(userLock -> {
                    UserLockDto dto = userLockMapper.toDto(userLock);
                    Optional<User> user = userRepository.findById(userLock.getUserId());
                    if (user.isPresent()) {
                        dto.setUsername(user.get().getUsername());
                        dto.setNickName(user.get().getNickName());
                    }
                    return dto;
                }));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserLockDto> queryAll(UserLockQueryCriteria criteria) {
        return userLockRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder))
                .stream()
                .map(userLock -> {
                    UserLockDto dto = userLockMapper.toDto(userLock);
                    Optional<User> user = userRepository.findById(userLock.getUserId());
                    if (user.isPresent()) {
                        dto.setUsername(user.get().getUsername());
                        dto.setNickName(user.get().getNickName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void unlock(Long userId) {
        Optional<UserLock> userLockOpt = userLockRepository.findByUserId(userId);
        if (userLockOpt.isPresent()) {
            UserLock userLock = userLockOpt.get();
            userLock.setIsLocked(false);
            userLockRepository.save(userLock);
        }
    }

    @Override
    public void unlockBatch(List<Long> userIds) {
        for (Long userId : userIds) {
            unlock(userId);
        }
    }

    @Override
    public boolean handleLoginFailure(String username, int maxFailedAttempts) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }

        Optional<UserLock> userLockOptional = userLockRepository.findByUserId(user.getId());
        UserLock userLock;

        if (userLockOptional.isPresent()) {
            userLock = userLockOptional.get();
            userLock.setFailedAttempts(userLock.getFailedAttempts() + 1);
            if (userLock.getFailedAttempts() >= maxFailedAttempts) {
                userLock.setIsLocked(true);
                userLock.setLockReason("登录失败次数过多");
                userLock.setLockExpireTime(new Date(System.currentTimeMillis() + 900000)); // 锁定15分钟
            }
        } else {
            userLock = new UserLock();
            userLock.setUserId(user.getId());
            userLock.setFailedAttempts(1);
            userLock.setIsLocked(false);
        }

        userLockRepository.save(userLock);
        return userLock.getIsLocked();
    }

    @Override
    public void handleLoginSuccess(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Optional<UserLock> userLockOptional = userLockRepository.findByUserId(user.getId());
            userLockOptional.ifPresent(userLock -> {
                userLock.setFailedAttempts(0);
                userLock.setIsLocked(false);
                userLock.setLockExpireTime(null);
                userLockRepository.save(userLock);
            });
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLocked(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }

        Optional<UserLock> userLockOptional = userLockRepository.findByUserId(user.getId());
        if (userLockOptional.isPresent()) {
            UserLock userLock = userLockOptional.get();
            return userLock.getIsLocked() && (userLock.getLockExpireTime() == null || userLock.getLockExpireTime().after(new Date()));
        }

        return false;
    }
}