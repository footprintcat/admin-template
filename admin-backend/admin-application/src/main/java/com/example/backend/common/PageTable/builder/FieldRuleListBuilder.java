package com.example.backend.common.PageTable.builder;

import com.alibaba.fastjson2.JSONArray;

public class FieldRuleListBuilder {

    private JSONArray ruleList;

    public static FieldRuleListBuilder create() {
        FieldRuleListBuilder builder = new FieldRuleListBuilder();
        builder.ruleList = new JSONArray();
        return builder;
    }

    public FieldRuleListBuilder add(FieldRuleBuilder fieldRuleBuilder) {
        ruleList.add(fieldRuleBuilder.build());
        return this;
    }

    public JSONArray build() {
        return ruleList;
    }
}
