package com.example.backend.common.ManageList.pojo.columnconfig.base;

import lombok.Getter;

/**
 * 列配置基类
 * 定义所有列配置类共有的属性和方法
 */
@Getter
public abstract class BaseColumnConfig<T extends BaseColumnConfig<T>> implements ColumnConfig {

    // 列宽度
    private Integer columnWidth;
    // 是否隐藏
    private boolean hidden = false;
    // 是否可排序
    private boolean sortable = true;
    // 是否可过滤
    private boolean filterable = true;
    // 是否固定列
    private String fixed; // left, right, null
    // 是否允许编辑
    private boolean editable = false;

    public T setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
        return self();
    }

    public T setHidden(boolean hidden) {
        this.hidden = hidden;
        return self();
    }

    public T setSortable(boolean sortable) {
        this.sortable = sortable;
        return self();
    }

    public T setFilterable(boolean filterable) {
        this.filterable = filterable;
        return self();
    }

    public T setFixed(String fixed) {
        this.fixed = fixed;
        return self();
    }

    public T setEditable(boolean editable) {
        this.editable = editable;
        return self();
    }

    // 子类需实现的抽象方法
    protected abstract T self();
}