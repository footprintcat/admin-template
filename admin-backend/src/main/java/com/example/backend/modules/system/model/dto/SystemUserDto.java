package com.example.backend.modules.system.model.dto;

import com.example.backend.modules.system.model.entity.User;
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

    public static SystemUserDto fromEntity(User entity) {
        if (entity == null) {
            return null;
        }
        SystemUserDto dto = new SystemUserDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static List<SystemUserDto> fromEntity(List<User> entityList) {
        return entityList.stream().map(SystemUserDto::fromEntity).collect(Collectors.toList());
    }

    public static User toEntity(SystemUserDto dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
