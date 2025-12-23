package com.example.backend.modules.system.model.dto;

import com.example.backend.modules.system.model.entity.Role;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoleDto {

    private Long id;
    private Long parentRoleId;
    private Integer level;
    private String roleName;
    private List<String> privileges;
    private List<String> inheritPrivileges;
    private List<RoleDto> children;

    public static RoleDto fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        RoleDto roleDTO = new RoleDto();
        BeanUtils.copyProperties(role, roleDTO);
        roleDTO.setPrivileges(new ArrayList<>());
        roleDTO.setInheritPrivileges(new ArrayList<>());
        roleDTO.setChildren(new ArrayList<>());
        return roleDTO;
    }

    public static List<RoleDto> fromEntity(List<Role> roleList) {
        return roleList.stream().map(RoleDto::fromEntity).collect(Collectors.toList());
    }

    public static Role toEntity(RoleDto roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return role;
    }
}
