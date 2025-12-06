package com.example.backend.dto;

import com.example.backend.entity.SystemUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SystemUserDTO {

    private Long id;
    private String username;
    private String nickname;
    private Integer roleId;
    private String telephone;
    private String status;

    public static SystemUserDTO fromEntity(SystemUser entity) {
        if (entity == null) {
            return null;
        }
        SystemUserDTO dto = new SystemUserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static List<SystemUserDTO> fromEntity(List<SystemUser> entityList) {
        return entityList.stream().map(SystemUserDTO::fromEntity).collect(Collectors.toList());
    }

    public static SystemUser toEntity(SystemUserDTO dto) {
        if (dto == null) {
            return null;
        }
        SystemUser entity = new SystemUser();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
