package com.example.backend;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public class Test {
    public static void main(String[] args) {
        // 配置全局序列化规则
        // 让 Fastjson2 将所有 Long 类型自动序列化为字符串格式
        JSON.config(JSONWriter.Feature.WriteLongAsString);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("a", 111111111111111111L);
        System.out.println(jsonObject.toString());

        int i = Integer.parseInt(null);
        System.out.println(i);
    }
}
