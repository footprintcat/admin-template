package com.example.backend.query;

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
        if (pageSize > 1000) {
            pageSize = 1000;
        }
        this.pageSize = pageSize;
    }
}
