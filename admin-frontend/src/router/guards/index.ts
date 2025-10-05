import type { Router } from 'vue-router'
import { createFooBarGuard } from './foo-bar'

/**
 * 路由守卫合并入口
 *
 * 此处定义的路由守卫将被 router/index.ts 引入
 */
export function setupRouterGuards(router: Router) {

  createFooBarGuard(router)

}
