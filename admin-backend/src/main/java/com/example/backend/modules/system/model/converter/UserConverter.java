package com.example.backend.modules.system.model.converter;

import com.example.backend.common.mapstruct.ConvertHelper;
import com.example.backend.modules.system.model.dto.UserDto;
import com.example.backend.modules.system.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ConvertHelper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserConverter {

    /**
     * entity -> dto
     *
     * @param user entity
     * @return dto
     */
    UserDto toDto(User user);

    List<UserDto> toDto(List<User> user);

    /**
     * dto -> entity
     *
     * @param userDto dto
     * @return entity
     */
    User toEntity(UserDto userDto);

    List<User> toEntity(List<UserDto> userDto);

}
