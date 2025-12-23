package com.example.backend.common.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistration;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
// 不在此处使用 Servlet @WebFilter 注解，改为使用 @Component + @FilterRegistration 方式加载
@Component
@FilterRegistration(order = 10, urlPatterns = "/*")
public class LogApiPathFilter implements Filter {

    private static final String FILTER_NAME = "LogApiPathFilter";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("{} init.", FILTER_NAME);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // log.info("[{}] doFilter(): ", filterName);

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        // HttpSession session = httpServletRequest.getSession();
        // String userAgent = httpServletRequest.getHeader("User-Agent");

        String method = httpServletRequest.getMethod();
        // String origin = httpServletRequest.getHeader("Origin");
        String requestURI = httpServletRequest.getRequestURI();
        // log.info("[{}] doFilter(): method, origin, requestURI: {}  {}  {}", filterName, method, origin, requestURI);

        if (!"OPTIONS".equalsIgnoreCase(method)) {
            log.info("[{}] {}", method, requestURI);
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("{} destroy.", FILTER_NAME);
    }
}
