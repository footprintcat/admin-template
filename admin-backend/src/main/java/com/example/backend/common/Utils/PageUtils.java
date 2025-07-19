// package com.example.backend.common.Utils;
//
// import com.github.pagehelper.PageInfo;
// import org.springframework.beans.BeanUtils;
//
// import java.util.List;
// import java.util.function.Function;
// import java.util.stream.Collectors;
//
// public class PageUtils {
//
//     /**
//      * 转换PageInfo中的List
//      *
//      * @param pageInfoPo 原来的pageInfo
//      * @param convert    转换方式
//      * @param <P>        原来的类型
//      * @param <V>        转换后的类型
//      * @return 转换后的pageInfo
//      */
//     public static <P, V> PageInfo<V> convert(PageInfo<P> pageInfoPo, Function<P, V> convert) {
//         //视图pageInfo
//         PageInfo<V> vPageInfo = new PageInfo<>();
//         //copy属性
//         BeanUtils.copyProperties(pageInfoPo, vPageInfo);
//         //转化
//         List<V> vList = pageInfoPo.getList().stream().map(convert).collect(Collectors.toList());
//         //赋值
//         vPageInfo.setList(vList);
//         return vPageInfo;
//     }
// }