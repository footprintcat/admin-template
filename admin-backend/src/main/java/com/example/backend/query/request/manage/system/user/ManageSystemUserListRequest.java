package com.example.backend.query.request.manage.system.user;

import com.example.backend.dto.SystemUserDto;
import com.example.backend.query.request.manage.BaseManagePaginationQueryRequest;
import lombok.Data;

@Data
public class ManageSystemUserListRequest extends BaseManagePaginationQueryRequest {

    private SystemUserDto params;

}
