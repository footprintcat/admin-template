import type { NavigationGuardNext, RouteLocationNormalized, Router } from 'vue-router'

/**
 *
 * 未登录、已登录未选择身份、已登录已选择身份
 *
 * e.g. 正常页跳转
 *
 * - 正常页面
 *   http://localhost:5983/manage/system/user/manage
 *   http://localhost:5983/manage/login
 *   http://localhost:5983/manage/choose-identity
 *
 *
 * e.g. 登录页跳转
 *
 * - 正常跳转
 *    http://localhost:5983/manage/login?redirectTo=/system/user/manage
 *
 * - redirectTo 的地址中包含 login 本身 / choose-identity -> 应直接跳转 Dashboard 页
 *    http://localhost:5983/manage/login?redirectTo=/login?redirectTo=/system/user/manage
 *    http://localhost:5983/manage/login?redirectTo=/choose-identity
 *    http://localhost:5983/manage/login?redirectTo=/choose-identity?redirectTo=/system/user/manage
 *
 *
 * e.g. 选择身份页跳转
 *
 * - 正常跳转
 *    http://localhost:5983/manage/choose-identity?redirectTo=/system/user/manage
 *    http://localhost:5983/manage/choose-identity?redirectTo=/dashboard
 *
 * - redirectTo 的地址中包含 choose-identity 本身 / login -> 应直接跳转 Dashboard 页
 *    http://localhost:5983/manage/choose-identity?redirectTo=/choose-identity
 *    http://localhost:5983/manage/choose-identity?redirectTo=/choose-identity?redirectTo=/system/user/manage
 *    http://localhost:5983/manage/choose-identity?redirectTo=/login
 *    http://localhost:5983/manage/choose-identity?redirectTo=/login?redirectTo=/system/user/manage
 *
 */

const isLogRedirectInfo: boolean = true
const LOG_PREFIX = '[redirect_to.ts]'

/**
 * 获取 redirectTo 参数
 *
 * @param router
 * @returns
 * @since 2025-12-26
 */
export function getRedirectToParam(router: Router): string | undefined {
  const route = router.currentRoute?.value
  return route?.query?.redirectTo as string | undefined
}

/**
 * 登录后跳转逻辑
 *
 * - 如果 redirectTo 的地址中包含 login 本身, 那么直接跳转默认目标
 *
 * @param router
 * @param params 跳转目标
 */
export function redirectAfterLogin(router: Router, params: { gotoChooseIdentity: boolean }): void {
  const route = router.currentRoute?.value
  const fullPath: string | undefined = route?.query?.redirectTo as string | undefined
  if (isLogRedirectInfo) {
    console.log(LOG_PREFIX, 'redirectAfterLogin -> fullPath', fullPath)
  }
  const path: string | undefined = fullPath?.split('?')[0]

  // 若跳转身份选择页
  if (params.gotoChooseIdentity) {
    // console.log('进入身份选择页')
    router.push({
      name: 'ChooseIdentity',
      query: {
        redirectTo: fullPath,
      },
    })
    return
  }

  // console.log('直接进入，无需选择身份')
  if (path && !path.includes('/login') && !path.includes('/choose-identity')) {
    router.push(fullPath!)
  } else {
    router.push({ name: 'Dashboard' })
  }
}

/**
 * 选择身份后跳转逻辑
 * 切换身份页点击返回按钮跳转逻辑
 *
 * @param router
 */
export function redirectAfterChooseIdentity(router: Router) {
  const route = router.currentRoute?.value
  const fullPath: string | undefined = route?.query?.redirectTo as string | undefined
  if (isLogRedirectInfo) {
    console.log(LOG_PREFIX, 'redirectAfterChooseIdentity -> fullPath', fullPath)
  }
  const path: string | undefined = fullPath?.split('?')[0]

  if (path && !path.includes('/login') && !path.includes('/choose-identity')) {
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
 * @param to
 * @param next
 */
export function redirectAfterLoginBeforeRoute(to: RouteLocationNormalized, next: NavigationGuardNext): void {
  const fullPath: string | undefined = to.query?.redirectTo as string | undefined
  if (isLogRedirectInfo) {
    console.log(LOG_PREFIX, 'redirectAfterLoginBeforeRoute -> fullPath', fullPath)
  }
  const path: string | undefined = fullPath?.split('?')[0]

  if (path && !path.includes('/login') && !path.includes('/choose-identity')) {
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
 * @param from
 * @param next
 * @since 2025-12-24
 */
export function redirectToLoginBeforeRoute(/*router: Router,*/ from: RouteLocationNormalized, next: NavigationGuardNext) {
  if (isLogRedirectInfo) {
    console.log(LOG_PREFIX, 'redirectToLoginBeforeRoute')
  }
  // router.currentRoute.value === from
  next({
    name: 'Login',
    query: {
      // 2025.08.26 route.path 改为 route.fullPath (支持携带 hash 路由参数)
      redirectTo: from.fullPath,
    },
  })
}

/**
 * 携带 redirectTo 参数跳转身份选择页
 *
 * 函数调用时机：网页 router.beforeEach 时机，同时满足
 * - 已登录
 * - 未选择身份
 * - 不在身份选择页
 *
 * @param from
 * @param next
 * @since 2025-12-26
 */
export function redirectToChooseIdentityBeforeRoute(/*router: Router,*/ from: RouteLocationNormalized, next: NavigationGuardNext) {
  if (isLogRedirectInfo) {
    console.log(LOG_PREFIX, 'redirectToChooseIdentityBeforeRoute')
  }
  // router.currentRoute.value === from
  next({
    name: 'ChooseIdentity',
    query: {
      // 2025.08.26 route.path 改为 route.fullPath (支持携带 hash 路由参数)
      redirectTo: from.fullPath,
    },
  })
}

/**
 * 跳转登录页
 *
 * @param router
 * @since 2025-12-27
 */
export function redirectToLogin(router: Router, redirectParam: boolean) {
  router.push({
    name: 'Login',
    query: {
      ...redirectParam ? {
        // 2025.08.26 route.path 改为 route.fullPath (支持携带 hash 路由参数)
        redirectTo: router.currentRoute.value.fullPath, // window.location.href
      } : {},
    },
  })
}


/**
 * 跳转到切换身份页
 *
 * @param router
 * @since 2025-12-27
 */
export function redirectToChooseIdentity(router: Router, redirectParam: boolean) {
  router.push({
    name: 'ChooseIdentity',
    query: {
      ...redirectParam ? {
        redirectTo: router.currentRoute.value.fullPath,
      } : {},
    },
  })
}
