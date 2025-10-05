import type { RouteRecordRaw } from 'vue-router'
import AppLayout from '@/layouts/AppLayout.vue'

/**
 * 框架路由
 */
const frameworkRoute: Array<RouteRecordRaw> = [
  // 在此处定义您的页面路由
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/',
    name: 'Home',
    component: AppLayout,
    children: [
      {
        path: 'dashboard',
        name: 'dashboard',
        meta: {
          title: '系统首页',
          permiss: 'dashboard',
        },
        component: () => import('@/views/core/dashboard.vue'),
      },
    ],
  },
]

export default frameworkRoute
