package com.example.backend.dto;

import com.example.backend.entity.User;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserDTO {
    Integer id;
    String username;

    public static UserDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static List<UserDTO> fromEntity(List<User> userList) {
        List<UserDTO> collect = userList.stream().map(user -> fromEntity(user)).collect(Collectors.toList());
        return collect;
    }
}