package me.zhengjie.modules.system.service.mapstruct;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.system.domain.TaskLog;
import me.zhengjie.modules.system.service.dto.TaskLogDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskLogMapper extends BaseMapper<TaskLogDto, TaskLog> {
}
