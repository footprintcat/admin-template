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

import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

// refer: https://baomidou.com/reference/new-code-generator-configuration/
@Deprecated
public class MybatisPlusEntityGenerator {

    private static final String DATASOURCE_URL = "jdbc:mysql://localhost:3306/admin_template" +
            "?serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true&useSSL=false&characterEncoding=UTF-8";
    private static final String DATASOURCE_USERNAME = "root";
    private static final String DATASOURCE_PASSWORD = "123456";

    // 数据库关键字（生成 entity 时，这些字段自动添加反引号 ``）
    private static final List<String> autoDelimitKeywords = Arrays.asList(
            "order", "group", "level", "timestamp", "key"
    );

    public static void main(String[] args) {
        LinkedList<String> includeTables = new LinkedList<>();
        /* 请按字母顺序添加 */
        // includeTables.add("system_config");
        // includeTables.add("system_department");
        // includeTables.add("system_identity");
        // includeTables.add("system_identity_role_relation");
        // includeTables.add("system_job_position");
        // includeTables.add("system_log");
        // // includeTables.add("system_log_detail");
        // includeTables.add("system_menu");
        // includeTables.add("system_privilege");
        // includeTables.add("system_role");
        // includeTables.add("system_tenant");
        // includeTables.add("system_user");
        // includeTables.add("system_user_auth");

        if (includeTables.isEmpty()) {
            System.out.println(" includeTables 为空，跳过该模块实体类生成");
            return;
        }

        // 获取项目路径
        String projectPath = System.getProperty("user.dir");
        String javaBasePath = projectPath + "/src/main/java";
        String resourcesBasePath = projectPath + "/src/main/resources";

        // 数据源配置
        FastAutoGenerator.create(DATASOURCE_URL, DATASOURCE_USERNAME, DATASOURCE_PASSWORD)
                .globalConfig(builder -> {
                    builder.author("coder-xiaomo")        // 设置作者
                            // .enableSwagger()        // 开启 swagger 模式 默认值:false
                            .enableSpringdoc()      // 开启 springdoc 模式 默认值:false
                            .disableOpenDir()       // 禁止打开输出目录 默认值:true
                            .commentDate("yyyy-MM-dd") // 注释日期
                            .dateType(DateType.ONLY_DATE)   // 定义生成的实体类中日期类型 DateType.ONLY_DATE 默认值: DateType.TIME_PACK
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
                            // .controller("controller") // Controller 包名 默认值:controller
                            .entity("entity") // Entity 包名 默认值:entity
                            // .service("service") // Service 包名 默认值:service
                            .serviceImpl("repository") // Service 实现类包名
                            .mapper("mapper") // Mapper 包名 默认值:mapper
                            // .moduleName("xxx") // 设置父包模块名 默认值:无
                            .pathInfo(Collections.singletonMap(OutputFile.xml, resourcesBasePath + "/mapper")); // 设置mapperXml生成路径
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

                            .controllerBuilder() // controller 策略配置
                            .disable()
                            // .formatFileName("%sController")
                            // .enableRestStyle() // 开启RestController注解
                            // .enableFileOverride()

                            .serviceBuilder() // service策略配置
                            // .disable() // = disableService + disableServiceImpl
                            // service
                            .disableService()
                            // .formatServiceFileName("%sService")
                            // serviceImpl
                            // .formatServiceImplFileName("%sServiceImpl")
                            .formatServiceImplFileName("%sRepository")
                            // .enableFileOverride()

                            .entityBuilder() // 实体类策略配置
                            .idType(IdType.ASSIGN_ID) // 主键策略  雪花算法自动生成的id
                            .addTableFills(new Column("create_time", FieldFill.INSERT)) // 自动填充配置
                            .addTableFills(new Property("update_time", FieldFill.INSERT_UPDATE))
                            .enableLombok() // 开启lombok
                            .toString(false) // 不生成 lombok 的 @ToString, 默认值: true
                            .fieldUseJavaDoc(false) // 不启用字段文档注释, 默认值: true
                            .logicDeleteColumnName("delete_time") // is_delete // 说明逻辑删除是哪个字段
                            .versionColumnName("version") // 说明乐观锁版本号是哪个字段
                            .enableTableFieldAnnotation()
                            // entity 数据库关键字字段添加 ``
                            .tableFieldAnnotationHandler(new CustomTableFieldAnnotationHandler())
                            // entity 类注解 & 字段注解排序
                            // related issue: https://github.com/baomidou/mybatis-plus/issues/6685
                            .annotationAttributesFunction(annotationAttributes -> annotationAttributes.stream()
                                    .sorted(Comparator.comparingInt((AnnotationAttributes c) -> c.getDisplayName().charAt(1))).collect(Collectors.toList()))
                            .enableFileOverride()

                            .mapperBuilder() // mapper策略配置
                            // xml
                            .formatXmlFileName("%sMapper")
                            // mapper
                            .formatMapperFileName("%sMapper")
                            // .enableMapperAnnotation() // @mapper注解开启
                            .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class) // @mapper注解开启
                            // .enableFileOverride()
                    ;
                })

                // .templateConfig(builder -> {
                //     builder.disable(TemplateType.CONTROLLER, TemplateType.SERVICE_IMPL, TemplateType.SERVICE);
                // })

                .templateEngine(new FreemarkerTemplateEngine())
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
