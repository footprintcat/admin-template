package com.example.backend.modules.system.model.dto;

import com.example.backend.modules.system.model.entity.SystemRole;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SystemRoleDto {

    private Long id;
    private Long parentRoleId;
    private Integer level;
    private String roleName;
    private List<String> privileges;
    private List<String> inheritPrivileges;
    private List<SystemRoleDto> children;

    public static SystemRoleDto fromEntity(SystemRole systemRole) {
        if (systemRole == null) {
            return null;
        }
        SystemRoleDto systemRoleDTO = new SystemRoleDto();
        BeanUtils.copyProperties(systemRole, systemRoleDTO);
        systemRoleDTO.setPrivileges(new ArrayList<>());
        systemRoleDTO.setInheritPrivileges(new ArrayList<>());
        systemRoleDTO.setChildren(new ArrayList<>());
        return systemRoleDTO;
    }

    public static List<SystemRoleDto> fromEntity(List<SystemRole> systemRoleList) {
        return systemRoleList.stream().map(SystemRoleDto::fromEntity).collect(Collectors.toList());
    }

    public static SystemRole toEntity(SystemRoleDto systemRoleDTO) {
        if (systemRoleDTO == null) {
            return null;
        }
        SystemRole systemRole = new SystemRole();
        BeanUtils.copyProperties(systemRoleDTO, systemRole);
        return systemRole;
    }
}
