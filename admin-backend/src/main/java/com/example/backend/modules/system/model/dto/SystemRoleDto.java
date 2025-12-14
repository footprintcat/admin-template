package com.example.backend.modules.system.model.dto;

import com.example.backend.modules.system.model.entity.Role;
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

    public static SystemRoleDto fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        SystemRoleDto systemRoleDTO = new SystemRoleDto();
        BeanUtils.copyProperties(role, systemRoleDTO);
        systemRoleDTO.setPrivileges(new ArrayList<>());
        systemRoleDTO.setInheritPrivileges(new ArrayList<>());
        systemRoleDTO.setChildren(new ArrayList<>());
        return systemRoleDTO;
    }

    public static List<SystemRoleDto> fromEntity(List<Role> roleList) {
        return roleList.stream().map(SystemRoleDto::fromEntity).collect(Collectors.toList());
    }

    public static Role toEntity(SystemRoleDto systemRoleDTO) {
        if (systemRoleDTO == null) {
            return null;
        }
        Role role = new Role();
        BeanUtils.copyProperties(systemRoleDTO, role);
        return role;
    }
}
