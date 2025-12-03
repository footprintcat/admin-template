package com.example.backend.query.request.manage.system.user;

import com.example.backend.dto.SystemUserDTO;
import com.example.backend.query.request.manage.BaseManageQueryRequest;
import lombok.Data;

@Data
public class ManageUserListRequest extends BaseManageQueryRequest {

    private SystemUserDTO params;

}
