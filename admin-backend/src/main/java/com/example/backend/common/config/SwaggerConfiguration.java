package com.example.backend.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.providers.JavadocProvider;
import org.springdoc.core.providers.SpringDocJavadocProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Swagger 接口文档
 */
@Configuration
public class SwaggerConfiguration {

    // 后面 GroupedOpenApi 添加后，springOpenAPI 会被多次调用
    @Bean
    public OpenAPI springOpenAPI() {
        Info info = new Info()
                .title("admin-template 项目后端接口文档")
                .description("前后端分离的接口文档")
                .version("1.0.0");

        return new OpenAPI().info(info);
    }

    /**
     * 获取 JavadocProvider
     * <p>
     * 这其实是默认行为，可以不写，但是考虑到后续如果希望调整方便查找，所以写在这里
     *
     * @return
     */
    public JavadocProvider getJavadocProvider() {
        return new SpringDocJavadocProvider();
    }

    // Swagger 页面右上角下拉框的分组
    @Bean
    public List<GroupedOpenApi> groupedOpenApi() {
        return Arrays.asList(
                GroupedOpenApi.builder()
                        .group(getGroupName("所有接口"))
                        .pathsToMatch("/**")
                        .build(),

                // 按包路径扫描
                GroupedOpenApi.builder()
                        .group(getGroupName("公开接口"))
                        .packagesToScan("com.example.backend.controller.publicapi")
                        .build(),
                GroupedOpenApi.builder()
                        .group(getGroupName("后台管理接口"))
                        .packagesToScan("com.example.backend.controller.manage")
                        .build(),
                GroupedOpenApi.builder()
                        .group(getGroupName("App接口"))
                        .packagesToScan("com.example.backend.controller.app")
                        .build()

/*
                // 匹配路径
                GroupedOpenApi.builder()
                        .group(getGroupName("v3前缀接口"))
                        .pathsToMatch("/v3/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group(getGroupName("v2前缀接口"))
                        .pathsToMatch("/v2/**")
                        .build(),
                GroupedOpenApi.builder()
                        .group(getGroupName("v1前缀接口"))
                        .pathsToMatch("/v1/**")
                        .build()
*/
        );
    }

    int i = 0;

    private String getGroupName(String name) {
        // 添加数字前缀以保证 Swagger 网页右上角下拉框排序
        return ++i + "-" + name;
    }
}
