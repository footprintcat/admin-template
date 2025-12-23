package com.example.backend.common.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加此注解的 Controller 会对抛出的异常进行全局处理
 *
 * @since 2025-12-18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleControllerGlobalException {
}
