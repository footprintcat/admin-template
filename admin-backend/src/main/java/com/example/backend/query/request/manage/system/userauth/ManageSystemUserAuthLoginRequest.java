package com.example.backend.query.request.manage.system.userauth;

import com.example.backend.query.request.manage.BaseManageQueryRequest;
import lombok.Data;

@Data
public class ManageSystemUserAuthLoginRequest extends BaseManageQueryRequest {

    private String username;
    private String password;

}
