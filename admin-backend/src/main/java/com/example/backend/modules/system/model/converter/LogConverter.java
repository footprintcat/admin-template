package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.LogDto;
import com.example.backend.modules.system.model.entity.Log;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

// 使用 @Autowired 注入 ConvertHelper (ConvertHelper 上需有 @Component 注解)
@Mapper(componentModel = "spring", uses = {ConvertHelper.class})
public interface LogConverter {

    LogConverter INSTANCE = Mappers.getMapper(LogConverter.class);

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
