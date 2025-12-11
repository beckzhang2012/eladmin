package me.zhengjie.modules.system.service.dto;

import lombok.Data;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则查询条件
 */
@Data
public class LimiterRuleQueryCriteria {

    private String uri;

    private Boolean enabled;
}
