package com.example.backend.modules.system.model.dto;

import com.example.backend.common.utils.NumberUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.model.entity.Menu;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class MenuDto implements Serializable {

    private String id;
    private String parentId;
    private Integer level;
    private String menuType;
    private String menuCode;
    private String actionCode;
    private String menuName;
    private String menuPath;
    private Integer sortOrder;
    private Integer canEdit;
    private Integer isHide;
    private List<MenuDto> children;

    public static MenuDto fromEntity(Menu menu) {
        if (menu == null) {
            return null;
        }
        MenuDto menuDTO = new MenuDto();
        BeanUtils.copyProperties(menu, menuDTO);
        menuDTO.setId(StringUtils.toNullableString(menu.getId()));
        menuDTO.setParentId(StringUtils.toNullableString(menu.getParentId()));
        return menuDTO;
    }

    public static List<MenuDto> fromEntity(List<Menu> menuList) {
        return menuList.stream().map(MenuDto::fromEntity).collect(Collectors.toList());
    }

    public static Menu toEntity(MenuDto menuDTO) {
        if (menuDTO == null) {
            return null;
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(menuDTO, menu);
        menu.setId(NumberUtils.parseLong(menuDTO.getId()));
        menu.setParentId(NumberUtils.parseLong(menuDTO.getParentId()));
        return menu;
    }

}
