import type { NavigationGuardNext, RouteLocationNormalized, Router } from 'vue-router'

/**
 * 登录后跳转逻辑
 *
 * e.g.
 * - 正常跳转
 * http://localhost:5983/manage/login?redirectTo=/system/user/manage
 *
 * - 如果 redirectTo 的地址中包含 login 本身, 那么直接跳转 Dashboard 页
 * http://localhost:5983/manage/login?redirectTo=/login?redirectTo=/system/user/manage
 *
 * @param router
 * @returns routerToParam | undefined
 */
export function redirectAfterLogin(router: Router): void {
  const fullPath: string | undefined = router.currentRoute?.value?.query?.redirectTo as string | undefined
  const path: string | undefined = fullPath?.split('?')[0]

  if (path && !path.includes('/login')) {
    router.push(fullPath!)
  } else {
    router.push({ name: 'Dashboard' })
  }
}

/**
 * 若存在 redirectTo 参数则跳转传入页面，否则跳到 dashboard 页面
 *
 * 函数调用时机：网页 router.beforeEach 时机，同时满足
 * - 已登录
 * - 在登录页
 *
 * @param router
 */
export function redirectAfterLoginBeforeRoute(to: RouteLocationNormalized, next: NavigationGuardNext): void {
  const fullPath: string | undefined = to.query?.redirectTo as string | undefined
  const path: string | undefined = fullPath?.split('?')[0]

  if (path && !path.includes('/login')) {
    next(fullPath!)
  } else {
    next({ name: 'Dashboard' })
  }
}

/**
 * 携带 redirectTo 参数跳转登录页
 *
 * 函数调用时机：网页 router.beforeEach 时机，同时满足
 * - 未登录
 * - 不在登录页
 *
 * @param router
 */
export function redirectToLoginBeforeRoute(/*router: Router,*/ from: RouteLocationNormalized, next: NavigationGuardNext) {
  // router.currentRoute.value === from
  next({
    name: 'Login',
    query: {
      // 2025.08.26 route.path 改为 route.fullPath (支持携带 hash 路由参数)
      redirectTo: from.fullPath,
    },
  })
}
