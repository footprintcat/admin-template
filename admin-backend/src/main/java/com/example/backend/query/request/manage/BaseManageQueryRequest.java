package com.example.backend.query.request.manage;

import com.example.backend.query.PageQuery;
import lombok.Data;

@Data
public class BaseManageQueryRequest {

    /**
     * 分页参数
     */
    private PageQuery pageQuery;

}
