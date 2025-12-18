package com.example.backend.common.ManageList.pojo;

import com.example.backend.common.ManageList.enums.QueryType;
import com.example.backend.common.ManageList.pojo.queryconfig.BooleanQueryConfig;
import com.example.backend.common.ManageList.pojo.queryconfig.DateTimeQueryConfig;
import com.example.backend.common.ManageList.pojo.queryconfig.DropdownQueryConfig;
import com.example.backend.common.ManageList.pojo.queryconfig.MapLocation2DConfig;
import com.example.backend.common.ManageList.pojo.queryconfig.MapLocation3DConfig;
import com.example.backend.common.ManageList.pojo.queryconfig.NumberRangeQueryConfig;
import com.example.backend.common.ManageList.pojo.queryconfig.StringQueryConfig;
import com.example.backend.common.ManageList.pojo.queryconfig.base.BaseQueryConfig;
import lombok.Data;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 查询配置类
 * 用于配置表格的查询条件
 */
@Data
@Accessors(chain = true)
public class ManageListQuery {

    // 查询条件列表
    private List<Map<String, Object>> queries = new ArrayList<>();

    private ManageListQuery() {
    }

    /**
     * 创建一个新的QueryConfig实例
     *
     * @return QueryConfig实例
     */
    public static ManageListQuery create() {
        return new ManageListQuery();
    }

    private <T extends BaseQueryConfig<?>> Map<String, Object> createBaseQueryMap(
            @NotNull String field, @NotNull String fieldName, @NotNull QueryType queryType,
            @Nullable Map<String, Object> options,
            @Nullable Function<T, T> configFunc, @Nullable T configInstance
    ) {
        Map<String, Object> queryMap = new java.util.HashMap<>();
        queryMap.put("field", field); // 字段名
        queryMap.put("fieldName", fieldName); // 显示名称
        queryMap.put("queryType", queryType); // 列类型
        queryMap.put("options", options);

        // 配置函数 & 配置实例
        if (configFunc != null && configInstance != null) {
            T config = configFunc.apply(configInstance);
            // 检查配置是否有效
            if (!config.getIsValid()) {
                throw new IllegalArgumentException(config.getClass().getSimpleName() + " is not valid");
            }
            queryMap.put("extra", config);
        } else {
            queryMap.put("extra", Collections.emptyMap());
        }
        return queryMap;
    }

    /**
     * 添加查询条件
     *
     * @param field     字段名
     * @param fieldName 显示名称
     * @param queryType 查询类型
     * @return 当前实例
     */
    public ManageListQuery add(@NotNull String field, @NotNull String fieldName, QueryType queryType) {
        return add(field, fieldName, queryType, null, null, null);
    }

    /**
     * 添加查询条件
     *
     * @param field     字段名
     * @param fieldName 显示名称
     * @param queryType 查询类型
     * @param options   下拉框选项
     * @return 当前实例
     */
    public ManageListQuery add(@NotNull String field, @NotNull String fieldName, @NotNull QueryType queryType,
                               @Nullable Map<String, Object> options) {
        return add(field, fieldName, queryType, options, null, null);
    }

    /**
     * 添加查询条件
     *
     * @param field          字段名
     * @param fieldName      显示名称
     * @param queryType      查询类型
     * @param configFunc     配置函数
     * @param configInstance 配置实例
     * @param <T>            配置类型
     * @return 当前实例
     */
    private <T extends BaseQueryConfig<?>> ManageListQuery add(
            @NotNull String field, @NotNull String fieldName, @NotNull QueryType queryType,
            @Nullable Function<T, T> configFunc, @Nullable T configInstance
    ) {
        return add(field, fieldName, queryType, null, configFunc, configInstance);
    }

    /**
     * 添加查询条件
     *
     * @param field          字段名
     * @param fieldName      显示名称
     * @param queryType      查询类型
     * @param options        下拉框选项
     * @param configFunc     配置函数
     * @param configInstance 配置实例
     * @param <T>            配置类型
     * @return 当前实例
     */
    public <T extends BaseQueryConfig<?>> ManageListQuery add(
            @NotNull String field, @NotNull String fieldName, @NotNull QueryType queryType,
            @Nullable Map<String, Object> options,
            @Nullable Function<T, T> configFunc, @Nullable T configInstance
    ) {
        Map<String, Object> queryMap = createBaseQueryMap(field, fieldName, queryType, options, configFunc, configInstance);
        queries.add(queryMap);
        return this;
    }

    // ===== 查询类型专用方法 =====

    /**
     * 添加字符串查询条件
     */
    public ManageListQuery string(@NotNull String field, @NotNull String fieldName) {
        return add(field, fieldName, QueryType.STRING);
    }

    public ManageListQuery string(@NotNull String field, @NotNull String fieldName,
                                  @Nullable Function<StringQueryConfig, StringQueryConfig> configFunction) {
        return add(field, fieldName, QueryType.STRING, configFunction, new StringQueryConfig());
    }

