package com.example.backend.controller.manage.v1.system.dto.response.identity;

import com.example.backend.modules.system.model.dto.IdentityDto;
import com.example.backend.modules.system.model.dto.MenuDto;
import lombok.Data;

import java.util.List;

@Data
public class ManageSystemIdentityInfoResponse {

    private List<IdentityDto> identityList;
    private IdentityDto currentIdentity;
    private List<MenuDto> menuList;

}
