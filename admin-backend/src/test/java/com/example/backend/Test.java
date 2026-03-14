package com.example.backend;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public class Test {
    public static void main(String[] args) {
        // 配置 Jackson ObjectMapper
        ObjectMapper mapper = new ObjectMapper();
        
        // 配置 Long 类型序列化为字符串（防止前端精度丢失）
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        mapper.registerModule(simpleModule);
        
        // 序列化时包含 null 值
        mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        try {
            String json = mapper.writeValueAsString(new TestBean(111111111111111111L));
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        int i = Integer.parseInt(null);
        System.out.println(i);
    }
    
    static class TestBean {
        private Long a;
        
        public TestBean(Long a) {
            this.a = a;
        }
        
        public Long getA() {
            return a;
        }
        
        public void setA(Long a) {
            this.a = a;
        }
    }
}