    /**
     * 添加布尔查询条件
     */
    public ManageListQuery bool(@NotNull String field, @NotNull String fieldName) {
        return add(field, fieldName, QueryType.BOOLEAN);
    }

    public ManageListQuery bool(@NotNull String field, @NotNull String fieldName,
                                @Nullable Function<BooleanQueryConfig, BooleanQueryConfig> configFunction) {
        return add(field, fieldName, QueryType.BOOLEAN, configFunction, new BooleanQueryConfig());
    }

    /**
     * 添加日期时间查询条件
     */
    public ManageListQuery datetimeExact(@NotNull String field, @NotNull String fieldName) {
        return add(field, fieldName, QueryType.DATETIME_EXACT);
    }

    public ManageListQuery datetimeExact(@NotNull String field, @NotNull String fieldName,
                                         @Nullable Function<DateTimeQueryConfig, DateTimeQueryConfig> configFunction) {
        return add(field, fieldName, QueryType.DATETIME_EXACT, configFunction, new DateTimeQueryConfig());
    }

    public ManageListQuery datetimeRange(@NotNull String field, @NotNull String fieldName) {
        return add(field, fieldName, QueryType.DATETIME_RANGE);
    }

    public ManageListQuery datetimeRange(@NotNull String field, @NotNull String fieldName,
                                         @Nullable Function<DateTimeQueryConfig, DateTimeQueryConfig> configFunction) {
        return add(field, fieldName, QueryType.DATETIME_RANGE, configFunction, new DateTimeQueryConfig());
    }

    /**
     * 添加数字范围查询条件
     */
    public ManageListQuery numberRange(@NotNull String field, @NotNull String fieldName) {
        return add(field, fieldName, QueryType.NUMBER_RANGE);
    }

    public ManageListQuery numberRange(@NotNull String field, @NotNull String fieldName,
                                       @Nullable Function<NumberRangeQueryConfig, NumberRangeQueryConfig> configFunction) {
        return add(field, fieldName, QueryType.NUMBER_RANGE, configFunction, new NumberRangeQueryConfig());
    }

    /**
     * 添加2D地图位置查询条件
     */
    public ManageListQuery mapLocation2d(@NotNull String field, @NotNull String fieldName) {
        return add(field, fieldName, QueryType.MAP_LOCATION_2D);
    }

    public ManageListQuery mapLocation2d(@NotNull String field, @NotNull String fieldName,
                                         @Nullable Function<MapLocation2DConfig, MapLocation2DConfig> configFunction) {
        return add(field, fieldName, QueryType.MAP_LOCATION_2D, configFunction, new MapLocation2DConfig());
    }

    /**
     * 添加3D地图位置查询条件
     */
    public ManageListQuery mapLocation3d(@NotNull String field, @NotNull String fieldName) {
        return add(field, fieldName, QueryType.MAP_LOCATION_3D);
    }

    public ManageListQuery mapLocation3d(@NotNull String field, @NotNull String fieldName,
                                         @Nullable Function<MapLocation3DConfig, MapLocation3DConfig> configFunction) {
        return add(field, fieldName, QueryType.MAP_LOCATION_3D, configFunction, new MapLocation3DConfig());
    }

    /**
     * 添加下拉框查询条件
     */
    public ManageListQuery dropdown(@NotNull String field, @NotNull String fieldName,
                                    @Nullable Map<String, Object> options) {
        return add(field, fieldName, QueryType.DROPDOWN, options);
    }

    public ManageListQuery dropdown(@NotNull String field, @NotNull String fieldName,
                                    @Nullable Map<String, Object> options,
                                    @Nullable Function<DropdownQueryConfig, DropdownQueryConfig> configFunction) {
        return add(field, fieldName, QueryType.DROPDOWN, options, configFunction, new DropdownQueryConfig());
    }

    /**
     * 检查配置是否有效，无效则抛出异常
     *
     * @param lowerCaseCallerName 调用者类名（小写）
     * @param setFunctionName     设置函数名
     * @param info                QueryConfig对象
     */
    public static void checkValidOrThrow(String lowerCaseCallerName, String setFunctionName, ManageListQuery info) {
        final String className = ManageListQuery.class.getSimpleName();
        char[] charArray = className.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        final String lowerCaseClassName = new String(charArray);

        if (info == null) {
            throw new IllegalArgumentException(lowerCaseClassName + " is not set, please call " + lowerCaseCallerName + "." + setFunctionName + "(" + lowerCaseClassName + ")");
        }

        if (info.getQueries() == null || info.getQueries().isEmpty()) {
            throw new IllegalArgumentException(lowerCaseClassName + " is not valid, please call " + lowerCaseClassName + ".add(field, fieldName, ...);");
        }
    }
}
