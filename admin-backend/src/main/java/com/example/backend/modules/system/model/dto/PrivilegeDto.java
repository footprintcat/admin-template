package com.example.backend.modules.system.model.dto;

import com.example.backend.common.utils.NumberUtils;
import com.example.backend.common.utils.StringUtils;
import com.example.backend.modules.system.model.entity.Privilege;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class PrivilegeDto {

    private String id;
    private Long roleId;
    private String module;
    private String type;

    public static PrivilegeDto fromEntity(Privilege privilege) {
        if (privilege == null) {
            return null;
        }
        PrivilegeDto privilegeDTO = new PrivilegeDto();
        BeanUtils.copyProperties(privilege, privilegeDTO);
        privilegeDTO.setId(StringUtils.toNullableString(privilege.getId()));
        return privilegeDTO;
    }

    public static List<PrivilegeDto> fromEntity(List<Privilege> privilegeList) {
        return privilegeList.stream().map(PrivilegeDto::fromEntity).collect(Collectors.toList());
    }

    public static Privilege toEntity(PrivilegeDto privilegeDTO) {
        if (privilegeDTO == null) {
            return null;
        }
        Privilege privilege = new Privilege();
        BeanUtils.copyProperties(privilegeDTO, privilege);
        privilege.setId(NumberUtils.parseLong(privilegeDTO.getId()));
        return privilege;
    }
}
