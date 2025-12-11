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
package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.modules.system.domain.LimitRule;
import me.zhengjie.modules.system.service.LimitRuleService;
import me.zhengjie.modules.system.service.dto.LimitRuleDto;
import me.zhengjie.modules.system.service.dto.LimitRuleQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author /
 */
@RestController
@Api(tags = "系统：限流规则管理")
@RequestMapping("/api/limitRule")
public class LimitRuleController {

    @Resource
    private LimitRuleService limitRuleService;

    @ApiOperation("查询限流规则")
    @GetMapping
    public ResponseEntity<Object> query(LimitRuleQueryCriteria criteria, Pageable pageable) {
        return new ResponseEntity<>(limitRuleService.queryAll(criteria, pageable), HttpStatus.OK);
    }

    @ApiOperation("根据URI查询限流规则")
    @GetMapping("/uri/{uri}")
    public ResponseEntity<LimitRuleDto> findByUri(@PathVariable String uri) {
        return new ResponseEntity<>(limitRuleService.findByUri(uri), HttpStatus.OK);
    }

    @ApiOperation("新增限流规则")
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody LimitRule resources) {
        limitRuleService.create(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation("修改限流规则")
    @PutMapping
    public ResponseEntity<Object> update(@RequestBody LimitRule resources) {
        limitRuleService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("根据URI修改限流规则")
    @PutMapping("/uri/{uri}")
    public ResponseEntity<Object> updateByUri(@PathVariable String uri, @RequestBody LimitRule resources) {
        limitRuleService.updateByUri(uri, resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation("删除限流规则")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        limitRuleService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
