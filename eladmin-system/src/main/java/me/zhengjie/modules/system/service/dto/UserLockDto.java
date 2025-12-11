package me.zhengjie.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

/**
 * @author admin
 * @date 2025-06-27
 */
@Getter
@Setter
public class UserLockDto {

    private Long id;

    private Long userId;

    private String username;

    private String nickName;

    private String lockReason;

    private Date lockExpireTime;

    private Boolean isLocked;

    private Date createTime;

    private Date updateTime;
}
