package com.example.backend.dto;

import com.example.backend.common.Utils.NumberUtils;
import com.example.backend.common.Utils.StringUtils;
import com.example.backend.entity.Privilege;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PrivilegeDTO {

    private String id;
    private Integer roleId;
    private String userId;
    private String module;
    private String type;

    public static PrivilegeDTO fromEntity(Privilege privilege) {
        if (privilege == null) {
            return null;
        }
        PrivilegeDTO privilegeDTO = new PrivilegeDTO();
        BeanUtils.copyProperties(privilege, privilegeDTO);
        privilegeDTO.setId(StringUtils.toNullableString(privilege.getId()));
        privilegeDTO.setUserId(StringUtils.toNullableString(privilege.getUserId()));
        return privilegeDTO;
    }

    public static List<PrivilegeDTO> fromEntity(List<Privilege> privilegeList) {
        return privilegeList.stream().map(PrivilegeDTO::fromEntity).collect(Collectors.toList());
    }

    public static Privilege toEntity(PrivilegeDTO privilegeDTO) {
        if (privilegeDTO == null) {
            return null;
        }
        Privilege privilege = new Privilege();
        BeanUtils.copyProperties(privilegeDTO, privilege);
        privilege.setId(NumberUtils.parseLong(privilegeDTO.getId()));
        privilege.setUserId(NumberUtils.parseLong(privilegeDTO.getUserId()));
        return privilege;
    }
}
