package me.zhengjie.modules.system.service;

import me.zhengjie.base.PageResult;
import me.zhengjie.modules.system.domain.TaskLog;
import me.zhengjie.modules.system.service.dto.TaskLogDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskLogService {

    PageResult<TaskLogDto> queryAll(Long taskId, Pageable pageable);

    List<TaskLogDto> queryAll(Long taskId);

    TaskLogDto findById(Long id);

    TaskLog create(TaskLog taskLog);

    void delete(Long id);

    void deleteAll(Long[] ids);
}
