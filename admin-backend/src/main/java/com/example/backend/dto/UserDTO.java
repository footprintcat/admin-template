package com.example.backend.dto;

import com.example.backend.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String username;
    private String nickname;
    private Integer roleId;


    public static UserDTO fromEntity(User entity) {
        if (entity == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    public static List<UserDTO> fromEntity(List<User> entityList) {
        return entityList.stream().map(UserDTO::fromEntity).collect(Collectors.toList());
    }

    public static User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
}
