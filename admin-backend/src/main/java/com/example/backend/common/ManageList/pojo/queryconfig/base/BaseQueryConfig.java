package com.example.backend.common.ManageList.pojo.queryconfig.base;

import lombok.Getter;

/**
 * 查询配置基类
 * 定义所有查询配置类共有的属性和方法
 */
@Getter
public abstract class BaseQueryConfig<T extends BaseQueryConfig<T>> implements QueryConfig {
    // 是否折叠到更多查询条件中，默认为false
    private boolean fold = false;
    // 占位符文本
    private String placeHolder;
    // 是否必填
    private boolean required = false;
    // 默认值
    private Object defaultValue;

    public T setFold(boolean fold) {
        this.fold = fold;
        return self();
    }

    public T setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
        return self();
    }

    public T setRequired(boolean required) {
        this.required = required;
        return self();
    }

    public T setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
        return self();
    }

    // 子类需实现的抽象方法
    protected abstract T self();
}