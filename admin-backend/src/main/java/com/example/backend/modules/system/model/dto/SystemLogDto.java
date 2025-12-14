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
public class SystemLogDto {

    private String id;
    private Long createTimestamp;
    private String action;
    private String userId;
    private String ip;
    private String title;
    private String content;

    public static SystemLogDto fromEntity(Log log) {
        if (log == null) {
            return null;
        }
        SystemLogDto systemLogDTO = new SystemLogDto();
        BeanUtils.copyProperties(log, systemLogDTO);
        // if (systemLog.getLogTime() != null) {
        //     systemLogDTO.setCreateTimestamp(systemLog.getLogTime().getTime());
        // }
        // systemLogDTO.setUserId(StringUtils.toNullableString(systemLog.getUserId()));
        systemLogDTO.setId(StringUtils.toNullableString(log.getId()));
        return systemLogDTO;
    }

    public static List<SystemLogDto> fromEntity(List<Log> logList) {
        return logList.stream().map(SystemLogDto::fromEntity).collect(Collectors.toList());
    }

    public static Log toEntity(SystemLogDto systemLogDTO) {
        if (systemLogDTO == null) {
            return null;
        }
        Log log = new Log();
        BeanUtils.copyProperties(systemLogDTO, log);
        if (systemLogDTO.getCreateTimestamp() != null) {
            Date date = new Date(systemLogDTO.getCreateTimestamp());
            // systemLog.setLogTime(date);
        }

        // systemLog.setUserId(NumberUtils.parseLong(systemLogDTO.getUserId()));
        log.setId(NumberUtils.parseLong(systemLogDTO.getId()));

        return log;
    }
}
