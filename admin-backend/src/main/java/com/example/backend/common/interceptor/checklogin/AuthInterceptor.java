package com.example.backend.common.interceptor.checklogin;

import com.alibaba.fastjson2.JSONObject;
import com.example.backend.common.annotations.NoNeedIdentity;
import com.example.backend.common.annotations.PublicAccess;
import com.example.backend.common.baseobject.response.CommonReturn;
import com.example.backend.common.error.BusinessErrorCode;
import com.example.backend.common.utils.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 登录校验
 *
 * @since 2015-12-14
 */
@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final Set<String> IDENTITY_RELATED_PATHS = Set.of(
            "/manage/v1/system/identity/switch"
    );

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {
        // 如果不是HandlerMethod，直接放行（如静态资源）
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        Method method = handlerMethod.getMethod();

        // 检查方法上是否有 @PublicAccess 注解
        if (method.isAnnotationPresent(PublicAccess.class)) {
            // 有注解，直接放行，不需要登录
            log.info("@PublicAccess 接口直接放行");
            return true;
        }

        HttpSession session = request.getSession();

        // 没有注解，进行登录验证
        if (!SessionUtils.isLogin(session)) {
            // 没有权限
            // System.out.println("     没有权限 requestURI: " + requestURI);

            // 响应头
            response.addHeader("content-type", "application/json; charset=utf-8");

            // 返回“用户未登录”
            CommonReturn commonReturn = CommonReturn.error(BusinessErrorCode.USER_NOT_LOGIN, "用户未登录");
            String jsonString = JSONObject.toJSONString(commonReturn);

            response.resetBuffer();
            response.getWriter().print(jsonString);
            response.flushBuffer();
            return false;
        }

        // 检查方法上是否有 @NoNeedIdentity 注解
        boolean isIdentityFree = method.isAnnotationPresent(NoNeedIdentity.class);
        if (!isIdentityFree && SessionUtils.getIdentityId(session) == null) {
            response.addHeader("content-type", "application/json; charset=utf-8");

            // 返回“请先选择登录身份”
            CommonReturn commonReturn = CommonReturn.error(BusinessErrorCode.USER_NOT_SELECT_IDENTITY, "请先选择登录身份");
            String jsonString = JSONObject.toJSONString(commonReturn);

            response.resetBuffer();
            response.getWriter().print(jsonString);
            response.flushBuffer();
            return false;
        }

        return true;
    }
}
