package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.RoleDto;
import com.example.backend.modules.system.model.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ConvertHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleConverter {

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
