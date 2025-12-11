package me.zhengjie.modules.system.domain;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;

/**
 * @author admin
 * @date 2025-06-27
 */
@Data
@Entity
@Table(name = "sys_user_lock")
public class UserLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "lock_reason")
    private String lockReason;

    @Column(name = "lock_expire_time")
    private Date lockExpireTime;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "failed_attempts")
    private Integer failedAttempts;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @PrePersist
    public void prePersist() {
        this.createTime = new Date();
        this.updateTime = new Date();
        this.isLocked = false;
        this.failedAttempts = 0;
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLockReason() {
        return lockReason;
    }

    public void setLockReason(String lockReason) {
        this.lockReason = lockReason;
    }

    public Date getLockExpireTime() {
        return lockExpireTime;
    }

    public void setLockExpireTime(Date lockExpireTime) {
        this.lockExpireTime = lockExpireTime;
    }

    public Boolean getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(Boolean isLocked) {
        this.isLocked = isLocked;
    }

    public Integer getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(Integer failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}