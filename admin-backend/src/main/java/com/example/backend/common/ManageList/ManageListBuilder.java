package com.example.backend.common.ManageList;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.example.backend.common.ManageList.enums.ColumnType;
import com.example.backend.common.ManageList.enums.DateDisplayType;
import com.example.backend.common.ManageList.enums.DateValueType;
import com.example.backend.common.ManageList.pojo.ManageListColumn;
import com.example.backend.common.ManageList.pojo.ManageListInfo;
import com.example.backend.common.ManageList.pojo.ManageListPageInfo;
import com.example.backend.common.ManageList.pojo.ManageListQuery;
import com.example.backend.common.ManageList.pojo.queryconfig.StringQueryConfig;
import com.example.backend.common.config.FastjsonInitializer;
import com.example.backend.modules.system.model.entity.User;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.StopWatch;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 列表构建器
 * 用于构建管理列表页面的数据结构
 */
@Slf4j
public class ManageListBuilder<T> {

    // 页面信息
    private ManageListInfo manageListInfo;
    // 下拉框数据
    private final HashMap<String, Map<String, Object>> dropdownMap = new HashMap<>();
    // 列表数据
    private List<? extends T> pageData;
    // 表格查询配置
    private ManageListQuery tableQuery;
    // 表格列配置
    private ManageListColumn tableColumn;
    // 分页信息
    private ManageListPageInfo pageInfo;

    private ManageListBuilder() {
    }

    /**
     * 创建一个新实例
     *
     * @param clazz 用于类型推断 避免这样调用 ManageListBuilder.&lt;Entity>create()
     * @param <T>   泛型
     * @return ManageListBuilder
     */
    public static <T> ManageListBuilder<T> create(Class<T> clazz) {
        return new ManageListBuilder<>();
    }

    /**
     * 设置 ManageListInfo
     *
     * @param manageListInfo 管理列表信息
     * @return this
     */
    public ManageListBuilder<T> setInfo(@NotNull ManageListInfo manageListInfo) {
        if (this.manageListInfo != null) {
            throw new RuntimeException("You should not call this twice!");
        }
        this.manageListInfo = manageListInfo;
        return this;
    }

    /**
     * 使用函数式接口设置 ManageListInfo
     * 支持lambda表达式形式：setInfo((info) -> info.xxx)
     *
     * @param infoFunction 接收ManageListInfo并返回修改后的ManageListInfo的函数
     * @return this
     */
    public ManageListBuilder<T> setInfo(@NotNull Function<ManageListInfo, ManageListInfo> infoFunction) {
        if (this.manageListInfo != null) {
            throw new RuntimeException("You should not call this twice!");
        }
        // 应用函数修改 manageListInfo
        this.manageListInfo = infoFunction.apply(ManageListInfo.create());
        return this;
    }

    /**
     * 添加下拉框
     *
     * @param code     下拉框 code
     * @param dropdown 下拉框 map
     * @return this
     */
    public ManageListBuilder<T> addDropdown(@NotNull String code, @NotNull Map<String, Object> dropdown) {
        if (dropdownMap.containsKey(code)) {
            throw new RuntimeException("You should not add dropdown with same code ('" + code + "') twice!");
        }
        dropdownMap.put(code, dropdown);
        return this;
    }

    /**
     * 设置列表数据
     *
     * @param pageData 列表数据
     * @return this
     */
    public ManageListBuilder<T> setList(@NotNull List<? extends T> pageData) {
        if (this.pageData != null) {
            throw new RuntimeException("You should not call this twice!");
        }
        this.pageData = pageData;
        return this;
    }

    /**
     * 设置列表配置
     *
     * @param pageInfo 列表配置
     * @return this
     */
    public ManageListBuilder<T> setListConfig(ManageListPageInfo pageInfo) {
        this.pageInfo = pageInfo;
        return this;
    }

    /**
     * 设置表格查询配置
     *
     * @param manageListQuery 查询配置
     * @return this
     */
    public ManageListBuilder<T> setTableQuery(@NotNull ManageListQuery manageListQuery) {
        if (this.tableQuery != null) {
            throw new RuntimeException("You should not call this twice!");
        }
        this.tableQuery = manageListQuery;
        return this;
    }

