/**
 * MapStruct 转换 Mapper 类 interface 定义
 * <p><br>
 * `@Mapper` 注解属性含义：<br>
 * - componentModel = "spring"：     使用 Spring 方式注入类（配合 `uses = {ConvertHelper.class}` 属性）<br>
 * - uses = {ConvertHelper.class}：  使用 @Autowired 注入 ConvertHelper (ConvertHelper 上需有 @Component 注解)<br>
 * - unmappedTargetPolicy = ReportingPolicy.IGNORE：编译打包时提示 WARNING 问题（source 中存在但 target 中不存在的字段）<br>
 *
 * <p>
 * 如果不使用 spring 方式，那么需要在 Converter 类中添加
 * <code>XxxConverter INSTANCE = Mappers.getMapper(XxxConverter.class);</code>
 * <p>
 * 用法：
 * <pre>
 * XxxDto xxxDTO = XxxConverter.INSTANCE.toDto(xxx);
 * </pre>
 * 如果使用 spring 方式，那么不能通过 INSTANCE 属性访问，否则会找不到 ConvertHelper 类的实例
 * <p>
 * 用法：
 * <pre>
 * &#064;Resource
 * private XxxConverter xxxConverter;
 *
 * XxxDto xxxDTO = xxxConverter.toDto(xxx);
 * </pre>
 *
 */
package com.example.backend.modules.system.model.converter;
