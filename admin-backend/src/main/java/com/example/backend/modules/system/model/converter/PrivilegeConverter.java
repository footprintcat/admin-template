package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.PrivilegeDto;
import com.example.backend.modules.system.model.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

// 使用 @Autowired 注入 ConvertHelper (ConvertHelper 上需有 @Component 注解)
@Mapper(componentModel = "spring", uses = {ConvertHelper.class})
public interface PrivilegeConverter {

    PrivilegeConverter INSTANCE = Mappers.getMapper(PrivilegeConverter.class);

    /**
     * entity -> dto
     *
     * @param privilege entity
     * @return dto
     */
    PrivilegeDto toDto(Privilege privilege);

    List<PrivilegeDto> toDto(List<Privilege> privilege);

    /**
     * dto -> entity
     *
     * @param privilegeDto dto
     * @return entity
     */
    Privilege toEntity(PrivilegeDto privilegeDto);

    List<Privilege> toEntity(List<PrivilegeDto> privilegeDto);

}
