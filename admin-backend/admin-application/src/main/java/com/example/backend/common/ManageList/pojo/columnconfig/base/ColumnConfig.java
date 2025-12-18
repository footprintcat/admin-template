package com.example.backend.common.ManageList.pojo.columnconfig.base;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * 列配置接口
 * 定义所有列配置类应遵循的接口
 */
public interface ColumnConfig {

    /**
     * 验证配置是否有效
     *
     * @return 如果配置有效返回true，否则返回false
     */
    @JSONField(serialize = false)
    boolean getIsValid();
}