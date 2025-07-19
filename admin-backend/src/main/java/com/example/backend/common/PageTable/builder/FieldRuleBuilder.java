package com.example.backend.common.PageTable.builder;

import com.alibaba.fastjson2.JSONObject;

/**
 * Element Plus 表单验证
 * GiHub refer: <a href="https://github.com/yiminghe/async-validator">...</a>
 */
public class FieldRuleBuilder {

    private String fieldDisplayName;
    private JSONObject rules;

    public static FieldRuleBuilder create(String fieldDisplayName) {
        FieldRuleBuilder builder = new FieldRuleBuilder();
        builder.rules = new JSONObject();
        builder.fieldDisplayName = fieldDisplayName;
        // 默认在 blur 时触发验证
        // builder.rules.put("trigger", "blur");
        builder.rules.put("trigger", "change");
        return builder;
    }

    public FieldRuleBuilder trigger(String trigger) {
        rules.put("trigger", trigger);
        return this;
    }

    public FieldRuleBuilder message(String message) {
        rules.put("message", message);
        return this;
    }

    public FieldRuleBuilder required() {
        rules.put("required", true);
        rules.put("message", fieldDisplayName + "不能为空");
        return this;
    }

    public FieldRuleBuilder pattern(String regEx) {
        rules.put("pattern", regEx);
        rules.put("message", fieldDisplayName + "输入不符合要求，请检查");
        return this;
    }

    public FieldRuleBuilder pattern(String regEx, String message) {
        rules.put("pattern", regEx);
        rules.put("message", fieldDisplayName + message);
        return this;
    }

    public FieldRuleBuilder number() {
        rules.put("type", "number");
        rules.put("message", fieldDisplayName + "必须为数字");
        return this;
    }

    public FieldRuleBuilder min(Integer min) {
        rules.put("min", min);
        rules.put("message", fieldDisplayName + "过短");
        return this;
    }

    public FieldRuleBuilder min(Double min) {
        rules.put("min", min);
        rules.put("message", fieldDisplayName + "过短");
        return this;
    }

    public FieldRuleBuilder max(Integer max) {
        rules.put("max", max);
        rules.put("message", fieldDisplayName + "过长");
        return this;
    }

    public FieldRuleBuilder max(Double max) {
        rules.put("max", max);
        rules.put("message", fieldDisplayName + "过长");
        return this;
    }

    public FieldRuleBuilder minMax(Integer min, Integer max) {
        rules.put("min", min);
        rules.put("max", max);
        rules.put("message", fieldDisplayName + "长度应在 " + min + " 至 " + max + " 之间");
        return this;
    }

    public FieldRuleBuilder length(Integer len) {
        rules.put("min", len);
        rules.put("max", len);
        rules.put("message", fieldDisplayName + "位数不对，应为 " + len + " 位");
        return this;
    }

    public FieldRuleBuilder add(String field, Object value) {
        rules.put(field, value);
        return this;
    }

    public JSONObject build() {
        return rules;
    }
}

