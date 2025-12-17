package com.example.backend.controller.manage.v1.system.dto.request.user;

import com.example.backend.modules.system.model.dto.UserDto;
import com.example.backend.common.baseobject.request.BaseManagePaginationQueryRequest;
import lombok.Data;

@Data
public class ManageSystemUserListRequest extends BaseManagePaginationQueryRequest {

    private UserDto params;

}
