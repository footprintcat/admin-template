import type { RouteRecordRaw } from 'vue-router'

/**
 * 框架路由
 */
const frameworkRoute: Array<RouteRecordRaw> = [
  {
    path: 'dashboard',
    name: 'dashboard',
    meta: {
      title: '系统首页',
      permission: 'dashboard',
    },
    component: () => import('@/views/core/dashboard.vue'),
  },
]

export default frameworkRoute
