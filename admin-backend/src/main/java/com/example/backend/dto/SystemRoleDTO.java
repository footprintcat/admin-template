package com.example.backend.dto;

import com.example.backend.entity.SystemRole;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class SystemRoleDTO {

    private Integer id;
    private String roleName;
    private List<String> privileges;
    private List<String> inheritPrivileges;
    private Integer parentRoleId;
    private List<SystemRoleDTO> children;

    public static SystemRoleDTO fromEntity(SystemRole systemRole) {
        if (systemRole == null) {
            return null;
        }
        SystemRoleDTO systemRoleDTO = new SystemRoleDTO();
        BeanUtils.copyProperties(systemRole, systemRoleDTO);
        systemRoleDTO.setPrivileges(new ArrayList<>());
        systemRoleDTO.setInheritPrivileges(new ArrayList<>());
        systemRoleDTO.setChildren(new ArrayList<>());
        return systemRoleDTO;
    }

    public static List<SystemRoleDTO> fromEntity(List<SystemRole> systemRoleList) {
        return systemRoleList.stream().map(SystemRoleDTO::fromEntity).collect(Collectors.toList());
    }

    public static SystemRole toEntity(SystemRoleDTO systemRoleDTO) {
        if (systemRoleDTO == null) {
            return null;
        }
        SystemRole systemRole = new SystemRole();
        BeanUtils.copyProperties(systemRoleDTO, systemRole);
        return systemRole;
    }
}
