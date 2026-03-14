package com.example.backend.common.ManageList.pojo.queryconfig.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    boolean getIsValid();
}
