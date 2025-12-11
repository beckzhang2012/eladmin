package me.zhengjie.modules.system.service;

import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UserLock;
import me.zhengjie.modules.system.repository.UserLockRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.impl.UserLockServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginLockTest {

    @Mock
    private UserLockRepository userLockRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserLockServiceImpl userLockService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleLoginFailure() {
        // 测试登录失败次数未达到阈值
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        UserLock userLock = new UserLock();
        userLock.setUserId(1L);
        userLock.setFailedAttempts(3);
        userLock.setIsLocked(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userLockRepository.findByUserId(1L)).thenReturn(Optional.of(userLock));

        boolean isLocked = userLockService.handleLoginFailure("test", 5);
        assertFalse(isLocked);
        verify(userLockRepository, times(1)).save(userLock);
        assertEquals(4, userLock.getFailedAttempts());

        // 测试登录失败次数达到阈值，用户被锁定
        userLock.setFailedAttempts(4);
        isLocked = userLockService.handleLoginFailure("test", 5);
        assertTrue(isLocked);
        verify(userLockRepository, times(2)).save(userLock);
        assertTrue(userLock.getIsLocked());
        assertNotNull(userLock.getLockExpireTime());
    }

    @Test
    public void testHandleLoginSuccess() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        UserLock userLock = new UserLock();
        userLock.setUserId(1L);
        userLock.setFailedAttempts(3);
        userLock.setIsLocked(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userLockRepository.findByUserId(1L)).thenReturn(Optional.of(userLock));

        userLockService.handleLoginSuccess("test");
        verify(userLockRepository, times(1)).save(userLock);
        assertEquals(0, userLock.getFailedAttempts());
    }

    @Test
    public void testIsLocked() {
        // 测试用户未锁定
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        UserLock userLock = new UserLock();
        userLock.setUserId(1L);
        userLock.setIsLocked(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userLockRepository.findByUserId(1L)).thenReturn(Optional.of(userLock));

        boolean isLocked = userLockService.isLocked("test");
        assertFalse(isLocked);

        // 测试用户已锁定
        userLock.setIsLocked(true);
        userLock.setLockExpireTime(new Date(System.currentTimeMillis() + 3600000)); // 1小时后过期
        isLocked = userLockService.isLocked("test");
        assertTrue(isLocked);

        // 测试用户锁定已过期
        userLock.setLockExpireTime(new Date(System.currentTimeMillis() - 3600000)); // 1小时前过期
        isLocked = userLockService.isLocked("test");
        assertFalse(isLocked);
    }
}