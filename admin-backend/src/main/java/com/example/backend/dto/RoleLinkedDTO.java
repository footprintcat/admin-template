package com.example.backend.dto;

import com.example.backend.entity.SystemRole;
import lombok.Data;

@Data
public class RoleLinkedDTO {
    public Long id;
    public String roleName;
    public Long parentRoleId;
    public RoleLinkedDTO parentRole;

    public static RoleLinkedDTO createRoleLinkedDTO(SystemRole systemRole) {
        if (systemRole == null) {
            return null;
        }
        RoleLinkedDTO dto = new RoleLinkedDTO();
        dto.id = systemRole.getId();
        dto.roleName = systemRole.getRoleName();
        dto.parentRoleId = systemRole.getParentRoleId();
        return dto;
    }
}
