package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.entity.Identity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ConvertHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IdentityConverter {

    /**
     * entity -> dto
     *
     * @param identity entity
     * @return dto
     */
    IdentityDto toDto(Identity identity);

    List<IdentityDto> toDto(List<Identity> identity);

    /**
     * dto -> entity
     *
     * @param identityDto dto
     * @return entity
     */
    Identity toEntity(IdentityDto identityDto);

    List<Identity> toEntity(List<IdentityDto> identityDto);

}
