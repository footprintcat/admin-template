import type { RouteRecordRaw } from 'vue-router'

/**
 * 框架路由
 */
export const frameworkRoute: Array<RouteRecordRaw> = [
  {
    path: 'dashboard',
    name: 'Dashboard', // 请不要修改此 name
    meta: {
      title: '系统首页',
      // 不添加 permission 则所有用户都可访问
      // permission: 'dashboard',
    },
    component: () => import('@/views/core/dashboard.vue'),
  },
]
