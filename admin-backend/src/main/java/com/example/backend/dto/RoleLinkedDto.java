package com.example.backend.dto;

import com.example.backend.entity.SystemRole;
import lombok.Data;

@Data
public class RoleLinkedDto {
    public Long id;
    public String roleName;
    public Long parentId;
    public RoleLinkedDto parentRole;

    public static RoleLinkedDto createRoleLinkedDTO(SystemRole systemRole) {
        if (systemRole == null) {
            return null;
        }
        RoleLinkedDto dto = new RoleLinkedDto();
        dto.id = systemRole.getId();
        dto.roleName = systemRole.getRoleName();
        dto.parentId = systemRole.getParentId();
        return dto;
    }
}