    /**
     * 使用函数式接口设置表格查询配置
     * 支持lambda表达式形式：setTableQuery((queryObj) -> queryObj.add(...))
     *
     * @param queryFunction 接收QueryConfig并返回修改后的QueryConfig的函数
     * @return this
     */
    public ManageListBuilder<T> setTableQuery(@NotNull Function<ManageListQuery, ManageListQuery> queryFunction) {
        if (this.tableQuery != null) {
            throw new RuntimeException("You should not call this twice!");
        }
        // 应用函数修改 tableQuery
        this.tableQuery = queryFunction.apply(ManageListQuery.create());
        return this;
    }

    /**
     * 设置表格列配置
     *
     * @param columnConfig 列配置
     * @return this
     */
    public ManageListBuilder<T> setTableColumn(@NotNull ManageListColumn columnConfig) {
        if (this.tableColumn != null) {
            throw new RuntimeException("You should not call this twice!");
        }
        this.tableColumn = columnConfig;
        return this;
    }

    /**
     * 使用函数式接口设置表格列配置
     * 支持lambda表达式形式：setTableColumn((columnObj) -> columnObj.add(...))
     *
     * @param columnFunction 接收ManageListColumn并返回修改后的ManageListColumn的函数
     * @return this
     */
    public ManageListBuilder<T> setTableColumn(@NotNull Function<ManageListColumn, ManageListColumn> columnFunction) {
        if (this.tableColumn != null) {
            throw new RuntimeException("You should not call this twice!");
        }
        // 应用函数修改 tableColumn
        this.tableColumn = columnFunction.apply(ManageListColumn.create());
        return this;
    }

    /**
     * 构建并返回
     *
     * @return this
     */
    @SneakyThrows
    public ManageListBuilder<T> build() {
        final String className = this.getClass().getSimpleName();
        char[] charArray = className.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        final String lowerCaseClassName = new String(charArray);

        // 检查必要的配置是否已设置
        ManageListInfo.checkValidOrThrow(lowerCaseClassName, "setInfo", this.manageListInfo);

        // tableQuery 和 tableColumn 不是必须的，可以根据需要设置
        if (this.tableQuery != null) {
            ManageListQuery.checkValidOrThrow(lowerCaseClassName, "setTableQuery", this.tableQuery);
        }

        if (this.tableColumn != null) {
            ManageListColumn.checkValidOrThrow(lowerCaseClassName, "setTableColumn", this.tableColumn);
        }

        if (this.pageInfo != null) {
            ManageListPageInfo.checkValidOrThrow(lowerCaseClassName, "setListConfig", this.pageInfo);
        }

        return this;
    }

    /**
     * 转 json 对象
     *
     * @return json 对象
     */
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("info", this.manageListInfo);
        jsonObject.put("dropdown", this.dropdownMap);
        jsonObject.put("list", this.pageData);
        jsonObject.put("query", this.tableQuery);
        jsonObject.put("column", this.tableColumn);

        // 添加列表配置
        jsonObject.put("listConfig", pageInfo);

