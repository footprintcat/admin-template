package com.example.backend.modules.system.service.needrefactor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.backend.common.baseobject.request.PageQuery;
import com.example.backend.modules.system.mapper.LogMapper;
import com.example.backend.modules.system.model.dto.LogDto;
import com.example.backend.modules.system.model.entity.Log;
import jakarta.annotation.Resource;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemLogService {

    @Resource
    private LogMapper logMapper;

    private QueryWrapper<Log> getSelectQueryWrapper(LogDto logDTO) {
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

    public Page<Log> getSystemLogPage(PageQuery pageQuery, @NotNull LogDto logDTO) {
        Page<Log> page = new Page<>(pageQuery.getPageIndex(), pageQuery.getPageSize());
        QueryWrapper<Log> selectQueryWrapper = getSelectQueryWrapper(logDTO);
        return logMapper.selectPage(page, selectQueryWrapper);
    }

    public List<Log> getSystemLogList(@NotNull LogDto logDTO) {
        QueryWrapper<Log> selectQueryWrapper = getSelectQueryWrapper(logDTO);
        return logMapper.selectList(selectQueryWrapper);
    }

    /**
     * 记录日志
     * systemLog.getEventType() 不可为空
     *
     * @param log
     */
    public void add(Log log) {
        if (log == null) {
            return;
        }
        log.setId(null);
        logMapper.insert(log);
    }
}
