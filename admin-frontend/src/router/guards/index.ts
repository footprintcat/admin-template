import type { Router } from 'vue-router'
import { createCheckLoginGuard } from './check-login'
import { createCheckPermissionGuard } from './check-permission'
import { createFooBarGuard } from './foo-bar'
import { createSiteTitleUpdateGuardGuard } from './site-title-update-guard'

/**
 * 路由守卫合并入口
 *
 * 此处定义的路由守卫将被 router/index.ts 引入
 */
export function setupRouterGuards(router: Router) {

  createSiteTitleUpdateGuardGuard(router)

  // 先校验登录态，再校验权限
  createCheckLoginGuard(router)
  createCheckPermissionGuard(router)

  createFooBarGuard(router)

}
