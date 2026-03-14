package com.example.backend.common.PageTable.builder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class FieldRuleListBuilder {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ArrayNode ruleList;

    public static FieldRuleListBuilder create() {
        FieldRuleListBuilder builder = new FieldRuleListBuilder();
        builder.ruleList = OBJECT_MAPPER.createArrayNode();
        return builder;
    }

    public FieldRuleListBuilder add(FieldRuleBuilder fieldRuleBuilder) {
        ruleList.add(fieldRuleBuilder.build());
        return this;
    }

    public ArrayNode build() {
        return ruleList;
    }
}
