package com.example.backend.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebFilter(filterName = "Privilege", urlPatterns = {
        "/manage/*",
        "/system/user"
})
public class PrivilegeFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        System.out.println("PrivilegeFilter init.");
    }

    private static final List<String> openAuthorityApi = new ArrayList<>();
    private static final List<String> openAuthorityPathPrefix = new ArrayList<>();

    static {
        openAuthorityApi.add("/system/user/login");
        // openAuthorityApi.add("/v1/user/logout");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        System.out.println("[PrivilegeFilter] doFilter(): ");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpSession session = httpServletRequest.getSession();

        String method = ((HttpServletRequest) servletRequest).getMethod();
        String requestURI = httpServletRequest.getRequestURI();
        String origin = httpServletRequest.getHeader("Origin");
        System.out.println("     method, requestURI: " + method + "  " + requestURI);

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 匹配前缀
        List<String> matchedPrefixList = openAuthorityPathPrefix.stream().filter(requestURI::startsWith).toList();
        if (!"OPTIONS".equals(method) && matchedPrefixList.size() == 0 && !openAuthorityApi.contains(requestURI) &&
            Objects.isNull(session.getAttribute("userId"))) {
            System.out.println("     没有权限 requestURI: " + requestURI);

            httpServletResponse.setStatus(403);
            httpServletResponse.addHeader("content-type", "application/json; charset=utf-8");

            // 响应头
            httpServletResponse.addHeader("Access-Control-Allow-Origin", origin);
            httpServletResponse.addHeader("Access-Control-Request-Headers", "content-type");
            httpServletResponse.addHeader("Access-Control-Allow-Headers", "content-type");
            httpServletResponse.addHeader("Access-Control-Allow-Credentials", "true");

            servletResponse.resetBuffer();
            servletResponse.getWriter().print("用户未登录");
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
