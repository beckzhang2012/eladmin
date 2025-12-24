package me.zhengjie.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.base.BaseDTO;

import java.util.Date;

@Getter
@Setter
public class TaskDto extends BaseDTO {

    private Long id;

    private String taskName;

    private String taskType;

    private String taskStatus;

    private String taskParams;

    private String filePath;

    private String fileName;

    private Long fileSize;

    private Long totalRecords;

    private Long successRecords;

    private Long errorRecords;

    private String errorMessage;

    private Date startTime;

    private Date endTime;

    private String createdBy;

    private Date createdAt;

    private String updatedBy;

    private Date updatedAt;
}
