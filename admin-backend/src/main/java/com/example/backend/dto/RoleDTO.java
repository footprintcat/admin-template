package com.example.backend.dto;

import com.example.backend.entity.Role;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RoleDTO {

    private Integer id;
    private String roleName;
    private List<String> privileges;
    private List<String> inheritPrivileges;
    private Integer parentRoleId;
    private List<RoleDTO> children;

    public static RoleDTO fromEntity(Role role) {
        if (role == null) {
            return null;
        }
        RoleDTO roleDTO = new RoleDTO();
        BeanUtils.copyProperties(role, roleDTO);
        roleDTO.setPrivileges(new ArrayList<>());
        roleDTO.setInheritPrivileges(new ArrayList<>());
        roleDTO.setChildren(new ArrayList<>());
        return roleDTO;
    }

    public static List<RoleDTO> fromEntity(List<Role> roleList) {
        return roleList.stream().map(RoleDTO::fromEntity).collect(Collectors.toList());
    }

    public static Role toEntity(RoleDTO roleDTO) {
        if (roleDTO == null) {
            return null;
        }
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        return role;
    }
}
