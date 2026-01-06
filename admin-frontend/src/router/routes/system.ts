import type { RouteRecordRaw } from 'vue-router'

/**
 * system 模块路由
 */
export const systemRoute: Array<RouteRecordRaw> = [
  {
    path: 'system',
    children: [
      {
        path: 'user/manage',
        name: 'system-user-manage',
        meta: {
          title: '用户管理',
          permission: 'system:user',
        },
        component: () => import('@/views/system/user/system-user-manage.vue'),
      },
    ],
  },
]
