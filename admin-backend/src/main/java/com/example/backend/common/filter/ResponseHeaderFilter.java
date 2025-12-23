package com.example.backend.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
// 不在此处使用 Servlet @WebFilter 注解，改为使用 @Component + @FilterRegistration 方式加载
// docs:
// - Registering Servlets, Filters, and Listeners as Spring Beans
//   https://docs.spring.io/spring-boot/reference/web/servlet.html#web.servlet.embedded-container.servlets-filters-listeners.beans
@Component
@FilterRegistration(order = 20, urlPatterns = "/*")
// @WebFilter(filterName = "ResponseHeader", urlPatterns = "/*")
public class ResponseHeaderFilter implements Filter {

    private static final String FILTER_NAME = "ResponseHeaderFilter";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("{} init.", FILTER_NAME);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("[{}] doFilter(): ", FILTER_NAME);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String method = httpServletRequest.getMethod();
        String origin = httpServletRequest.getHeader("Origin");

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 响应头
        // 在 Filter 中设置如下 header，就不需要在每个 Controller 类上写 @CrossOrigin 注解了
        httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.addHeader("Access-Control-Request-Headers", "content-type");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "content-type");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

        if ("OPTIONS".equalsIgnoreCase(method)) {
            // 直接返回 200 OK 响应
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);

        // TODO 记录日志
        // systemLogService.log(session, "Call Api", "[" + method + "] " + requestURI, userAgent, "");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("{} destroy.", FILTER_NAME);
    }
}
