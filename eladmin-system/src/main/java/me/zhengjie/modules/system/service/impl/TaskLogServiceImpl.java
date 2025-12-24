package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.base.PageResult;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.TaskLog;
import me.zhengjie.modules.system.repository.TaskLogRepository;
import me.zhengjie.modules.system.service.TaskLogService;
import me.zhengjie.modules.system.service.dto.TaskLogDto;
import me.zhengjie.modules.system.service.mapstruct.TaskLogMapper;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class TaskLogServiceImpl implements TaskLogService {

    private final TaskLogRepository taskLogRepository;
    private final TaskLogMapper taskLogMapper;

    @Override
    public PageResult<TaskLogDto> queryAll(Long taskId, Pageable pageable) {
        Page<TaskLog> page = taskLogRepository.findAll((root, query, cb) -> {
            query.where(cb.equal(root.get("taskId"), taskId));
            query.orderBy(cb.desc(root.get("logTime")));
            return query.getRestriction();
        }, pageable);
        return PageUtil.toPage(page.map(taskLogMapper::toDto));
    }

    @Override
    public List<TaskLogDto> queryAll(Long taskId) {
        return taskLogMapper.toDto(taskLogRepository.findByTaskId(taskId));
    }

    @Override
    public TaskLogDto findById(Long id) {
        TaskLog taskLog = taskLogRepository.findById(id).orElseThrow(() -> new BadRequestException("任务日志不存在"));
        return taskLogMapper.toDto(taskLog);
    }

    @Override
    public TaskLog create(TaskLog taskLog) {
        taskLog.setLogTime(new Date());
        taskLog.setCreatedBy(SecurityUtils.getUsername());
        taskLog.setCreatedAt(new Date());
        taskLog.setUpdatedBy(SecurityUtils.getUsername());
        taskLog.setUpdatedAt(new Date());
        return taskLogRepository.save(taskLog);
    }

    @Override
    public void delete(Long id) {
        taskLogRepository.deleteById(id);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            delete(id);
        }
    }
}
