package com.example.backend.controller.manage.v1.system.dto.request.userauth;

import com.example.backend.common.baseobject.request.BaseManageQueryRequest;
import lombok.Data;

@Data
public class ManageSystemUserAuthLoginRequest extends BaseManageQueryRequest {

    private String username;
    private String password;

}
