package com.example.backend.controller.manage.v1.system.dto.response.userauth;

import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.dto.SystemUserDto;
import lombok.Data;

import java.util.List;

@Data
public class ManageSystemUserAuthLoginResponse {

    private SystemUserDto userInfo;
    private List<IdentityDto> identityList;

}
