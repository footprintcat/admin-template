package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.MenuDto;
import com.example.backend.modules.system.model.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

// 使用 @Autowired 注入 ConvertHelper (ConvertHelper 上需有 @Component 注解)
@Mapper(componentModel = "spring", uses = {ConvertHelper.class})
public interface MenuConverter {

    MenuConverter INSTANCE = Mappers.getMapper(MenuConverter.class);

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
