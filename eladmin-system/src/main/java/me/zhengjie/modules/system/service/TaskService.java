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
package me.zhengjie.modules.system.service;

import me.zhengjie.utils.PageResult;
import me.zhengjie.modules.system.domain.Task;
import me.zhengjie.modules.system.service.dto.TaskDto;
import me.zhengjie.modules.system.service.dto.TaskQueryCriteria;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 任务服务接口
 * @author Zheng Jie
 * @date 2025-12-24
 */
public interface TaskService {

    /**
     * 查询所有任务
     * @param criteria 查询条件
     * @param pageable 分页参数
     * @return 分页结果
     */
    PageResult<TaskDto> queryAll(TaskQueryCriteria criteria, Pageable pageable);

    /**
     * 查询所有任务（不分页）
     * @param criteria 查询条件
     * @return 任务列表
     */
    List<TaskDto> queryAll(TaskQueryCriteria criteria);

    /**
     * 根据ID查询任务
     * @param id 任务ID
     * @return 任务DTO
     */
    TaskDto findById(Long id);

    /**
     * 创建导出任务
     * @param entityType 实体类型
     * @param params 查询参数
     * @return 任务ID
     */
    Long createExportTask(String entityType, Map<String, Object> params);

    /**
     * 创建导入任务
     * @param entityType 实体类型
     * @param fileId 文件ID
     * @param params 导入参数
     * @return 任务ID
     */
    Long createImportTask(String entityType, Long fileId, Map<String, Object> params);

    /**
     * 执行任务
     * @param task 任务
     */
    void executeTask(Task task);

    /**
     * 重试任务
     * @param id 任务ID
     */
    void retryTask(Long id);

    /**
     * 取消任务
     * @param id 任务ID
     */
    void cancelTask(Long id);

    /**
     * 删除任务
     * @param id 任务ID
     */
    void delete(Long id);

    /**
     * 下载任务结果
     * @param id 任务ID
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    void download(Long id, HttpServletResponse response) throws IOException;

    /**
     * 导出任务数据
     * @param tasks 任务列表
     * @param response HTTP响应
     * @throws IOException IO异常
     */
    void download(List<TaskDto> tasks, HttpServletResponse response) throws IOException;
}