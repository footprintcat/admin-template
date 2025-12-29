package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.PrivilegeDto;
import com.example.backend.modules.system.model.entity.Privilege;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ConvertHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PrivilegeConverter {

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
