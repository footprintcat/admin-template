package com.example.backend.common.baseobject.request;

import jakarta.validation.Valid;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class ManageListExportQueryRequest<T> {

    @Nullable
    private T params;

    /**
     * 数据导出范围
     */
    private String range;

    @Valid
    private List<SortColumnRequestItem> sort;

    private PageQuery pageQuery;

}