        return jsonObject;
    }

    /**
     * 转 json 字符串
     *
     * @return json 字符串
     */
    public String toJson() {
        // return JSON.toJSONString(this);
        // return toJsonObject().toString();
        // 保留 null 值
        return toJsonObject().toJSONString(JSONWriter.Feature.WriteNulls);
    }

    /**
     * ManageListBuilder 使用示例
     */
    public static void main(String[] args) {
        FastjsonInitializer fastjsonInitializer = new FastjsonInitializer();
        fastjsonInitializer.run(null);

        // 准备测试数据
        User user1 = new User();
        user1.setUsername("张三");
        User user2 = new User();
        user2.setUsername("李四");
        User user3 = new User();
        user3.setUsername("王五");
        User user4 = new User();
        user4.setUsername("赵六");
        // 示例数据列表
        List<User> userList = Arrays.asList(user1, user2, user3, user4);

        StopWatch stopWatch = new StopWatch();
        // 方式1：传统方式，直接传入 ManageListInfo 对象
        stopWatch.start("方式1");
        ManageListInfo info = ManageListInfo.create();
        info.setPageName("用户管理页面");

        // 创建查询配置
        ManageListQuery manageListQuery = ManageListQuery.create();
        manageListQuery.string("username", "用户名");

        // 创建列配置
        ManageListColumn columnConfig = ManageListColumn.create();
        columnConfig.text("username", "用户名");

        // 创建列表配置
        ManageListPageInfo pageInfo = ManageListPageInfo.create()
                .setPagination(true)
                .setPageSize(20)
                .setDefaultSortField("createTime")
                .setDefaultSortOrder("descend")
                .setCheckable(true);

        ManageListBuilder<User> builder1 = ManageListBuilder.create(User.class)
                .setInfo(info)
                .setList(userList)
                .setTableQuery(manageListQuery)
                .setTableColumn(columnConfig)
                .setListConfig(pageInfo)
                .addDropdown("status", Collections.singletonMap("1", "启用"))
                .build();
        stopWatch.stop();
        log.info("方式1结果：{}", builder1.toJson());

        // 方式2：使用 lambda 表达式方式设置
        stopWatch.start("方式2");
        // 模拟标签列表
        List<StringQueryConfig.TagConfig> tagList = Arrays.asList(
                new StringQueryConfig.TagConfig().setCode("right").setName("对").setTagType("success"),
                new StringQueryConfig.TagConfig().setCode("wrong").setName("错").setTagType("error")
        );

        // 模拟下拉框选项
        Map<String, Object> options = new HashMap<>();
        options.put("1", "选项1");
        options.put("2", "选项2");

        // 模拟字段映射器
        Map<String, Object> fieldMapper = new HashMap<>();
        fieldMapper.put("1", "是");
        fieldMapper.put("0", "否");

        ManageListBuilder<User> builder2 = ManageListBuilder.create(User.class)
                .setInfo((infoObj) -> infoObj.setPageName("用户管理页面"))
                .setList(userList)
                .setTableQuery((queryObj) -> queryObj
                        .string("username", "用户名", (query) -> query
                                .setFold(true)
                                .setPlaceHolder("请输入用户名")
                                .setSelectByTag(tagList)
                        )
                        .bool("status", "状态", (query) -> query
                                .setShowType("switch")
                        )
                        .dropdown("role", "角色", options)
                )
                .setTableColumn((columnObj) -> columnObj
                        .add("username", "用户名", ColumnType.TEXT)
                        .text("nickname", "昵称", fieldMapper, (config) -> config
                                .setLongTextEllipsis(true)
                        )
                        .datetime("createTime", "创建时间", DateValueType.TIMESTAMP_MILLISECOND, DateDisplayType.DATE_TIME)
                        .image("avatar", "头像", fieldMapper, (config) -> config
                                .setWidth(48)
                                .setHeight(48)
                        )
                        .add("isAdmin", "是否管理员", ColumnType.TEXT, fieldMapper)
                )
                .addDropdown("status", Collections.singletonMap("1", "启用"))
                .build();
        stopWatch.stop();
        log.info("方式2结果：{}", builder2.toJson());

        System.out.println(stopWatch.prettyPrint());

        /*
        期望的调用方式：
        ManageListBuilder<Object> builder2 = ManageListBuilder.create()
                .setInfo((infoObj) -> infoObj.setPageName("用户管理页面"))
                .setList(pageList)
                .setTableQuery((queryObj) ->
                    // queryObj.add 最后都支持传入 lambda, 不同类型的 lambda 可能支持配置的选项不同, 但会有一些通用的配置项比如: setFold
                    // 建议创建一个 BaseQuery 类，所有的 Query 都基于它，这样方便配置通用属性

                    queryObj.add("field", "fieldName", QueryType.STRING, (stringQuery) -> stringQuery
                        .setFold(true) // default: false, 设置为 true 时折叠到更多查询条件中
                        .setPlaceHolder("")
                        .setSelectByTag(tagList) // tagList: [ { code: "right", name: "对", tagType: "success" }, { code: "wrong", name: "错", tagType: "error" } ]
                    )
                    queryObj.add("field", "fieldName", QueryType.STRING_MULTIPLE)
                    queryObj.add("field", "fieldName", QueryType.BOOLEAN, (booleanQuery) ->
                        .setShowType() // dropdown 下拉框, checkbox 复选框, radio 两个单选框, switch 开关
                    )
                    queryObj.add("field", "fieldName", QueryType.DROPDOWN, options)
                    queryObj.add("field", "fieldName", QueryType.DROPDOWN_MULTIPLE, options)
                    queryObj.add("field", "fieldName", QueryType.DATETIME_EXACT, (datetimeQuery) ->
                        .setDateType(DateQueryType.TIME)
                        // DateQueryType: DATETIME, DATE, TIME
                    )
                    queryObj.add("field", "fieldName", QueryType.DATETIME_RANGE)
                    queryObj.add("field", "fieldName", QueryType.NUMBER_EXACT)
                    queryObj.add("field", "fieldName", QueryType.NUMBER_RANGE, (numberRangeQuery) ->
                        .setShowType() // slider 滑块,
                    )
                    queryObj.add("field", "fieldName", QueryType.MAP_LOCATION_2D, (mapLocation2d) ->
                        .setCenter(lat, lng)
                        .setZoom()
                    )
                    queryObj.add("field", "fieldName", QueryType.MAP_LOCATION_3D, (mapLocation2d) ->
                        .setCenter(lat, lng, alt)
                        .setZoom()
                    )
                )
                .setTableColumn((columnObj) ->
                    // columnObj.add 最后都支持传入 lambda, 不同类型的 lambda 可能支持配置的选项不同, 但会有一些通用的配置项比如: setColumnWidth
                    // 建议创建一个 BaseColumn 类，所有的 BaseColumn 都基于它，这样方便配置通用属性

                    columnObj.add("field", "fieldName", ColumnType.TEXT);

                    columnObj.add("field", "fieldName", ColumnType.TEXT, (textExtra) -> textExtra
                        .setColumnWidth(48) // default: unset
                        .setLongTextEllipsis(true) // 过长时展示省略号 default: false
                        .setUseTooltip(true) // 过长时展示省略号 default: false
                        .setShowAsTag(true) // default: false, equals `.setShowAsTag(true, "default TagType")`, tagType: default, success, warn, error, grey
                    );

                    columnObj.add("field", "fieldName", ColumnType.DATETIME, DateValueType.MILLISECOND_TIMESTAMP);
                    // or ↓
                    columnObj.add("field", "fieldName", ColumnType.DATETIME, DateValueType.MILLISECOND_TIMESTAMP, DateDisplayType.DATE_TIME); // DateDisplayType is optional, DateDisplayType.DATE_TIME is default value
                    // DateValueType:
                    // TIMESTAMP_MILLISECOND: 毫秒级时间戳 (13位数字), 示例: 1696924800000
                    // TIMESTAMP_SECOND: 秒级时间戳 (10位数字), 示例: 1696924800
                    // ISO_STRING: ISO 8601 标准格式, 示例: "2023-10-10T12:00:00Z"
                    // ISO_TIMEZONE_STRING: 带时区的ISO格式, 示例: "2023-10-10T12:00:00+08:00"
                    // DATE_TIME_STRING: 完整日期时间格式 (yyyy-MM-dd HH:mm:ss), 示例: "2023-10-10 12:00:00"
                    // DATE_STRING: 仅日期格式 (yyyy-MM-dd), 示例: "2023-10-10"
                    // TIME_STRING: 仅时间格式 (HH:mm:ss), 示例: "12:00:00"
                    // DATE_TIME_MILLISECOND_STRING: 带毫秒的完整日期时间格式 (yyyy-MM-dd HH:mm:ss.SSS), 示例: "2023-10-10 12:00:00.000"
                    // RFC_1123: RFC 1123 格式 (常用于HTTP协议), 示例: "Tue, 10 Oct 2023 12:00:00 GMT"
                    //
                    // DateDisplayType:
                    // RAW: 原始数据格式（不推荐）
                    // TIMESTAMP_MILLISECOND: 展示毫秒级时间戳 (13位数字), 示例: 1696924800000
                    // TIMESTAMP_SECOND: 展示秒级时间戳 (10位数字), 示例: 1696924800
                    // DATE_TIME: 展示为 2023-10-10 12:00:00 格式
                    // DATE: 展示为 2023-10-10 格式
                    // TIME: 展示为 12:00:00 格式
                    // DATE_TIME_MILLISECOND: 展示为 2023-10-10 12:00:00.000 格式
                    columnObj.add("field", "fieldName", ColumnType.DATETIME, DateValueType.MILLISECOND_TIMESTAMP, (datetimeExtra) -> datetimeExtra
                        .setColumnWidth(48) // default: unset
                    );
                    columnObj.add("field", "fieldName", ColumnType.DATETIME, DateValueType.MILLISECOND_TIMESTAMP, DateDisplayType.DATE_TIME, () -> { ... });

                    columnObj.add("field", "fieldName", ColumnType.IMAGE);
                    columnObj.add("field", "fieldName", ColumnType.IMAGE, (imageExtra) -> imageExtra
                        .setColumnWidth(48) // default: unset
                        .setWidth(48) // default: unset
                        .setHeight(48) // default: unset
                    );

                    columnObj.add("field", "fieldName", fieldMapper);
                )
                .build();
         */

    }
}
