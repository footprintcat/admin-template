package com.example.backend.common.baseobject.request;

import lombok.Getter;

@Getter
public class PageQuery {

    /**
     * 第几页
     */
    private int pageIndex = 1;

    /**
     * 每页几条数据
     */
    private int pageSize = 10;

    /**
     * 每页最多查询出多少条数据
     */
    private final int maxPageSize = 1000;

    public void setPageIndex(int page) {
        if (page < 1) {
            page = 1;
        }
        this.pageIndex = page;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < 1) {
            pageSize = 1;
        }
        if (pageSize > maxPageSize) {
            pageSize = maxPageSize;
        }
        this.pageSize = pageSize;
    }
}
