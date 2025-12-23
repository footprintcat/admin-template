package com.example.backend.common.ManageList.pojo.queryconfig.base;

import com.alibaba.fastjson2.annotation.JSONField;

/**
 * 查询配置接口
 * 定义所有查询配置类应遵循的接口
 */
public interface QueryConfig {

    /**
     * 验证配置是否有效
     *
     * @return 如果配置有效返回true，否则返回false
     */
    @JSONField(serialize = false)
    boolean getIsValid();
}