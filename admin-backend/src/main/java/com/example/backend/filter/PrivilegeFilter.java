package com.example.backend.filter;

import com.alibaba.fastjson2.JSONObject;
import com.example.backend.common.Error.BusinessErrorCode;
import com.example.backend.common.Response.CommonReturnType;
import com.example.backend.common.Utils.SessionUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j

@WebFilter(filterName = "Privilege", urlPatterns = {
        "/manage/*",
        "/system/user"
})
// @WebFilter(filterName = "Privilege", urlPatterns = "/*", asyncSupported = true)
public class PrivilegeFilter implements Filter {

    @Resource
    DataSource dataSource;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("PrivilegeFilter init.");
        System.out.println("当前读取的配置文件: " + configName);
        System.out.println("当前环境(project-config.env): " + env);
        System.out.println("数据库连接URL: " + datasourceUrl);

        System.out.println("当前使用数据源: " + dataSource.getClass());
        System.out.println("druid 后台：http://localhost:8080/druid/index.html");

        if ("develop".equals(env)) {
            System.out.println("调试模式下，临时启用部分接口权限");
            openAuthorityPathPrefix.add("/v1/unifiedMaintenance");
        }
    }

    @Value("${project-config.config-name}")
    String configName = "";

    @Value("${spring.datasource.url}")
    String datasourceUrl = "";

    @Value("${project-config.env}")
    String env = "";

    private static final List<String> openAuthorityApi = new ArrayList<>();
    private static final List<String> openAuthorityPathPrefix = new ArrayList<>();

    static {
        // 公共接口
        openAuthorityPathPrefix.add("/v3/public");
        // 后台管理
        openAuthorityApi.add("/v1/user/login");
        openAuthorityApi.add("/v1/role/list");
        openAuthorityApi.add("/v1/user/logout");
        openAuthorityPathPrefix.add("/v2/ws/foo-bar/"); // 放开 WebSocket 权限 (调用方：后台管理, ...)
        // TODO 部署时要删掉 Swagger 访问权限
        openAuthorityPathPrefix.add("/swagger-ui/");
        openAuthorityPathPrefix.add("/swagger-resources");
        openAuthorityPathPrefix.add("/v3/api-docs");
        // 调试用接口
        openAuthorityPathPrefix.add("/test");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // System.out.println("[PrivilegeFilter] doFilter(): ");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession();

        String method = httpServletRequest.getMethod();
        String origin = httpServletRequest.getHeader("Origin");
        String requestURI = httpServletRequest.getRequestURI();
        // System.out.println("     method, origin, requestURI: " + method + "  " + origin + "  " + requestURI);
        log.info("[{}] {}", method, requestURI);

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 响应头
        httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.addHeader("Access-Control-Request-Headers", "content-type");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "content-type");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

        // 检查请求方法是否为OPTIONS
        if ("OPTIONS".equalsIgnoreCase(httpServletRequest.getMethod())) {
            // 直接返回200 OK响应
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 匹配前缀
        List<String> matchedPrefixList = openAuthorityPathPrefix.stream().filter(requestURI::startsWith).toList();
        if (matchedPrefixList.isEmpty() && !openAuthorityApi.contains(requestURI) && !SessionUtils.isLogin(session)) {
            // 没有权限
            // System.out.println("     没有权限 requestURI: " + requestURI);

            // 响应头
            httpServletResponse.addHeader("content-type", "application/json; charset=utf-8");

            // 返回“用户未登录”
            HashMap<String, Object> map = new HashMap<>();
            map.put("errCode", BusinessErrorCode.USER_NOT_LOGIN.getErrCode());
            map.put("errMsg", BusinessErrorCode.USER_NOT_LOGIN.getErrMsg());
            CommonReturnType commonReturnType = CommonReturnType.error(map, "用户未登录");
            String jsonString = JSONObject.toJSONString(commonReturnType);

            servletResponse.resetBuffer();
            servletResponse.getWriter().print(jsonString);
            servletResponse.flushBuffer();
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        System.out.println("PrivilegeFilter destroy");
    }
}
