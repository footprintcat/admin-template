package com.example.backend.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.ToStringSerializer;

/**
 * 配置 JSON 返回结果序列化
 *
 * @since 2025-12-16
 */
@Configuration
public class JacksonConfig {

    @Bean
    public JsonMapper jsonMapper() {
        SimpleModule simpleModule = new SimpleModule();

        // 配置 Long 类型序列化为字符串
        // 将 Long 包装类类型序列化为字符串
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        // 将基本数据类型 long 序列化为字符串
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);

        return JsonMapper.builder()
                .addModule(simpleModule)
                .build();
    }
}
