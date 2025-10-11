package com.example.backend.common.ManageList.pojo.queryconfig;

import com.example.backend.common.ManageList.pojo.queryconfig.base.BaseQueryConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 数字范围查询配置类
 * 用于配置数字范围类型查询条件的额外选项
 */
@Data
@Accessors(chain = true)
public class NumberRangeQueryConfig extends BaseQueryConfig<NumberRangeQueryConfig> {
    // 显示类型：slider 滑块等
    private String showType;

    @Override
    public boolean getIsValid() {
        // 如果指定了显示类型，检查是否为有效值
        if (showType != null && !showType.equals("slider")) {
            return false;
        }
        return true;
    }

    @Override
    protected NumberRangeQueryConfig self() {
        return this; // 返回当前子类实例
    }
}