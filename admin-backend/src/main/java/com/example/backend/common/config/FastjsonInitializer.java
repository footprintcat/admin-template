package com.example.backend.common.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 配置全局序列化规则
 */
@Component
public class FastjsonInitializer implements ApplicationRunner {

    @Override
    public void run(@NonNull ApplicationArguments args) {
        // 让 Fastjson2 将所有 Long 类型自动序列化为字符串格式
        JSON.config(
                // Long转为String防止前端精度丢失
                JSONWriter.Feature.WriteLongAsString,
                // 输出Map中的null值
                JSONWriter.Feature.WriteMapNullValue
        );
    }
}
