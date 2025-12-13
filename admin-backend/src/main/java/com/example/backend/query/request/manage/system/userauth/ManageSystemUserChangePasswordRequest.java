package com.example.backend.query.request.manage.system.userauth;

import com.example.backend.query.request.manage.BaseManageQueryRequest;
import lombok.Data;

@Data
public class ManageSystemUserChangePasswordRequest extends BaseManageQueryRequest {

    private String oldPassword;
    private String newPassword;

}
