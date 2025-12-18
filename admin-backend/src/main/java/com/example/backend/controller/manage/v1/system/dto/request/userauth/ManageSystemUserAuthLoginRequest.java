package com.example.backend.controller.manage.v1.system.dto.request.userauth;

import com.example.backend.common.baseobject.request.BaseManageQueryRequest;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ManageSystemUserAuthLoginRequest extends BaseManageQueryRequest {

    @NotEmpty(message = "用户名不能为空")
    private String username;
    @NotEmpty(message = "密码不能为空")
    private String password;

}
