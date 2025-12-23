package com.example.backend.common.component;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 项目启动后打印项目信息
 *
 * @since 2025-12-23
 */
@Component
public class ProjectConfigPrinter implements ApplicationRunner {

    private final ConfigurableEnvironment environment;
    private final DataSource dataSource;

    public ProjectConfigPrinter(ConfigurableEnvironment environment, DataSource dataSource) {
        this.environment = environment;
        this.dataSource = dataSource;
    }

    @Override
    public void run(@NonNull ApplicationArguments args) {
        final String sepLine = "=========================================";
        // final String sepLine = "════════════════════════════════════════";

        System.out.println(sepLine + "\n" + "🚀 系统启动成功！" + "\n" + sepLine);

        // 打印激活的 profiles
        String[] activeProfiles = environment.getActiveProfiles();
        System.out.println("当前环境: " + (activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "默认(default)"));

        // 获取并打印加载的配置文件
        Set<String> loadedConfigs = getLoadedConfigurations();
        System.out.println("已加载的配置文件：");

        if (loadedConfigs.isEmpty()) {
            System.out.println("(无)");
        } else {
            int index = 1;
            for (String config : loadedConfigs) {
                String display = "  " + index + ". " + config;
                System.out.println(display);
                index++;
            }
        }
        System.out.println(sepLine);

        // 打印关键配置
        printConfigurationSummary();
        System.out.println(sepLine);
    }

    private Set<String> getLoadedConfigurations() {
        Set<String> configFiles = new LinkedHashSet<>();
        MutablePropertySources propertySources = environment.getPropertySources();

        // 正则表达式匹配配置文件路径
        Pattern pattern = Pattern.compile("(application[^\\[\\]]*\\.(yml|yaml|properties))", Pattern.CASE_INSENSITIVE);

        for (PropertySource<?> source : propertySources) {
            String sourceName = source.getName();
            // System.out.println(sourceName);

            // 检查是否是配置文件
            if (isLikelyConfigFile(sourceName)) {
                Matcher matcher = pattern.matcher(sourceName);
                if (matcher.find()) {
                    // 找到配置文件
                    String fileName = matcher.group(1);

                    // 确定来源位置
                    String location = "classpath";
                    if (sourceName.contains("file:")) {
                        location = "file";
                    } else if (sourceName.contains("URL")) {
                        location = "external";
                    }

                    configFiles.add(fileName + " (" + location + ")");
                } else if (sourceName.contains("application") &&
                           (sourceName.contains(".yml") || sourceName.contains(".yaml") || sourceName.contains(".properties"))) {
                    // 尝试从名称中提取
                    int start = sourceName.lastIndexOf("application");
                    int end = sourceName.length();
                    if (sourceName.contains("[")) {
                        end = sourceName.indexOf("[");
                    }
                    String possibleName = sourceName.substring(start, end).trim();
                    configFiles.add(possibleName);
                }
            }
        }

        return configFiles;
    }

    private boolean isLikelyConfigFile(String sourceName) {
        return sourceName.contains("application") ||
               sourceName.contains("Config resource") ||
               (
                       sourceName.contains("class path resource") &&
                       (
                               sourceName.contains(".yml") ||
                               sourceName.contains(".yaml") ||
                               sourceName.contains(".properties")
                       )
               );
    }

    private void printConfigurationSummary() {
        System.out.println("📊 配置摘要：");

        final String appName = environment.getProperty("spring.application.name", "未配置");
        final String port = environment.getProperty("server.port");
        final String env = environment.getProperty("project-config.env");
        final String datasourceUrl = environment.getProperty("spring.datasource.url");
        final boolean springDocEnabled = Boolean.TRUE.equals(environment.getProperty("springdoc.api-docs.enabled", Boolean.class));
        final String swaggerUrl = springDocEnabled
                ? "http://localhost:" + port + "/swagger-ui/index.html"
                : "未启用";

        printConfigurationSummaryLine("应用名称", appName);
        printConfigurationSummaryLine("服务端口", port);
        printConfigurationSummaryLine("当前环境(project-config.env)", env);
        printConfigurationSummaryLine("数据库连接地址", datasourceUrl);
        printConfigurationSummaryLine("当前使用数据源", dataSource.getClass().getName());
        printConfigurationSummaryLine("接口地址", "http://localhost:" + port + "/");
        printConfigurationSummaryLine("Swagger 接口文档", swaggerUrl);
    }

    private void printConfigurationSummaryLine(@NotNull String name, @Nullable String value) {
        // System.out.println(String.format("  %-25s: %s", name, value));
        System.out.printf("  %-15s: %s%n", name, value); // 左对齐
        // System.out.printf("  %25s: %s%n", name, value); // 右对齐
    }
}
