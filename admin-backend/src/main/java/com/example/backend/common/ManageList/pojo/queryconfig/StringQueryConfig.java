package com.example.backend.common.ManageList.pojo.queryconfig;

import com.example.backend.common.ManageList.pojo.queryconfig.base.BaseQueryConfig;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 字符串查询配置
 * 用于配置字符串类型查询条件的额外选项
 */
@Data
@Accessors(chain = true)
public class StringQueryConfig extends BaseQueryConfig<StringQueryConfig> {
    // 标签选择列表 [{ code: "right", name: "对", tagType: "success" }, { code: "wrong", name: "错", tagType: "error" }]
    private List<TagConfig> selectByTag;

    /**
     * 标签配置类
     */
    @Data
    @Accessors(chain = true)
    public static class TagConfig {
        private String code;
        private String name;
        private String tagType;
    }

    @Override
    public boolean getIsValid() {
        // 如果有标签选择列表，检查每个标签配置是否有效
        if (selectByTag != null) {
            for (TagConfig tag : selectByTag) {
                if (tag == null || tag.getCode() == null || tag.getCode().isEmpty() || tag.getName() == null || tag.getName().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected StringQueryConfig self() {
        return this; // 返回当前子类实例
    }
}