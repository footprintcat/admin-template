package com.example.backend.modules.system.model.dto;

import com.example.backend.modules.system.model.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String nickname;
    private Long roleId;
    private String telephone;
    private String status;

    public static UserDto fromEntity(User entity) {
        if (entity == null) {
            return null;
        }
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static List<UserDto> fromEntity(List<User> entityList) {
        return entityList.stream().map(UserDto::fromEntity).collect(Collectors.toList());
    }

    public static User toEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
