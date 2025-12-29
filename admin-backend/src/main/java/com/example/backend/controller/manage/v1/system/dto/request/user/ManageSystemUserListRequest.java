package com.example.backend.controller.manage.v1.system.dto.request.user;

import com.example.backend.common.baseobject.request.BaseManagePaginationQueryRequest;
import com.example.backend.common.baseobject.request.SortColumnRequestItem;
import com.example.backend.modules.system.model.dto.UserDto;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;

@Data
public class ManageSystemUserListRequest extends BaseManagePaginationQueryRequest {

    private UserDto params;

    @Valid
    private List<SortColumnRequestItem> sort;

}
