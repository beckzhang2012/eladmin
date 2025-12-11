package me.zhengjie.modules.system.service.impl;

import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UserLock;
import me.zhengjie.modules.system.repository.UserLockRepository;
import me.zhengjie.modules.system.repository.UserRepository;
import me.zhengjie.modules.system.service.dto.UserLockDto;
import me.zhengjie.modules.system.service.dto.UserLockQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.UserLockMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author admin
 * @date 2025-06-27
 */
@ExtendWith(MockitoExtension.class)
public class UserLockServiceImplTest {

    @InjectMocks
    private UserLockServiceImpl userLockService;

    @Mock
    private UserLockRepository userLockRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserLockMapper userLockMapper;

    @Mock
    private Pageable pageable;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<UserLock> criteriaQuery;

    @Mock
    private Root<UserLock> root;

    @Test
    public void testQueryAll() {
        UserLockQueryCriteria criteria = new UserLockQueryCriteria();
        criteria.setUsername("test");

        UserLock userLock = new UserLock();
        userLock.setId(1L);
        userLock.setUserId(1L);
        userLock.setLockReason("登录失败次数过多");
        userLock.setIsLocked(true);

        List<UserLock> userLocks = new ArrayList<>();
        userLocks.add(userLock);

        Page<UserLock> page = new PageImpl<>(userLocks);
        when(userLockRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(page);

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setNickName("测试用户");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserLockDto userLockDto = new UserLockDto();
        userLockDto.setId(1L);
        userLockDto.setUserId(1L);
        userLockDto.setLockReason("登录失败次数过多");
        userLockDto.setIsLocked(true);

        when(userLockMapper.toDto(any(UserLock.class))).thenReturn(userLockDto);

        Object result = userLockService.queryAll(criteria, pageable);

        assertNotNull(result);
        assertTrue(result instanceof Page);
        Page<UserLockDto> resultPage = (Page<UserLockDto>) result;
        assertEquals(1, resultPage.getTotalElements());
        assertEquals("test", resultPage.getContent().get(0).getUsername());
        assertEquals("测试用户", resultPage.getContent().get(0).getNickName());
    }

    @Test
    public void testUnlock() {
        UserLock userLock = new UserLock();
        userLock.setId(1L);
        userLock.setUserId(1L);
        userLock.setLockReason("登录失败次数过多");
        userLock.setIsLocked(true);

        when(userLockRepository.findByUserId(1L)).thenReturn(Optional.of(userLock));
        when(userLockRepository.save(any(UserLock.class))).thenReturn(userLock);

        userLockService.unlock(1L);

        assertFalse(userLock.getIsLocked());
        verify(userLockRepository, times(1)).save(userLock);
    }

    @Test
    public void testUnlockBatch() {
        List<Long> userIds = new ArrayList<>();
        userIds.add(1L);
        userIds.add(2L);

        UserLock userLock1 = new UserLock();
        userLock1.setId(1L);
        userLock1.setUserId(1L);
        userLock1.setLockReason("登录失败次数过多");
        userLock1.setIsLocked(true);

        UserLock userLock2 = new UserLock();
        userLock2.setId(2L);
        userLock2.setUserId(2L);
        userLock2.setLockReason("登录失败次数过多");
        userLock2.setIsLocked(true);

        when(userLockRepository.findByUserId(1L)).thenReturn(Optional.of(userLock1));
        when(userLockRepository.findByUserId(2L)).thenReturn(Optional.of(userLock2));
        when(userLockRepository.save(any(UserLock.class))).thenReturn(null);

        userLockService.unlockBatch(userIds);

        assertFalse(userLock1.getIsLocked());
        assertFalse(userLock2.getIsLocked());
        verify(userLockRepository, times(2)).save(any(UserLock.class));
    }
}
