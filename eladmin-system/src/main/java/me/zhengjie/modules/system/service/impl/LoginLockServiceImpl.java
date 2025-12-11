/*
 *  Copyright 2019-2025 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.utils.PageResult;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.LoginLock;
import me.zhengjie.modules.system.repository.LoginLockRepository;
import me.zhengjie.modules.system.service.LoginLockService;
import me.zhengjie.modules.system.service.dto.LoginLockDto;
import me.zhengjie.modules.system.service.dto.LoginLockQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.LoginLockMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2025-01-23
 */
@Service
@RequiredArgsConstructor
public class LoginLockServiceImpl implements LoginLockService {

    private final LoginLockRepository loginLockRepository;
    private final LoginLockMapper loginLockMapper;

    @Override
    public PageResult<LoginLockDto> queryAll(LoginLockQueryCriteria criteria, Pageable pageable) {
        Page<LoginLock> page = loginLockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(loginLockMapper::toDto));
    }

    @Override
    public List<LoginLockDto> queryAll(LoginLockQueryCriteria criteria) {
        List<LoginLock> loginLocks = loginLockRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder));
        return loginLockMapper.toDto(loginLocks);
    }

    @Override
    public LoginLockDto findById(Long id) {
        LoginLock loginLock = loginLockRepository.findById(id).orElseGet(LoginLock::new);
        return loginLockMapper.toDto(loginLock);
    }

    @Override
    public LoginLockDto findByUsername(String username) {
        LoginLock loginLock = loginLockRepository.findByUsername(username).orElseGet(LoginLock::new);
        return loginLockMapper.toDto(loginLock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(LoginLock resources) {
        loginLockRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LoginLock resources) {
        LoginLock loginLock = loginLockRepository.findById(resources.getId()).orElseGet(LoginLock::new);
        loginLock.setFailedCount(resources.getFailedCount());
        loginLock.setLockReason(resources.getLockReason());
        loginLock.setLockTime(resources.getLockTime());
        loginLock.setExpireTime(resources.getExpireTime());
        loginLockRepository.save(loginLock);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        loginLockRepository.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unlock(String username) {
        loginLockRepository.deleteByUsername(username);
    }

    @Override
    public boolean isLocked(String username) {
        return loginLockRepository.findByUsername(username)
                .map(loginLock -> loginLock.getExpireTime() != null && new Date().before(loginLock.getExpireTime()))
                .orElse(false);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordFailedAttempt(String username) {
        LoginLock loginLock = loginLockRepository.findByUsername(username).orElseGet(() -> {
            LoginLock newLock = new LoginLock();
            newLock.setUsername(username);
            newLock.setFailedCount(0);
            return newLock;
        });
        loginLock.setFailedCount(loginLock.getFailedCount() + 1);
        loginLock.setLockReason("登录失败次数过多");
        loginLock.setLockTime(new Date());
        loginLock.setExpireTime(new Date(System.currentTimeMillis() + 15 * 60 * 1000)); // 15分钟后解锁
        loginLockRepository.save(loginLock);
    }
}