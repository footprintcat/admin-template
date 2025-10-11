package com.example.backend.common.ManageList.pojo.queryconfig;

import com.example.backend.common.ManageList.pojo.queryconfig.base.BaseQueryConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 下拉框查询配置类
 * 用于配置下拉框类型查询条件的额外选项
 */
@Data
@Accessors(chain = true)
public class DropdownQueryConfig extends BaseQueryConfig<DropdownQueryConfig> {

    @Override
    public boolean getIsValid() {
        return true;
    }

    @Override
    protected DropdownQueryConfig self() {
        return this; // 返回当前子类实例
    }
}