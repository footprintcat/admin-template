package com.example.backend.modules.system.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDto {

    private Long id;
    private Long parentRoleId;
    private Integer level;
    private String roleName;
    private List<String> privileges;
    private List<String> inheritPrivileges;
    private List<RoleDto> children;

}
