/**
 * MapStruct 转换 Mapper 类 interface 定义
 * <p>
 * `@Mapper` 注解属性含义：
 * - componentModel = "spring"：     使用 Spring 方式注入类（配合 `uses = {ConvertHelper.class}` 属性）
 * - uses = {ConvertHelper.class}：  使用 @Autowired 注入 ConvertHelper (ConvertHelper 上需有 @Component 注解)
 * - unmappedTargetPolicy = ReportingPolicy.IGNORE：编译打包时提示 WARNING 问题（source 中存在但 target 中不存在的字段）
 */
package com.example.backend.modules.system.model.converter;
