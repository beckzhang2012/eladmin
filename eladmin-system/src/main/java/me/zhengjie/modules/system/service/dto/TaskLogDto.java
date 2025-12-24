package me.zhengjie.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.util.Date;

@Getter
@Setter
public class TaskLogDto extends BaseDTO {

    private Long id;

    private Long taskId;

    private String logType;

    private String logMessage;

    private Date logTime;

    private String createdBy;

    private Date createdAt;
}
