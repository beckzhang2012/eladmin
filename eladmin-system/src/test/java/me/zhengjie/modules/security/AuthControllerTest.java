package me.zhengjie.modules.security;

import me.zhengjie.modules.security.rest.AuthController;
import me.zhengjie.modules.security.service.dto.AuthUserDto;
import me.zhengjie.modules.system.domain.UserLock;
import me.zhengjie.modules.system.repository.UserLockRepository;
import me.zhengjie.utils.RedisUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author admin
 * @date 2025-06-27
 */
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserLockRepository userLockRepository;

    @Mock
    private RedisUtils redisUtils;

    @Mock
    private HttpServletRequest request;

    @Test
    public void testLoginFailedTooManyTimes() throws Exception {
        AuthUserDto authUser = new AuthUserDto();
        authUser.setUsername("test");
        authUser.setPassword("wrongPassword");
        authUser.setCode("1234");
        authUser.setUuid("123456");

        // 模拟前4次登录失败
        when(redisUtils.get(anyString(), eq(Integer.class))).thenReturn(4);
        when(redisUtils.set(anyString(), anyInt(), anyLong(), any())).thenReturn(true);

        try {
            authController.login(authUser, request);
        } catch (Exception e) {
            // 第5次登录失败应该锁定用户
            verify(userLockRepository, times(1)).save(any(UserLock.class));
            verify(redisUtils, times(1)).del(anyString());
            assertEquals("登录失败5次，账号已被锁定15分钟", e.getMessage());
        }
    }

    @Test
    public void testLoginWhenLocked() throws Exception {
        AuthUserDto authUser = new AuthUserDto();
        authUser.setUsername("test");
        authUser.setPassword("password");
        authUser.setCode("1234");
        authUser.setUuid("123456");

        // 模拟用户已被锁定
        UserLock userLock = new UserLock();
        userLock.setIsLocked(true);
        userLock.setLockExpireTime(new java.util.Date(System.currentTimeMillis() + 1000 * 60 * 15)); // 15分钟后过期
        when(userLockRepository.findByUserId(anyLong())).thenReturn(Optional.of(userLock));

        try {
            authController.login(authUser, request);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("账号已被锁定，请在"));
        }
    }
}
