package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.LogDto;
import com.example.backend.modules.system.model.entity.Log;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ConvertHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogConverter {

    /**
     * entity -> dto
     *
     * @param log entity
     * @return dto
     */
    LogDto toDto(Log log);

    List<LogDto> toDto(List<Log> log);

    /**
     * dto -> entity
     *
     * @param logDto dto
     * @return entity
     */
    Log toEntity(LogDto logDto);

    List<Log> toEntity(List<LogDto> logDto);

}
