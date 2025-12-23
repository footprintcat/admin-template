package com.example.backend;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.DefaultTableFieldAnnotationHandler;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import com.baomidou.mybatisplus.generator.model.AnnotationAttributes;
import com.example.backend.common.utils.StringUtils;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MybatisPlusEntityModuleGenerator {

    // 数据库关键字（生成 entity 时，这些字段自动添加反引号 ``）
    private static final List<String> autoDelimitKeywords = Arrays.asList(
            "order", "group", "level", "timestamp", "key"
    );

    record ModuleTables(String moduleName, String tablePrefix, String[] tableList) {
    }

    public static void main(String[] args) {

        List<ModuleTables> moduleTables = new ArrayList<>();

        /* 请按字母顺序添加 */
        moduleTables.add(new ModuleTables("system", "system_", new String[]{
                // "system_config",
                // "system_department",
                // "system_identity",
                // "system_identity_role_relation",
                // "system_job_position",
                // "system_log",
                // // "system_log_detail",
                // "system_menu",
                // "system_privilege",
                // "system_role",
                // "system_tenant",
                // "system_user",
                // "system_user_auth",
                null
        }));

        for (ModuleTables moduleTable : moduleTables) {
            List<String> includeTables = Arrays.stream(moduleTable.tableList).filter(StringUtils::isNotEmpty).toList();
            generateForTables(includeTables, moduleTable.moduleName, moduleTable.tablePrefix);
        }
    }

    private static void generateForTables(List<String> includeTables, String moduleName, String tablePrefix) {

        if (includeTables.isEmpty()) {
            // throw new RuntimeException("includeTables 为空，跳过代码生成");
            System.out.println(moduleName + " 模块 includeTables 为空，跳过该模块实体类生成");
            return;
        }

        // 获取项目路径
        String projectPath = System.getProperty("user.dir");
        String javaBasePath = projectPath + "/src/main/java";
        String resourcesBasePath = projectPath + "/src/main/resources";

        // 构建动态路径映射
        Map<OutputFile, String> pathInfo = new HashMap<>();

        // 为模块设置特定的路径
        String modulePath = "/modules/" + moduleName;
        pathInfo.put(OutputFile.entity, javaBasePath + "/com/example/backend" + modulePath + "/model/entity");
        pathInfo.put(OutputFile.mapper, javaBasePath + "/com/example/backend" + modulePath + "/mapper");
        pathInfo.put(OutputFile.xml, resourcesBasePath + "/mapper" + modulePath);
        pathInfo.put(OutputFile.serviceImpl, javaBasePath + "/com/example/backend" + modulePath + "/repository");

        String modulePackageName = "modules." + moduleName;

        // 数据源配置
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/admin_template?serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&useSSL=false&characterEncoding=UTF-8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("coder-xiaomo")        // 设置作者
                            // .enableSwagger()        // 开启 swagger 模式 默认值:false
                            .enableSpringdoc()      // 开启 springdoc 模式 默认值:false
                            .disableOpenDir()       // 禁止打开输出目录 默认值:true
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .dateType(DateType.ONLY_DATE)   // 定义生成的实体类中日期类型 DateType.ONLY_DATE 默认值: DateType.TIME_PACK
                            // .outputDir(System.getProperty("user.dir") + "/src/main/java"); // 指定输出目录
                            .outputDir(javaBasePath); // 指定输出目录
                })

                .dataSourceConfig(builder -> builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                    int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                    // 自定义类型转换
                    return switch (typeCode) {
                        case Types.SMALLINT, Types.TINYINT -> DbColumnType.INTEGER;
                        case Types.DECIMAL -> DbColumnType.DOUBLE; // (mysql) decimal -> (java) Double
                        case Types.DATE -> DbColumnType.LOCAL_DATE;
                        case Types.TIME ->
                                DbColumnType.LOCAL_TIME; // (mysql) DATETIME & TIMESTAMP -> (java) LocalDateTime
                        case Types.TIMESTAMP -> DbColumnType.LOCAL_DATE_TIME;
                        default -> typeRegistry.getColumnType(metaInfo);
                    };
                }))

                .packageConfig(builder -> {
                    builder.parent("com.example.backend") // 父包模块名
                            // .controller("controller")   // Controller 包名 默认值:controller
                            .entity(modulePackageName + ".model.entity")     // Entity 包名 默认值:entity
                            // .service("service")         // Service 包名 默认值:service
                            .serviceImpl(modulePackageName + ".repository")  // Service 实现类包名
                            .mapper(modulePackageName + ".mapper")           // Mapper 包名 默认值:mapper
                            // .other("model") /* v3.5.9 没有这个函数了 */
                            // .moduleName("xxx")        // 设置父包模块名 默认值:无
                            // /* v3.5.1 -> */.pathInfo(Collections.singletonMap(OutputFile.mapperXml, resourcesBasePath + "/mapper")); // 设置mapperXml生成路径
                            // /* v3.5.2 -> */.pathInfo(Collections.singletonMap(OutputFile.xml, resourcesBasePath + "/mapper")); // 设置mapperXml生成路径
                            .pathInfo(pathInfo); // 使用动态路径配置
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
                            .addTablePrefix(tablePrefix) // 设置表前缀（根据映射关系自动去除）

                            .serviceBuilder() // service策略配置
                            // .enableFileOverride()
                            // .formatServiceFileName("%sService")
                            // .formatServiceImplFileName("%sServiceImpl")
                            // /* v3.5.9 -> */.disable()
                            .disableService()
                            .formatServiceImplFileName("%sRepository")

                            .entityBuilder() // 实体类策略配置
                            .idType(IdType.ASSIGN_ID) // 主键策略  雪花算法自动生成的id
                            .addTableFills(new Column("create_time", FieldFill.INSERT)) // 自动填充配置
                            .addTableFills(new Property("update_time", FieldFill.INSERT_UPDATE))
                            .enableLombok() // 开启lombok
                            /* v3.5.10 -> */.toString(false) // 不生成 lombok 的 @ToString, 默认值: true
                            /* v3.5.10 -> */.fieldUseJavaDoc(false) // 不启用字段文档注释, 默认值: true
                            // .logicDeleteColumnName("is_delete") // 说明逻辑删除是哪个字段
                            .logicDeleteColumnName("delete_time") // 说明逻辑删除是哪个字段
                            .versionColumnName("version") // 说明乐观锁版本号是哪个字段
                            .enableTableFieldAnnotation()
                            /* v3.5.2 -> */// .fileOverride()
                            /* v3.5.9 -> */.enableFileOverride()
                            // entity 数据库关键字字段添加 ``
                            /* v3.5.10 */.tableFieldAnnotationHandler(new CustomTableFieldAnnotationHandler())
                            // entity 类注解 & 字段注解排序
                            // related issue: https://github.com/baomidou/mybatis-plus/issues/6685
                            // /* v3.5.11 */.annotationAttributesFunction(annotationAttributes -> annotationAttributes.stream()
                            .annotationAttributesFunction(annotationAttributes -> annotationAttributes.stream()
                                    .sorted(Comparator.comparingInt((AnnotationAttributes c) -> c.getDisplayName().charAt(1))).collect(Collectors.toList()))

                            .controllerBuilder() // controller 策略配置
                            // .formatFileName("%sController")
                            // .enableRestStyle() // 开启RestController注解
                            /* v3.5.9 -> */.disable()

                            .mapperBuilder() // mapper策略配置
                            // .enableFileOverride()
                            .formatXmlFileName("%sMapper")
                            .formatMapperFileName("%sMapper")
                            /* v3.5.2 -> *///.enableMapperAnnotation() // @mapper注解开启
                            /* v3.5.9 -> */.mapperAnnotation(org.apache.ibatis.annotations.Mapper.class) // @mapper注解开启
                    ;
                })

                /* v3.5.6 -> */
                // .templateConfig(builder -> {
                //     builder.disable(TemplateType.CONTROLLER, /* v3.5.2 -> TemplateType.SERVICEIMPL *//* v3.5.9 -> */TemplateType.SERVICE_IMPL, TemplateType.SERVICE);
                // })

                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                // .templateEngine(new EnhanceFreemarkerTemplateEngine())
                .execute();
    }

    // 针对与数据库关键字相同的字段，自动添加 `` 引号
    private static class CustomTableFieldAnnotationHandler extends DefaultTableFieldAnnotationHandler {
        @Override
        public List<AnnotationAttributes> handle(TableInfo tableInfo, TableField tableField) {
            String annotationColumnName = tableField.getAnnotationColumnName();
            if (autoDelimitKeywords.contains(annotationColumnName)) {
                tableField.setColumnName("`" + annotationColumnName + "`");
            }
            return super.handle(tableInfo, tableField);
        }
    }
}
