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
package me.zhengjie.modules.system.service;

import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.domain.LoginLock;
import me.zhengjie.modules.system.service.dto.LoginLockDto;
import me.zhengjie.modules.system.service.dto.LoginLockQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2025-01-23
 */
public interface LoginLockService {

    /**
     * 查询全部数据
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    PageResult<LoginLockDto> queryAll(LoginLockQueryCriteria criteria, Pageable pageable);

    /**
     * 查询全部数据
     * @param criteria 条件
     * @return /
     */
    List<LoginLockDto> queryAll(LoginLockQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return /
     */
    LoginLockDto findById(Long id);

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return /
     */
    LoginLockDto findByUsername(String username);

    /**
     * 创建登录锁定记录
     * @param resources /
     */
    void create(LoginLock resources);

    /**
     * 更新登录锁定记录
     * @param resources /
     */
    void update(LoginLock resources);

    /**
     * 删除登录锁定记录
     * @param id /
     */
    void delete(Long id);

    /**
     * 解锁用户
     * @param username 用户名
     */
    void unlock(String username);

    /**
     * 检查用户是否被锁定
     * @param username 用户名
     * @return /
     */
    boolean isLocked(String username);

    /**
     * 记录登录失败次数
     * @param username 用户名
     */
    void recordFailedAttempt(String username);
}