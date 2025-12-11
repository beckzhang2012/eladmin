package me.zhengjie.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流日志实体
 */
@Data
@Entity
@Table(name = "sys_limiter_log")
public class LimiterLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 请求IP
     */
    @Column(name = "request_ip", nullable = false)
    private String requestIp;

    /**
     * 请求路径
     */
    @Column(name = "request_path", nullable = false)
    private String requestPath;

    /**
     * 请求方法
     */
    @Column(name = "request_method", nullable = false)
    private String requestMethod;

    /**
     * 限流键
     */
    @Column(name = "limiter_key", nullable = false)
    private String limiterKey;

    /**
     * 限流规则
     */
    @Column(name = "limiter_rule")
    private String limiterRule;

    /**
     * 限流时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "limiter_time", nullable = false)
    private Date limiterTime;

    /**
     * 处理结果
     */
    @Column(name = "handle_result")
    private String handleResult;
}
