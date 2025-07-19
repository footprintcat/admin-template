package com.example.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

@SpringBootApplication
@MapperScan("com.example.backend.mapper")
@ServletComponentScan
public class BackendApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =
                SpringApplication.run(BackendApplication.class, args);

        Environment environment = configurableApplicationContext.getBean(Environment.class);
        String port = environment.getProperty("server.port");
        System.out.println("============\n" +
                "系统启动成功！\n" +
                "        接口地址：http://localhost:" + port + "\n" +
                "Swagger 接口文档：http://localhost:" + port + "/swagger-ui/index.html" + "\n" +
                "============");
    }

}
