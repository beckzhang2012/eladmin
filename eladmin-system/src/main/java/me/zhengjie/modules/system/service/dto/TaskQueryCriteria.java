package me.zhengjie.modules.system.service.dto;

import lombok.Getter;
import lombok.Setter;
import me.zhengjie.annotation.Query;

import java.util.Date;

@Getter
@Setter
public class TaskQueryCriteria {

    @Query(type = Query.Type.INNER_LIKE)
    private String taskName;

    @Query
    private String taskType;

    @Query
    private String taskStatus;

    @Query(type = Query.Type.BETWEEN)
    private Date[] createTime;

    @Query(type = Query.Type.BETWEEN)
    private Date[] startTime;

    @Query(type = Query.Type.BETWEEN)
    private Date[] endTime;

    @Query
    private String createdBy;
}
