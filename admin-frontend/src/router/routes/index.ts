import type { RouteRecordRaw } from 'vue-router'
import frameworkRoute from './framework'

/**
 * 业务路由定义 (合并入口)
 *
 * 此处定义的路由将被 router/index.ts 引入
 */
const routeList: Array<RouteRecordRaw> = [

  {
    path: '/',
    redirect: '/dashboard',
  },

  // 框架基础页面路由
  ...frameworkRoute,

  // 在此处定义您的页面路由
  // 建议按照业务模块创建不同的 router/routes/*.ts, 然后在此处统一引入

  // 访问不存在的路径时，跳转到首页
  {
    path: '/:pathMatch(.*)*',
    redirect: '/', // 重定向到根路径
  },

]

export default routeList
