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
package me.zhengjie.modules.system.service.impl;

import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.system.domain.LimitLog;
import me.zhengjie.modules.system.repository.LimitLogRepository;
import me.zhengjie.modules.system.service.LimitLogService;
import me.zhengjie.modules.system.service.dto.LimitLogDto;
import me.zhengjie.modules.system.service.dto.LimitLogQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.LimitLogMapper;
import me.zhengjie.utils.PageUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author /
 */
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class LimitLogServiceImpl implements LimitLogService {

    private final LimitLogRepository limitLogRepository;
    private final LimitLogMapper limitLogMapper;

    @Override
    public List<LimitLogDto> queryAll(LimitLogQueryCriteria criteria) {
        List<LimitLog> limitLogs = limitLogRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        return limitLogMapper.toDto(limitLogs);
    }

    @Override
    public Object queryAll(LimitLogQueryCriteria criteria, Pageable pageable) {
        return PageUtil.toPage(limitLogRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.conjunction(), pageable), limitLogMapper);
    }

    @Override
    public LimitLogDto findById(Long id) {
        LimitLog limitLog = limitLogRepository.findById(id).orElseGet(LimitLog::new);
        return limitLogMapper.toDto(limitLog);
    }

    @Override
    public void save(LimitLog resources) {
        limitLogRepository.save(resources);
    }

    @Override
    public void create(LimitLog resources) {
        limitLogRepository.save(resources);
    }

    @Override
    public void update(LimitLog resources) {
        LimitLog limitLog = limitLogRepository.findById(resources.getId()).orElseGet(LimitLog::new);
        limitLog.setIp(resources.getIp());
        limitLog.setUri(resources.getUri());
        limitLog.setCount(resources.getCount());
        limitLog.setIsLimited(resources.getIsLimited());
        limitLogRepository.save(limitLog);
    }

    @Override
    public void delete(Long id) {
        limitLogRepository.deleteById(id);
    }
}
