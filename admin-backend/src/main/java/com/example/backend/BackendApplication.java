package com.example.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.servlet.context.ServletComponentScan;

@SpringBootApplication
// @MapperScan("com.example.backend.mapper")
@MapperScan("com.example.backend.modules.*.mapper")
@ServletComponentScan
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);

        // 启动时打印日志已经移至 ProjectConfigPrinter
/*
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(BackendApplication.class, args);

        ConfigurableEnvironment environment = configurableApplicationContext.getBean(ConfigurableEnvironment.class);
        final String port = environment.getProperty("server.port");
        final String env = environment.getProperty("project-config.env");
        final String datasourceUrl = environment.getProperty("spring.datasource.url");
        final boolean springDocEnabled = Boolean.TRUE.equals(environment.getProperty("springdoc.api-docs.enabled", Boolean.class));
        DataSource dataSource = configurableApplicationContext.getBean(DataSource.class);
        System.out.println(
                "============\n" +
                "系统启动成功！\n" +
                "============\n" +
                "当前读取的配置文件：" + configName + "\n" +
                "        当前环境：" + env + "\n" +
                "   数据库连接URL：" + datasourceUrl + "\n" +
                "   当前使用数据源：" + dataSource.getClass() + "\n" +
                "        接口地址：http://localhost:" + port + "\n" +
                "Swagger 接口文档：" + (springDocEnabled
                        ? "http://localhost:" + port + "/swagger-ui/index.html"
                        : "未启用") + "\n" +
                "============"
        );
*/
    }

}
