package com.example.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@SpringBootApplication
@MapperScan("com.example.backend.mapper")
@ServletComponentScan
public class BackendApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(BackendApplication.class, args);

        Environment environment = configurableApplicationContext.getBean(Environment.class);
        final String port = environment.getProperty("server.port");
        final String configName = environment.getProperty("project-config.config-name");
        final String env = environment.getProperty("project-config.env");
        final String datasourceUrl = environment.getProperty("spring.datasource.url");
        DataSource dataSource = configurableApplicationContext.getBean(DataSource.class);
        System.out.println(
                "============\n" +
                "系统启动成功！\n" +
                "当前读取的配置文件：" + configName + "\n" +
                "        当前环境：" + env + "\n" +
                "   数据库连接URL：" + datasourceUrl + "\n" +
                "   当前使用数据源：" + dataSource.getClass() + "\n" +
                "        接口地址：http://localhost:" + port + "\n" +
                "Swagger 接口文档：http://localhost:" + port + "/swagger-ui/index.html" + "\n" +
                "============"
        );
    }

}
