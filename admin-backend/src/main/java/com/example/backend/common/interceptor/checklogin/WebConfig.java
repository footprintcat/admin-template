package com.example.backend.common.interceptor.checklogin;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 注册 Interceptor
 *
 * @since 2015-12-14
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/manage/**") // 只拦截管理端接口
        // .excludePathPatterns("/manage/public/**") // 可排除某些路径
        ;
    }

}
