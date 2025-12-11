package me.zhengjie.modules.system.service.dto;

import lombok.Data;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流日志查询条件
 */
@Data
public class LimiterLogQueryCriteria {

    private String ip;

    private String uri;

    private String startTime;

    private String endTime;
}
