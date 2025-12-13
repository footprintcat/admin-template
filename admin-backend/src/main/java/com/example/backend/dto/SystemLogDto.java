package com.example.backend.dto;

import com.example.backend.common.Utils.NumberUtils;
import com.example.backend.common.Utils.StringUtils;
import com.example.backend.entity.SystemLog;
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

    public static SystemLogDto fromEntity(SystemLog systemLog) {
        if (systemLog == null) {
            return null;
        }
        SystemLogDto systemLogDTO = new SystemLogDto();
        BeanUtils.copyProperties(systemLog, systemLogDTO);
        // if (systemLog.getLogTime() != null) {
        //     systemLogDTO.setCreateTimestamp(systemLog.getLogTime().getTime());
        // }
        // systemLogDTO.setUserId(StringUtils.toNullableString(systemLog.getUserId()));
        systemLogDTO.setId(StringUtils.toNullableString(systemLog.getId()));
        return systemLogDTO;
    }

    public static List<SystemLogDto> fromEntity(List<SystemLog> systemLogList) {
        return systemLogList.stream().map(SystemLogDto::fromEntity).collect(Collectors.toList());
    }

    public static SystemLog toEntity(SystemLogDto systemLogDTO) {
        if (systemLogDTO == null) {
            return null;
        }
        SystemLog systemLog = new SystemLog();
        BeanUtils.copyProperties(systemLogDTO, systemLog);
        if (systemLogDTO.getCreateTimestamp() != null) {
            Date date = new Date(systemLogDTO.getCreateTimestamp());
            // systemLog.setLogTime(date);
        }

        // systemLog.setUserId(NumberUtils.parseLong(systemLogDTO.getUserId()));
        systemLog.setId(NumberUtils.parseLong(systemLogDTO.getId()));

        return systemLog;
    }
}
