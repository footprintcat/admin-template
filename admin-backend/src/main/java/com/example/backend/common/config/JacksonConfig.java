package com.example.backend.common.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置 JSON 返回结果序列化
 * <p>
 * 使用 Spring Boot 默认的 Jackson 作为 JSON 处理库，
 * 配置 Long 类型序列化为字符串防止前端精度丢失，
 * 同时支持序列化 null 值。
 *
 * @since 2025-12-16
 */
@Configuration
public class JacksonConfig {

    /**
     * 配置 ObjectMapper Bean
     * <p>
     * 主要配置：
     * 1. Long 类型序列化为字符串（防止前端 JavaScript 精度丢失）
     * 2. 序列化时包含 null 值（保持与原 fastjson2 行为一致）
     * 3. 反序列化时忽略未知属性（增强兼容性）
     *
     * @return 配置好的 ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper() {
        SimpleModule simpleModule = new SimpleModule();

        // 配置 Long 类型序列化为字符串
        // 将 Long 包装类类型序列化为字符串
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        // 将基本数据类型 long 序列化为字符串
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        return JsonMapper.builder()
                .addModule(simpleModule)
                // 序列化时包含 null 值，保持与原 fastjson2 的 WriteMapNullValue 行为一致
                .serializationInclusion(JsonInclude.Include.ALWAYS)
                // 反序列化时忽略未知属性，增强兼容性
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                // 禁用将日期序列化为时间戳
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .build();
    }
}
