package com.example.backend.modules.system.model.dto;

import com.example.backend.common.utils.NumberUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.model.entity.Privilege;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SystemPrivilegeDto {

    private String id;
    private Long roleId;
    private String userId;
    private String module;
    private String type;

    public static SystemPrivilegeDto fromEntity(Privilege privilege) {
        if (privilege == null) {
            return null;
        }
        SystemPrivilegeDto systemPrivilegeDTO = new SystemPrivilegeDto();
        BeanUtils.copyProperties(privilege, systemPrivilegeDTO);
        systemPrivilegeDTO.setId(StringUtils.toNullableString(privilege.getId()));
        systemPrivilegeDTO.setUserId(StringUtils.toNullableString(privilege.getUserId()));
        return systemPrivilegeDTO;
    }

    public static List<SystemPrivilegeDto> fromEntity(List<Privilege> privilegeList) {
        return privilegeList.stream().map(SystemPrivilegeDto::fromEntity).collect(Collectors.toList());
    }

    public static Privilege toEntity(SystemPrivilegeDto systemPrivilegeDTO) {
        if (systemPrivilegeDTO == null) {
            return null;
        }
        Privilege privilege = new Privilege();
        BeanUtils.copyProperties(systemPrivilegeDTO, privilege);
        privilege.setId(NumberUtils.parseLong(systemPrivilegeDTO.getId()));
        privilege.setUserId(NumberUtils.parseLong(systemPrivilegeDTO.getUserId()));
        return privilege;
    }
}
