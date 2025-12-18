package com.example.backend.common.baseobject.request;

import lombok.Data;

@Data
public class BaseManagePaginationQueryRequest {

    /**
     * 分页参数
     */
    private PageQuery pageQuery;

}
