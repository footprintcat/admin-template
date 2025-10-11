package com.example.backend.common.ManageList.pojo.queryconfig;

import com.example.backend.common.ManageList.pojo.queryconfig.base.BaseQueryConfig;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 布尔查询配置类
 * 用于配置布尔类型查询条件的额外选项
 */
@Data
@Accessors(chain = true)
public class BooleanQueryConfig extends BaseQueryConfig<BooleanQueryConfig> {
    // 显示类型：dropdown 下拉框, checkbox 复选框, radio 两个单选框, switch 开关
    private String showType = "dropdown";

    @Override
    public boolean getIsValid() {
        // 检查显示类型是否为有效值
        if (showType == null) {
            return false;
        }
        return showType.equals("dropdown") || showType.equals("checkbox") || showType.equals("radio") || showType.equals("switch");
    }

    @Override
    protected BooleanQueryConfig self() {
        return this; // 返回当前子类实例
    }
}