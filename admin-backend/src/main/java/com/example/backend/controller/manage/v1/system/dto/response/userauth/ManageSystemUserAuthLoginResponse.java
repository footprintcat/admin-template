package com.example.backend.controller.manage.v1.system.dto.response.userauth;

import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.dto.UserDto;
import lombok.Data;

import java.util.List;

@Data
public class ManageSystemUserAuthLoginResponse {

    private UserDto userInfo;
    private List<IdentityDto> identityList;

}
