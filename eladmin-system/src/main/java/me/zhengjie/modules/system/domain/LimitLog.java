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

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author /
 */
@Entity
@Data
@Table(name = "sys_limit_log")
public class LimitLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 访问IP
     */
    @Column(name = "ip", nullable = false)
    private String ip;

    /**
     * 接口URI
     */
    @Column(name = "uri", nullable = false)
    private String uri;

    /**
     * 访问时间
     */
    @CreationTimestamp
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 访问次数
     */
    @Column(name = "count", nullable = false)
    private Integer count;

    /**
     * 是否被限流
     */
    @Column(name = "is_limited", nullable = false)
    private Boolean isLimited;
}
