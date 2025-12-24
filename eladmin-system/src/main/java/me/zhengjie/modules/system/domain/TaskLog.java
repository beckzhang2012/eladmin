package me.zhengjie.modules.system.domain;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "sys_task_log")
public class TaskLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "log_type", nullable = false)
    private String logType;

    @Column(name = "log_message", columnDefinition = "TEXT")
    private String logMessage;

    @Column(name = "log_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date logTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "version")
    private Integer version = 0;
}
