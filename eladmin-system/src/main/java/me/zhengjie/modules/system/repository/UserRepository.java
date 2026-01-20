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

    /*=================== 用户状态统计 ===================*/
    
    /**
     * 根据用户状态统计用户数量
     * @param enabled 是否启用
     * @return 用户数量
     */
    Long countByEnabled(Boolean enabled);

    /**
     * 按部门统计用户数量
     * @return 部门ID和用户数量的映射
     */
    @Query(value = "SELECT dept_id, count(*) FROM sys_user GROUP BY dept_id", nativeQuery = true)
    List<Object[]> countByDept();

    /**
     * 按角色统计用户数量
     * @return 角色ID和用户数量的映射
     */
    @Query(value = "SELECT role_id, count(*) FROM sys_users_roles GROUP BY role_id", nativeQuery = true)
    List<Object[]> countByRole();

    /**
     * 按岗位统计用户数量
     * @return 岗位ID和用户数量的映射
     */
    @Query(value = "SELECT job_id, count(*) FROM sys_users_jobs GROUP BY job_id", nativeQuery = true)
    List<Object[]> countByJob();

    /*=================== 活跃度分析 ===================*/
    
    /**
     * 查询最近N天注册的用户
     * @param days 天数
     * @return 用户列表
     */
    @Query(value = "SELECT * FROM sys_user WHERE create_time >= DATE_SUB(NOW(), INTERVAL ?1 DAY)", nativeQuery = true)
    List<User> findRecentlyRegisteredUsers(int days);

    /**
     * 查询最近N天活跃的用户（根据更新时间）
     * @param days 天数
     * @return 用户列表
     */
    @Query(value = "SELECT * FROM sys_user WHERE update_time >= DATE_SUB(NOW(), INTERVAL ?1 DAY)", nativeQuery = true)
    List<User> findRecentlyActiveUsers(int days);

    /**
     * 查询长期不活跃的用户
     * @param days 天数
     * @return 用户列表
     */
    @Query(value = "SELECT * FROM sys_user WHERE update_time < DATE_SUB(NOW(), INTERVAL ?1 DAY)", nativeQuery = true)
    List<User> findInactiveUsers(int days);

    /**
     * 查询用户活跃度排名（按更新时间倒序）
     * @param limit 数量限制
     * @return 用户列表
     */
    @Query(value = "SELECT * FROM sys_user ORDER BY update_time DESC LIMIT ?1", nativeQuery = true)
    List<User> findMostActiveUsers(int limit);

    /*=================== 批量操作 ===================*/
    
    /**
     * 批量更新用户状态
     * @param ids 用户ID集合
     * @param enabled 是否启用
     */
    @Modifying
    @Query(value = "update sys_user set enabled = ?2 where user_id in ?1", nativeQuery = true)
    void updateEnabledBatch(Set<Long> ids, Boolean enabled);

    /**
     * 批量更新用户部门
     * @param ids 用户ID集合
     * @param deptId 部门ID
     */
    @Modifying
    @Query(value = "update sys_user set dept_id = ?2 where user_id in ?1", nativeQuery = true)
    void updateDeptBatch(Set<Long> ids, Long deptId);

    /**
     * 批量添加用户角色
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    @Modifying
    @Query(value = "INSERT INTO sys_users_roles(user_id, role_id) VALUES (?1, ?2)", nativeQuery = true)
    void addRoleBatch(Long userId, Long roleId);

    /**
     * 批量删除用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID集合
     */
    @Modifying
    @Query(value = "DELETE FROM sys_users_roles WHERE user_id = ?1 AND role_id IN ?2", nativeQuery = true)
    void removeRoleBatch(Long userId, Set<Long> roleIds);

    /**
     * 批量添加用户岗位
     * @param userId 用户ID
     * @param jobId 岗位ID
     */
    @Modifying
    @Query(value = "INSERT INTO sys_users_jobs(user_id, job_id) VALUES (?1, ?2)", nativeQuery = true)
    void addJobBatch(Long userId, Long jobId);

    /**
     * 批量删除用户岗位
     * @param userId 用户ID
     * @param jobIds 岗位ID集合
     */
    @Modifying
    @Query(value = "DELETE FROM sys_users_jobs WHERE user_id = ?1 AND job_id IN ?2", nativeQuery = true)
    void removeJobBatch(Long userId, Set<Long> jobIds);
}
