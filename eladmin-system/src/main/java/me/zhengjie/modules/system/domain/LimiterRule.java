package me.zhengjie.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author 郑杰
 * @date 2025-06-25
 * 限流规则实体
 */
@Data
@Entity
@Table(name = "sys_limiter_rule")
public class LimiterRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uri", unique = true, nullable = false)
    private String uri;

    @Column(name = "max_requests", nullable = false)
    private Integer maxRequests = 10;

    @Column(name = "time_window", nullable = false)
    private Integer timeWindow = 60;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "create_by")
    private String createBy;

    @Column(name = "update_by")
    private String updateBy;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time", nullable = false)
    private Date updateTime;
}
