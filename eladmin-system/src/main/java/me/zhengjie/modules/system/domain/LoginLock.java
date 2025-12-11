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
package me.zhengjie.modules.system.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author Zheng Jie
 * @date 2025-01-23
 */
@Entity
@Getter
@Setter
@Table(name="sys_login_lock")
public class LoginLock extends BaseEntity implements Serializable {

    @Id
    @Column(name = "id")
    @NotNull(groups = Update.class)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "ID", hidden = true)
    private Long id;

    @Column(name = "user_id")
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @NotBlank
    @Column(name = "username")
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "failed_count")
    @ApiModelProperty(value = "失败次数")
    private Integer failedCount;

    @Column(name = "lock_reason")
    @ApiModelProperty(value = "锁定原因")
    private String lockReason;

    @Column(name = "lock_time")
    @ApiModelProperty(value = "锁定时间")
    private Date lockTime;

    @Column(name = "expire_time")
    @ApiModelProperty(value = "过期时间")
    private Date expireTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginLock loginLock = (LoginLock) o;
        return Objects.equals(id, loginLock.id) &&
                Objects.equals(username, loginLock.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }
}