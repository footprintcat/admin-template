import { createRouter, createWebHistory } from 'vue-router'
import { setupRouterGuards } from './guards'
import routeList from './routes'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 建议在 routeList 中定义您的业务路由，方便后续框架代码更新
    ...routeList,
  ],
})

setupRouterGuards(router)

export default router
