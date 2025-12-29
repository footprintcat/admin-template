package com.example.backend.common.baseobject.db;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OrderByItem {

    // 字段名
    private String field;

    // 排序方向：ASC/DESC
    private Boolean isDesc;

}
