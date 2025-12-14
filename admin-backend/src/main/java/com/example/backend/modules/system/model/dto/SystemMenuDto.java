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
public class SystemMenuDto implements Serializable {

    private String id;
    private String parentId;
    private Integer level;
    private String menuId;
    private String menuFullName;
    private String menuName;
    private Integer sequence;
    private Integer isHide;
    private List<SystemMenuDto> children;

    public static SystemMenuDto fromEntity(Menu menu) {
        if (menu == null) {
            return null;
        }
        SystemMenuDto systemMenuDTO = new SystemMenuDto();
        BeanUtils.copyProperties(menu, systemMenuDTO);
        systemMenuDTO.setId(StringUtils.toNullableString(menu.getId()));
        systemMenuDTO.setParentId(StringUtils.toNullableString(menu.getParentId()));
        return systemMenuDTO;
    }

    public static List<SystemMenuDto> fromEntity(List<Menu> menuList) {
        return menuList.stream().map(SystemMenuDto::fromEntity).collect(Collectors.toList());
    }

    public static Menu toEntity(SystemMenuDto systemMenuDTO) {
        if (systemMenuDTO == null) {
            return null;
        }
        Menu menu = new Menu();
        BeanUtils.copyProperties(systemMenuDTO, menu);
        menu.setId(NumberUtils.parseLong(systemMenuDTO.getId()));
        menu.setParentId(NumberUtils.parseLong(systemMenuDTO.getParentId()));
        return menu;
    }

}
