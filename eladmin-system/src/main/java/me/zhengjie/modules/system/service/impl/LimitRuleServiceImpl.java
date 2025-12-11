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
import me.zhengjie.modules.system.domain.LimitRule;
import me.zhengjie.modules.system.repository.LimitRuleRepository;
import me.zhengjie.modules.system.service.LimitRuleService;
import me.zhengjie.modules.system.service.dto.LimitRuleDto;
import me.zhengjie.modules.system.service.dto.LimitRuleQueryCriteria;
import me.zhengjie.modules.system.service.mapstruct.LimitRuleMapper;
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
public class LimitRuleServiceImpl implements LimitRuleService {

    private final LimitRuleRepository limitRuleRepository;
    private final LimitRuleMapper limitRuleMapper;

    @Override
    public List<LimitRuleDto> queryAll(LimitRuleQueryCriteria criteria) {
        List<LimitRule> limitRules = limitRuleRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        return limitRuleMapper.toDto(limitRules);
    }

    @Override
    public Object queryAll(LimitRuleQueryCriteria criteria, Pageable pageable) {
        return PageUtil.toPage(limitRuleRepository.findAll((root, query, criteriaBuilder) -> criteriaBuilder.conjunction(), pageable), limitRuleMapper);
    }

    @Override
    public LimitRuleDto findById(Long id) {
        LimitRule limitRule = limitRuleRepository.findById(id).orElseGet(LimitRule::new);
        return limitRuleMapper.toDto(limitRule);
    }

    @Override
    public LimitRuleDto findByUri(String uri) {
        LimitRule limitRule = limitRuleRepository.findByUri(uri);
        return limitRuleMapper.toDto(limitRule);
    }

    @Override
    public void updateByUri(String uri, LimitRule resources) {
        LimitRule limitRule = limitRuleRepository.findByUri(uri);
        limitRule.setPeriod(resources.getPeriod());
        limitRule.setCount(resources.getCount());
        limitRule.setDescription(resources.getDescription());
        limitRuleRepository.save(limitRule);
    }

    @Override
    public void create(LimitRule resources) {
        limitRuleRepository.save(resources);
    }

    @Override
    public void update(LimitRule resources) {
        LimitRule limitRule = limitRuleRepository.findById(resources.getId()).orElseGet(LimitRule::new);
        limitRule.setUri(resources.getUri());
        limitRule.setPeriod(resources.getPeriod());
        limitRule.setCount(resources.getCount());
        limitRule.setDescription(resources.getDescription());
        limitRuleRepository.save(limitRule);
    }

    @Override
    public void delete(Long id) {
        limitRuleRepository.deleteById(id);
    }
}
