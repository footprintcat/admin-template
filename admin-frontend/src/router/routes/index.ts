import type { RouteRecordRaw } from 'vue-router'
import AppLayout from '@/layouts/AppLayout.vue'
import { errorPageRoute } from './error-page'
import { frameworkRoute } from './framework'
import { systemRoute } from './system'

/**
 * 业务路由定义 (合并入口)
 *
 * 此处定义的路由将被 router/index.ts 引入
 */
const routeList: Array<RouteRecordRaw> = [

  {
    path: '/',
    redirect: {
      name: 'Dashboard',
    },
  },

  {
    path: '/',
    name: 'Home',
    component: AppLayout,
    children: [
      // 在此处定义您的页面路由
      // 建议按照业务模块创建不同的 router/routes/*.ts, 然后在此处统一引入

      // 框架基础页面路由
      ...frameworkRoute,

      // system 模块路由
      ...systemRoute,
    ],
  },

  {
    path: '/login',
    // guards 中通过此 name 判断当前是否是登录页面，如果不需要调整登录逻辑，请不要修改这个 name
    name: 'Login',
    meta: {
      title: '登录',
    },
    component: () => import('@/views/global/login/login-wrapper.vue'),
  },

  {
    path: '/choose-identity',
    name: 'ChooseIdentity',
    meta: {
      title: '选择身份',
    },
    component: () => import('@/views/global/choose-identity/choose-identity.vue'),
  },

  {
    path: '/',
    name: 'ErrorPage',
    component: AppLayout,
    children: [
      // 框架错误页路由 (需放在最后)
      ...errorPageRoute,
    ],
  },

  // 这里不应该添加通配路由, 否则将无法进入 errorPageRoute
  /*
  // 访问不存在的路径时，跳转到首页
  {
    path: '/:pathMatch(.*)*',
    redirect: '/', // 重定向到根路径
  },
  */

]

export default routeList
