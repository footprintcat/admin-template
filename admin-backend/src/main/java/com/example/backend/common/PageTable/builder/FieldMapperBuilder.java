package com.example.backend.common.PageTable.builder;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.Map;

public class FieldMapperBuilder {

    private JSONArray columns;

    public static FieldMapperBuilder create() {
        FieldMapperBuilder builder = new FieldMapperBuilder();
        builder.columns = new JSONArray();
        return builder;
    }

    public FieldMapperBuilder add(String prop, String label, Map<?, ?> mapper) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", prop);
        jsonObject.put("value", label);
        jsonObject.put("mapper", mapper);
        columns.add(jsonObject);
        return this;
    }

    public JSONArray build() {
        return columns;
    }
}
