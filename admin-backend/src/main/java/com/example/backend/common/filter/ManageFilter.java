package com.example.backend.common.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;

@Slf4j
// 不在此处使用 Servlet @WebFilter 注解，改为使用 @Component + @FilterRegistration 方式加载
// docs:
// - Registering Servlets, Filters, and Listeners as Spring Beans
//   https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.embedded-container.servlets-filters-listeners.beans
@Component
@FilterRegistration(order = 30, urlPatterns = {
        "/manage/*"
})
// @WebFilter(filterName = "Privilege", urlPatterns = { "/manage/*" })
// @WebFilter(filterName = "Privilege", urlPatterns = "/*", asyncSupported = true)
public class ManageFilter implements Filter {

    private static final String FILTER_NAME = "ManageFilter";

    @Resource
    DataSource dataSource;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("PrivilegeFilter init.");
        // System.out.println("当前环境(project-config.env): " + env);
        // System.out.println("数据库连接URL: " + datasourceUrl);

        // System.out.println("当前使用数据源: " + dataSource.getClass());
    }

    // @Value("${spring.datasource.url}")
    // String datasourceUrl = "";

    // @Value("${project-config.env}")
    // String env = "";

    // private static final List<String> openAuthorityApi = new ArrayList<>();
    // private static final List<String> openAuthorityPathPrefix = new ArrayList<>();

    // static {
    //     // TODO 部署时要删掉 Swagger 访问权限
    //     openAuthorityPathPrefix.add("/swagger-ui/");
    //     openAuthorityPathPrefix.add("/swagger-resources");
    //     openAuthorityPathPrefix.add("/v3/api-docs");
    // }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        log.info("[{}] doFilter(): ", FILTER_NAME);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("{} destroy.", FILTER_NAME);
    }
}
