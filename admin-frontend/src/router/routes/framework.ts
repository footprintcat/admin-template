import type { RouteRecordRaw } from 'vue-router'

/**
 * 框架路由
 */
const frameworkRoute: Array<RouteRecordRaw> = [
  {
    path: 'dashboard',
    name: 'Dashboard', // 请不要修改此 name
    meta: {
      title: '系统首页',
      permission: 'dashboard',
    },
    component: () => import('@/views/core/dashboard.vue'),
  },
]

export default frameworkRoute
