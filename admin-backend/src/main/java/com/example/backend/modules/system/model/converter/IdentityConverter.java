package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.entity.Identity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

// 使用 @Autowired 注入 ConvertHelper (ConvertHelper 上需有 @Component 注解)
@Mapper(componentModel = "spring", uses = {ConvertHelper.class})
public interface IdentityConverter {

    IdentityConverter INSTANCE = Mappers.getMapper(IdentityConverter.class);

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
