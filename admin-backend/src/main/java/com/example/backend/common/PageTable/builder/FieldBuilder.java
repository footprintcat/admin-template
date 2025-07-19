package com.example.backend.common.PageTable.builder;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.backend.common.PageTable.enums.AddType;
import com.example.backend.common.PageTable.enums.EditType;
import com.example.backend.common.PageTable.enums.FieldType;
import com.example.backend.common.PageTable.enums.SearchType;

import java.util.Objects;

public class FieldBuilder {

    public final static String SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME = "<SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME>";

    public final static String EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER = "<EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER>";
    private JSONArray columns;

    public static FieldBuilder create() {
        FieldBuilder builder = new FieldBuilder();
        builder.columns = new JSONArray();
        return builder;
    }

    /**
     * @param field                对应 POJO 中的属性名称 <br>
     *                             用于新增/修改弹窗 <br>
     * @param prop                 显示的字段名 如果需要翻译（例如roleId->roleName）则填写翻译后的字段 <br>
     *                             用于渲染表格时指定显示列 <br>
     * @param fieldName            列的显示名称 <br>
     * @param defaultValue         新增弹窗中的默认值 <br>
     * @param fieldType            表格中该列的展示形式(以及是否展示该列) <br>
     * @param searchPlaceholder    搜索的placeholder <br>
     *                             如果为 null 则使用 fieldName <br>
     * @param addPlaceholder       新增弹窗中该字段 Placeholder <br>
     * @param editPlaceholder      修改弹窗中该字段 Placeholder <br>
     * @param searchType           该筛选字段显示为什么类型 <br>
     * @param addType              新增弹窗中该字段显示为什么类型 <br>
     * @param editType             修改弹窗中该字段显示为什么类型 <br>
     * @param fieldRuleListBuilder 提交时的表单验证 <br>
     * @return FieldBuilder
     */
    public FieldBuilder add(String field, String prop, String fieldName, Object defaultValue,
                            FieldType fieldType, SearchType searchType, AddType addType, EditType editType,
                            String searchPlaceholder, String addPlaceholder, String editPlaceholder,
                            FieldRuleListBuilder fieldRuleListBuilder, Integer columnWidth) {
        JSONObject jsonObject = new JSONObject();

        /*  实际字段  */
        // 用于筛选、增删改
        jsonObject.put("field", field);

        /*  表格数据  */
        // 展示字段
        jsonObject.put("prop", prop);
        // 表格列显示名称
        jsonObject.put("label", fieldName);
        // 表格是否展示该字段
        jsonObject.put("fieldType", fieldType.getValue());
        // 列宽
        jsonObject.put("columnWidth", columnWidth);

        /*  筛选  */
        // 上方筛选条件
        jsonObject.put("searchType", searchType.getValue());
        jsonObject.put("searchPlaceholder", SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME.equals(searchPlaceholder) ? fieldName : searchPlaceholder);

        /*  弹窗  */
        // 新增弹窗
        jsonObject.put("addType", addType.getValue());
        jsonObject.put("addPlaceholder", addPlaceholder);
        // 修改弹窗
        jsonObject.put("editType", editType.getValue());
        jsonObject.put("editPlaceholder", EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER.equals(editPlaceholder) ? addPlaceholder : editPlaceholder);
        // 新增/修改时的前端表单验证
        jsonObject.put("validateRules", Objects.nonNull(fieldRuleListBuilder)
                ? fieldRuleListBuilder.build() : new JSONArray());
        // 新增弹窗 字段默认值
        jsonObject.put("default", defaultValue);

        columns.add(jsonObject);
        return this;
    }

    public JSONArray build() {
        return columns;
    }
}
