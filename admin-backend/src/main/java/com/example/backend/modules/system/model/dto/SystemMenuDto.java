package com.example.backend.modules.system.model.dto;

import com.example.backend.common.utils.NumberUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.model.entity.SystemMenu;
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

    public static SystemMenuDto fromEntity(SystemMenu systemMenu) {
        if (systemMenu == null) {
            return null;
        }
        SystemMenuDto systemMenuDTO = new SystemMenuDto();
        BeanUtils.copyProperties(systemMenu, systemMenuDTO);
        systemMenuDTO.setId(StringUtils.toNullableString(systemMenu.getId()));
        systemMenuDTO.setParentId(StringUtils.toNullableString(systemMenu.getParentId()));
        return systemMenuDTO;
    }

    public static List<SystemMenuDto> fromEntity(List<SystemMenu> systemMenuList) {
        return systemMenuList.stream().map(SystemMenuDto::fromEntity).collect(Collectors.toList());
    }

    public static SystemMenu toEntity(SystemMenuDto systemMenuDTO) {
        if (systemMenuDTO == null) {
            return null;
        }
        SystemMenu systemMenu = new SystemMenu();
        BeanUtils.copyProperties(systemMenuDTO, systemMenu);
        systemMenu.setId(NumberUtils.parseLong(systemMenuDTO.getId()));
        systemMenu.setParentId(NumberUtils.parseLong(systemMenuDTO.getParentId()));
        return systemMenu;
    }

}
