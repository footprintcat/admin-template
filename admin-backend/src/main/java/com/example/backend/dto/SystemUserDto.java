package com.example.backend.dto;

import com.example.backend.entity.SystemUser;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SystemUserDto {

    private Long id;
    private String username;
    private String nickname;
    private Long roleId;
    private String telephone;
    private String status;

    public static SystemUserDto fromEntity(SystemUser entity) {
        if (entity == null) {
            return null;
        }
        SystemUserDto dto = new SystemUserDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static List<SystemUserDto> fromEntity(List<SystemUser> entityList) {
        return entityList.stream().map(SystemUserDto::fromEntity).collect(Collectors.toList());
    }

    public static SystemUser toEntity(SystemUserDto dto) {
        if (dto == null) {
            return null;
        }
        SystemUser entity = new SystemUser();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
