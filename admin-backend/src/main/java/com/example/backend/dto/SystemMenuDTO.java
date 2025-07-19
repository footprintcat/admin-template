package com.example.backend.dto;

import com.example.backend.common.Utils.NumberUtils;
import com.example.backend.common.Utils.StringUtils;
import com.example.backend.entity.SystemMenu;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SystemMenuDTO implements Serializable {

    private String id;
    private String parentId;
    private Integer level;
    private String menuId;
    private String menuFullName;
    private String menuName;
    private Integer sequence;
    private Integer isHide;
    private List<SystemMenuDTO> children;

    public static SystemMenuDTO fromEntity(SystemMenu systemMenu) {
        if (systemMenu == null) {
            return null;
        }
        SystemMenuDTO systemMenuDTO = new SystemMenuDTO();
        BeanUtils.copyProperties(systemMenu, systemMenuDTO);
        systemMenuDTO.setId(StringUtils.toNullableString(systemMenu.getId()));
        systemMenuDTO.setParentId(StringUtils.toNullableString(systemMenu.getParentId()));
        return systemMenuDTO;
    }

    public static List<SystemMenuDTO> fromEntity(List<SystemMenu> systemMenuList) {
        return systemMenuList.stream().map(SystemMenuDTO::fromEntity).collect(Collectors.toList());
    }

    public static SystemMenu toEntity(SystemMenuDTO systemMenuDTO) {
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
