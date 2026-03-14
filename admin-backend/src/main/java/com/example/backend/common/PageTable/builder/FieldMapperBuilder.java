package com.example.backend.common.PageTable.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;

public class FieldMapperBuilder {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ArrayNode columns;

    public static FieldMapperBuilder create() {
        FieldMapperBuilder builder = new FieldMapperBuilder();
        builder.columns = OBJECT_MAPPER.createArrayNode();
        return builder;
    }

    public FieldMapperBuilder add(String prop, String label, Map<?, ?> mapper) {
        ObjectNode jsonObject = OBJECT_MAPPER.createObjectNode();
        jsonObject.put("key", prop);
        jsonObject.put("value", label);
        jsonObject.putPOJO("mapper", mapper);
        columns.add(jsonObject);
        return this;
    }

    public ArrayNode build() {
        return columns;
    }
}
