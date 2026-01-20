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
package me.zhengjie.modules.system.repository;

import me.zhengjie.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author Zheng Jie
 * @date 2018-11-22
 */
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询
     * @param username 用户名
     * @return /
     */
    User findByUsername(String username);

    /**
     * 根据邮箱查询
     * @param email 邮箱
     * @return /
     */
    User findByEmail(String email);

    /**
     * 根据手机号查询
     * @param phone 手机号
     * @return /
     */
    User findByPhone(String phone);

    /**
     * 修改密码
     * @param username 用户名
     * @param pass 密码
     * @param lastPasswordResetTime /
     */
    @Modifying
    @Query(value = "update sys_user set password = ?2 , pwd_reset_time = ?3 where username = ?1",nativeQuery = true)
    void updatePass(String username, String pass, Date lastPasswordResetTime);

    /**
     * 修改邮箱
     * @param username 用户名
     * @param email 邮箱
     */
    @Modifying
    @Query(value = "update sys_user set email = ?2 where username = ?1",nativeQuery = true)
    void updateEmail(String username, String email);

    /**
     * 根据角色查询用户
     * @param roleId /
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r WHERE" +
            " u.user_id = r.user_id AND r.role_id = ?1", nativeQuery = true)
    List<User> findByRoleId(Long roleId);

    /**
     * 根据角色中的部门查询
     * @param deptId /
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles r, sys_roles_depts d WHERE " +
            "u.user_id = r.user_id AND r.role_id = d.role_id AND d.dept_id = ?1 group by u.user_id", nativeQuery = true)
    List<User> findByRoleDeptId(Long deptId);

    /**
     * 根据菜单查询
     * @param id 菜单ID
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u, sys_users_roles ur, sys_roles_menus rm WHERE\n" +
            "u.user_id = ur.user_id AND ur.role_id = rm.role_id AND rm.menu_id = ?1 group by u.user_id", nativeQuery = true)
    List<User> findByMenuId(Long id);

    /**
     * 根据Id删除
     * @param ids /
     */
    void deleteAllByIdIn(Set<Long> ids);

    /**
     * 根据岗位查询
     * @param ids /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_jobs j WHERE u.user_id = j.user_id AND j.job_id IN ?1", nativeQuery = true)
    int countByJobs(Set<Long> ids);

    /**
     * 根据部门查询
     * @param deptIds /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id IN ?1", nativeQuery = true)
    int countByDepts(Set<Long> deptIds);

    /**
     * 根据角色查询
     * @param ids /
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_roles r WHERE " +
            "u.user_id = r.user_id AND r.role_id in ?1", nativeQuery = true)
    int countByRoles(Set<Long> ids);

    /**
     * 重置密码
     * @param ids 、
     * @param pwd 、
     */
    @Modifying
    @Query(value = "update sys_user set password = ?2 where user_id in ?1",nativeQuery = true)
    void resetPwd(Set<Long> ids, String pwd);

    /**
     * 统计用户状态
     * @param enabled 是否启用
     * @return /
     */
    long countByEnabled(Boolean enabled);

    /**
     * 按部门统计用户数量
     * @param deptId 部门ID
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id = ?1", nativeQuery = true)
    long countByDeptId(Long deptId);

    /**
     * 按部门和状态统计用户数量
     * @param deptId 部门ID
     * @param enabled 是否启用
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.dept_id = ?1 AND u.enabled = ?2", nativeQuery = true)
    long countByDeptIdAndEnabled(Long deptId, Boolean enabled);

    /**
     * 按角色统计用户数量
     * @param roleId 角色ID
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u, sys_users_roles r WHERE u.user_id = r.user_id AND r.role_id = ?1", nativeQuery = true)
    long countUsersByRoleId(Long roleId);

    /**
     * 统计所有用户数量
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user", nativeQuery = true)
    long countAllUsers();

    /**
     * 根据时间范围统计新增用户数量
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return /
     */
    @Query(value = "SELECT count(1) FROM sys_user u WHERE u.create_time BETWEEN ?1 AND ?2", nativeQuery = true)
    long countUsersByCreateTimeBetween(Timestamp startTime, Timestamp endTime);

    /**
     * 获取最近活跃的用户（根据更新时间）
     * @param days 天数
     * @return /
     */
    @Query(value = "SELECT u.* FROM sys_user u WHERE u.update_time >= DATE_SUB(NOW(), INTERVAL ?1 DAY) ORDER BY u.update_time DESC", nativeQuery = true)
    List<User> findActiveUsersInDays(int days);

    /**
     * 批量启用/禁用用户
     * @param ids 用户ID列表
     * @param enabled 是否启用
     */
    @Modifying
    @Query(value = "update sys_user set enabled = ?2 where user_id in ?1",nativeQuery = true)
    void batchUpdateEnabled(Set<Long> ids, Boolean enabled);

    /**
     * 批量修改用户部门
     * @param ids 用户ID列表
     * @param deptId 部门ID
     */
    @Modifying
    @Query(value = "update sys_user set dept_id = ?2 where user_id in ?1",nativeQuery = true)
    void batchUpdateDept(Set<Long> ids, Long deptId);

    /**
     * 批量删除用户角色关联
     * @param ids 用户ID列表
     */
    @Modifying
    @Query(value = "delete from sys_users_roles where user_id in ?1",nativeQuery = true)
    void batchDeleteUserRoles(Set<Long> ids);

    /**
     * 批量删除用户岗位关联
     * @param ids 用户ID列表
     */
    @Modifying
    @Query(value = "delete from sys_users_jobs where user_id in ?1",nativeQuery = true)
    void batchDeleteUserJobs(Set<Long> ids);
}
