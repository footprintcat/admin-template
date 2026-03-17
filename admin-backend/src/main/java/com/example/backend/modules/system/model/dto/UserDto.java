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

}
