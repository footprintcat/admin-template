package com.example.backend.dto;

import com.example.backend.entity.Role;
import lombok.Data;

@Data
public class RoleLinkedDTO {
    public Integer id;
    public String roleName;
    public Integer parentRoleId;
    public RoleLinkedDTO parentRole;

    public static RoleLinkedDTO createRoleLinkedDTO(Role role) {
        if (role == null) {
            return null;
        }
        RoleLinkedDTO dto = new RoleLinkedDTO();
        dto.id = role.getId();
        dto.roleName = role.getRoleName();
        dto.parentRoleId = role.getParentRoleId();
        return dto;
    }
}
