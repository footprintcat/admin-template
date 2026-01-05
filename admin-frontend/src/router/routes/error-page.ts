import type { RouteRecordRaw } from 'vue-router'

/**
 * 错误页路由
 */
export const errorPageRoute: Array<RouteRecordRaw> = [
  {
    path: '/403',
    name: '403',
    meta: {
      title: '无权访问',
      isErrorPage: true, // isErrorPage=true 的页面不会在tab标题栏展示
    },
    component: () => import('@/views/core/error-page/403.vue'),
  },
  {
    path: '/404',
    name: '404',
    meta: {
      title: '页面不存在',
      isErrorPage: true, // isErrorPage=true 的页面不会在tab标题栏展示
    },
    component: () => import('@/views/core/error-page/404.vue'),
  },
]
