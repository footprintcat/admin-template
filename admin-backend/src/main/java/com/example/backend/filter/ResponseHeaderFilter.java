package com.example.backend.filter;

import com.example.backend.service.SystemLogServiceBak;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;

@Slf4j
@WebFilter(filterName = "ResponseHeader", urlPatterns = "/*")
public class ResponseHeaderFilter implements Filter {

    private static final String filterName = "ResponseHeaderFilter";

    @Lazy
    @Resource
    SystemLogServiceBak systemLogService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        log.info("{} init.", filterName);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession();
        String userAgent = httpServletRequest.getHeader("User-Agent");

        String method = httpServletRequest.getMethod();
        String origin = httpServletRequest.getHeader("Origin");
        String requestURI = httpServletRequest.getRequestURI();
        log.info("[{}] doFilter(): method, origin, requestURI: {}  {}  {}", filterName, method, origin, requestURI);

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 响应头
        httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
        httpServletResponse.addHeader("Access-Control-Request-Headers", "content-type");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "content-type");
        httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

        filterChain.doFilter(servletRequest, servletResponse);

        // 记录日志
        if (!"OPTIONS".equals(method)) {
            systemLogService.log(session, "Call Api", "[" + method + "] " + requestURI, userAgent, "");
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
        log.info("{} destroy.", filterName);
    }
}
