package com.example.backend.service.System;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.dto.SystemLogDto;
import com.example.backend.entity.SystemLog;
import com.example.backend.mapper.SystemLogMapper;
import com.example.backend.query.PageQuery;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemLogService {

    @Resource
    private SystemLogMapper systemLogMapper;

    private QueryWrapper<SystemLog> getSelectQueryWrapper(SystemLogDto systemLogDTO) {
        throw new RuntimeException("暂未实现");
        // QueryWrapper<SystemLog> systemLogQueryWrapper = new QueryWrapper<>();
        // systemLogQueryWrapper.lambda()
        //         .like(StringUtils.isNotEmpty(systemLogDTO.getAction()), SystemLog::getAction, systemLogDTO.getAction())
        //         .eq(StringUtils.isNotEmpty(systemLogDTO.getUserId()), SystemLog::getUserId, systemLogDTO.getUserId())
        //         .like(StringUtils.isNotEmpty(systemLogDTO.getIp()), SystemLog::getIp, systemLogDTO.getIp())
        //         .like(StringUtils.isNotEmpty(systemLogDTO.getTitle()), SystemLog::getTitle, systemLogDTO.getTitle())
        //         .like(StringUtils.isNotEmpty(systemLogDTO.getContent()), SystemLog::getContent, systemLogDTO.getContent());
        // systemLogQueryWrapper.orderByDesc("create_time");
        //
        // return systemLogQueryWrapper;
    }

    public Page<SystemLog> getSystemLogPage(PageQuery pageQuery, @NotNull SystemLogDto systemLogDTO) {
        Page<SystemLog> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        QueryWrapper<SystemLog> selectQueryWrapper = getSelectQueryWrapper(systemLogDTO);
        return systemLogMapper.selectPage(page, selectQueryWrapper);
    }

    public List<SystemLog> getSystemLogList(@NotNull SystemLogDto systemLogDTO) {
        QueryWrapper<SystemLog> selectQueryWrapper = getSelectQueryWrapper(systemLogDTO);
        return systemLogMapper.selectList(selectQueryWrapper);
    }

    /**
     * 记录日志
     * systemLog.getEventType() 不可为空
     *
     * @param systemLog
     */
    public void add(SystemLog systemLog) {
        if (systemLog == null) {
            return;
        }
        systemLog.setId(null);
        systemLogMapper.insert(systemLog);
    }
}
