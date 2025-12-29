package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.RoleDto;
import com.example.backend.modules.system.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

// 使用 @Autowired 注入 ConvertHelper (ConvertHelper 上需有 @Component 注解)
@Mapper(componentModel = "spring", uses = {ConvertHelper.class})
public interface RoleConverter {

    RoleConverter INSTANCE = Mappers.getMapper(RoleConverter.class);

    /**
     * entity -> dto
     *
     * @param role entity
     * @return dto
     */
    RoleDto toDto(Role role);

    List<RoleDto> toDto(List<Role> role);

    /**
     * dto -> entity
     *
     * @param roleDto dto
     * @return entity
     */
    Role toEntity(RoleDto roleDto);

    List<Role> toEntity(List<RoleDto> roleDto);

}
