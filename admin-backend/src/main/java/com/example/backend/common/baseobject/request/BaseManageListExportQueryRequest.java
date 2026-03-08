package com.example.backend.common.baseobject.request;

import lombok.Data;

@Data
public class BaseManageListExportQueryRequest {

    /**
     * 数据导出范围
     */
    private String range;

}
