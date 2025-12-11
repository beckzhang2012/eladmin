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
public class UserLockQueryCriteria {

    private String username;

    private String nickName;

    private Boolean isLocked;

    private Date createTimeStart;

    private Date createTimeEnd;

    private Date lockExpireTimeStart;

    private Date lockExpireTimeEnd;
}
