package com.example.backend.modules.system.model.dto;

import com.example.backend.common.utils.NumberUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.model.entity.Log;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LogDto {

    private String id;
    private Long createTimestamp;
    private String action;
    private String userId;
    private String ip;
    private String title;
    private String content;

    public static LogDto fromEntity(Log log) {
        if (log == null) {
            return null;
        }
        LogDto logDTO = new LogDto();
        BeanUtils.copyProperties(log, logDTO);
        // if (systemLog.getLogTime() != null) {
        //     systemLogDTO.setCreateTimestamp(systemLog.getLogTime().getTime());
        // }
        // systemLogDTO.setUserId(StringUtils.toNullableString(systemLog.getUserId()));
        logDTO.setId(StringUtils.toNullableString(log.getId()));
        return logDTO;
    }

    public static List<LogDto> fromEntity(List<Log> logList) {
        return logList.stream().map(LogDto::fromEntity).collect(Collectors.toList());
    }

    public static Log toEntity(LogDto logDTO) {
        if (logDTO == null) {
            return null;
        }
        Log log = new Log();
        BeanUtils.copyProperties(logDTO, log);
        if (logDTO.getCreateTimestamp() != null) {
            Date date = new Date(logDTO.getCreateTimestamp());
            // systemLog.setLogTime(date);
        }

        // systemLog.setUserId(NumberUtils.parseLong(systemLogDTO.getUserId()));
        log.setId(NumberUtils.parseLong(logDTO.getId()));

        return log;
    }
}
