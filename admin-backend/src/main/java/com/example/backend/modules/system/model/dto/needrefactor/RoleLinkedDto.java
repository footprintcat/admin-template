package com.example.backend.modules.system.model.dto.needrefactor;

import com.example.backend.modules.system.model.entity.Role;
import lombok.Data;

@Data
public class RoleLinkedDto {
    public Long id;
    public String roleName;
    public Long parentId;
    public RoleLinkedDto parentRole;

    public static RoleLinkedDto createRoleLinkedDTO(Role role) {
        if (role == null) {
            return null;
        }
        RoleLinkedDto dto = new RoleLinkedDto();
        dto.id = role.getId();
        dto.roleName = role.getRoleName();
        dto.parentId = role.getParentId();
        return dto;
    }
}
