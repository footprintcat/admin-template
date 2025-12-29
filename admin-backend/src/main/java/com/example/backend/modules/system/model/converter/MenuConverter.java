package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.MenuDto;
import com.example.backend.modules.system.model.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ConvertHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuConverter {

    /**
     * entity -> dto
     *
     * @param menu entity
     * @return dto
     */
    MenuDto toDto(Menu menu);

    List<MenuDto> toDto(List<Menu> menu);

    /**
     * dto -> entity
     *
     * @param menuDto dto
     * @return entity
     */
    Menu toEntity(MenuDto menuDto);

    List<Menu> toEntity(List<MenuDto> menuDto);

}
