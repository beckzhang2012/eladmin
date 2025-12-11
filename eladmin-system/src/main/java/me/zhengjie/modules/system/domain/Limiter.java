package me.zhengjie.modules.system.domain;

import lombok.Data;
import javax.persistence.*;
import java.sql.Timestamp;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则实体类
 */
@Data
@Entity
@Table(name = "sys_limiter")
public class Limiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 限流键
     */
    @Column(name = "limiter_key", nullable = false, unique = true)
    private String limiterKey;

    /**
     * 限流名称
     */
    @Column(name = "limiter_name", nullable = false)
    private String limiterName;

    /**
     * 限流类型
     */
    @Column(name = "limiter_type", nullable = false)
    private String limiterType;

    /**
     * 限流策略
     */
    @Column(name = "limiter_strategy", nullable = false)
    private String limiterStrategy;

    /**
     * 限流次数
     */
    @Column(name = "limiter_count", nullable = false)
    private Integer limiterCount;

    /**
     * 限流时间窗口(秒)
     */
    @Column(name = "limiter_time_window", nullable = false)
    private Integer limiterTimeWindow;

    /**
     * 限流消息
     */
    @Column(name = "limiter_message")
    private String limiterMessage;

    /**
     * 是否启用
     */
    @Column(name = "is_enabled", nullable = false)
    private Boolean isEnabled;

    /**
     * 创建时间
     */
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Timestamp updateTime;
}
