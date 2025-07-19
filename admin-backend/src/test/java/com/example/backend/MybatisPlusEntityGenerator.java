package com.example.backend;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.util.Collections;
import java.util.LinkedList;

// refer: https://www.bilibili.com/read/cv22171769/
public class MybatisPlusEntityGenerator {
    public static void main(String[] args) {

        // 是否覆盖
        final boolean isOverride = false;

        LinkedList<String> includeTables = new LinkedList<>();
        includeTables.add("user");

        // 数据源配置
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/mine?serverTimezone=GMT%2b8&useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("coder-xiaomo")        // 设置作者
                            .enableSwagger()        // 开启 swagger 模式 默认值:false
                            .disableOpenDir()       // 禁止打开输出目录 默认值:true
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .dateType(DateType.ONLY_DATE)   // 定义生成的实体类中日期类型 DateType.ONLY_DATE 默认值: DateType.TIME_PACK
                            .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录

                    // fileOverride @Deprecated see:
                    // MybatisPlus代码生成器FastAutoGenerator，文件不覆盖，且全局配置中fileOverride过时问题
                    // https://blog.csdn.net/qq_41713884/article/details/130704606
                    // if (isOverride) { /* v3.5.1 */
                    //     builder.fileOverride(); // 设置文件覆盖
                    // }
                })

                .packageConfig(builder -> {
                    builder.parent("com.example.backend") // 父包模块名
                            // .controller("controller")   // Controller 包名 默认值:controller
                            .entity("entity")           // Entity 包名 默认值:entity
                            // .service("service")         // Service 包名 默认值:service
                            .mapper("mapper")           // Mapper 包名 默认值:mapper
                            .other("model")
                            //.moduleName("xxx")        // 设置父包模块名 默认值:无
                            // /* v3.5.1 -> */.pathInfo(Collections.singletonMap(OutputFile.mapperXml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                            /* v3.5.2 -> */.pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "/src/main/resources/mapper")); // 设置mapperXml生成路径
                    // 默认存放在mapper的xml下
                })

                // .injectionConfig(consumer -> {
                //     Map<String, String> customFile = new HashMap<>();
                //     // DTO、VO
                //     customFile.put("DTO.java", "/templates/entityDTO.java.ftl");
                //     customFile.put("VO.java", "/templates/entityVO.java.ftl");
                //
                //     consumer.customFile(customFile);
                // })

                .strategyConfig(builder -> {
                    builder.addInclude(includeTables)
                            // .addTablePrefix("tb_", "gms_") // 设置过滤表前缀

                            // .serviceBuilder()//service策略配置
                            // .formatServiceFileName("%sService")
                            // .formatServiceImplFileName("%sServiceImpl")

                            .entityBuilder()// 实体类策略配置
                            .idType(IdType.ASSIGN_ID)// 主键策略  雪花算法自动生成的id
                            .addTableFills(new Column("create_time", FieldFill.INSERT)) // 自动填充配置
                            .addTableFills(new Property("update_time", FieldFill.INSERT_UPDATE))
                            .enableLombok() // 开启lombok
                            .logicDeleteColumnName("deleted")// 说明逻辑删除是哪个字段
                            .enableTableFieldAnnotation()// 属性加上注解说明

                            // .controllerBuilder() //controller 策略配置
                            // .formatFileName("%sController")
                            // .enableRestStyle() // 开启RestController注解

                            .mapperBuilder()// mapper策略配置
                            .formatMapperFileName("%sMapper")
                            .enableMapperAnnotation()//@mapper注解开启
                            .formatXmlFileName("%sMapper");
                })

                .templateConfig(builder -> {
                    builder.disable(TemplateType.CONTROLLER, TemplateType.SERVICEIMPL, TemplateType.SERVICE);
                })

                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                // .templateEngine(new EnhanceFreemarkerTemplateEngine())
                .execute();
    }
}
