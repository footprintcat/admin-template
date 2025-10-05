import type { RouteRecordRaw } from 'vue-router'
import frameworkRoute from './framework'

/**
 * 业务路由定义 (合并入口)
 *
 * 此处定义的路由将被 router/index.ts 引入
 */
const routeList: Array<RouteRecordRaw> = [

  ...frameworkRoute,

  // 在此处定义您的页面路由
  // 建议按照业务模块创建不同的 router/routes/*.ts, 然后在此处统一引入

]

export default routeList
