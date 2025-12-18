package com.example.backend.common.PageTable.builder;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.backend.common.PageTable.enums.AddType;
import com.example.backend.common.PageTable.enums.EditType;
import com.example.backend.common.PageTable.enums.FieldType;
import com.example.backend.common.PageTable.enums.SearchType;
import com.example.backend.common.utils.StringUtils;

import java.util.Map;
import java.util.Objects;

public class FieldBuilder {

    public final static String SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME = "<SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME>";

    public final static String EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER = "<EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER>";

    public final static String EDIT_SMALL_TEXT_HTML_SAME_AS_ADD_PLACEHOLDER = "<EDIT_SMALL_TEXT_HTML_SAME_AS_ADD_PLACEHOLDER>";

    private JSONArray columns;

    public static FieldBuilder create() {
        FieldBuilder builder = new FieldBuilder();
        builder.columns = new JSONArray();
        return builder;
    }

    /**
     * @param field                对应 POJO 中的属性名称
     *                             用于新增/修改弹窗
     * @param prop                 显示的字段名 如果需要翻译（例如roleId->roleName）则填写翻译后的字段
     *                             用于渲染表格时指定显示列
     * @param fieldName            列的显示名称
     * @param defaultValue         新增弹窗中的默认值
     * @param fieldType            表格中该列的展示形式(以及是否展示该列)
     * @param searchType           该筛选字段显示为什么类型
     * @param addType              新增弹窗中该字段显示为什么类型
     * @param editType             修改弹窗中该字段显示为什么类型
     * @param searchPlaceholder    搜索的placeholder
     *                             如果为 null 则使用 fieldName
     * @param addPlaceholder       新增弹窗中该字段 Placeholder
     * @param editPlaceholder      修改弹窗中该字段 Placeholder
     * @param fieldRuleListBuilder 提交时的表单验证
     * @param mockDataPattern      mock数据正则 <a href="http://mockjs.com/examples.html">文档</a>
     * @return
     */
    public FieldBuilder add(String field, String prop, String fieldName, Object defaultValue,
                            FieldType fieldType, SearchType searchType, AddType addType, EditType editType,
                            String searchPlaceholder, String addPlaceholder, String editPlaceholder,
                            FieldRuleListBuilder fieldRuleListBuilder, String mockDataPattern) {
        return add(field, prop, fieldName, defaultValue,
                fieldType, searchType, addType, editType,
                searchPlaceholder, addPlaceholder, editPlaceholder,
                StringUtils.EMPTY_STRING, StringUtils.EMPTY_STRING, null,
                fieldRuleListBuilder, mockDataPattern);
    }

    /**
     * @param field                对应 POJO 中的属性名称
     *                             用于新增/修改弹窗
     * @param prop                 显示的字段名 如果需要翻译（例如roleId->roleName）则填写翻译后的字段
     *                             用于渲染表格时指定显示列
     * @param fieldName            列的显示名称
     * @param defaultValue         新增弹窗中的默认值
     * @param fieldType            表格中该列的展示形式(以及是否展示该列)
     * @param searchType           该筛选字段显示为什么类型
     * @param addType              新增弹窗中该字段显示为什么类型
     * @param editType             修改弹窗中该字段显示为什么类型
     * @param searchPlaceholder    搜索的placeholder
     *                             如果为 null 则使用 fieldName
     * @param addPlaceholder       新增弹窗中该字段 Placeholder
     * @param editPlaceholder      修改弹窗中该字段 Placeholder
     * @param addSmallTextHTML     新增弹窗输入框下方小字
     * @param editSmallTextHTML    修改弹窗输入框下方小字
     * @param fieldRuleListBuilder 提交时的表单验证
     * @param mockDataPattern      mock数据正则 <a href="http://mockjs.com/examples.html">文档</a>
     * @return
     */
    public FieldBuilder add(String field, String prop, String fieldName, Object defaultValue,
                            FieldType fieldType, SearchType searchType, AddType addType, EditType editType,
                            String searchPlaceholder, String addPlaceholder, String editPlaceholder,
                            String addSmallTextHTML, String editSmallTextHTML, Map<String, Object> extraConfig,
                            FieldRuleListBuilder fieldRuleListBuilder, String mockDataPattern) {
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

        /*  筛选  */
        // 上方筛选条件
        jsonObject.put("searchType", searchType.getValue());
        jsonObject.put("searchPlaceholder", SEARCH_PLACEHOLDER_SAME_AS_FIELDNAME.equals(searchPlaceholder) ? fieldName : searchPlaceholder);

        /*  弹窗  */
        // 新增弹窗
        jsonObject.put("addType", addType.getValue());
        jsonObject.put("addPlaceholder", addPlaceholder);
        jsonObject.put("addSmallTextHTML", addSmallTextHTML);
        // 修改弹窗
        jsonObject.put("editType", editType.getValue());
        jsonObject.put("editPlaceholder", EDIT_PLACEHOLDER_SAME_AS_ADD_PLACEHOLDER.equals(editPlaceholder) ? addPlaceholder : editPlaceholder);
        jsonObject.put("editSmallTextHTML", EDIT_SMALL_TEXT_HTML_SAME_AS_ADD_PLACEHOLDER.equals(editSmallTextHTML) ? addSmallTextHTML : editSmallTextHTML);
        // 新增/修改时的前端表单验证
        jsonObject.put("validateRules", Objects.nonNull(fieldRuleListBuilder)
                ? fieldRuleListBuilder.build() : new JSONArray());
        // 新增弹窗 字段默认值
        jsonObject.put("default", defaultValue);

        /* 字段额外配置 */
        if (extraConfig != null) {
            jsonObject.put("extraConfig", extraConfig); // 可能为 null
        } else {
            if (addType == AddType.INPUT_AUTO_COMPLETE || editType == EditType.INPUT_AUTO_COMPLETE) {
                throw new RuntimeException("INPUT_AUTO_COMPLETE 字段需要配置 extraConfig -> autoCompleteDropDown 属性");
            }
        }

        /* 随机填充测试数据 */
        jsonObject.put("mockRegex", mockDataPattern);

        columns.add(jsonObject);
        return this;
    }

    public JSONArray build() {
        return columns;
    }
}
