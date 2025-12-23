import type { Router } from 'vue-router'
import { redirectAfterLoginBeforeRoute, redirectToLoginBeforeRoute } from '@/router/guards/scripts/redirect_to'
import { usePermissionStore } from '@/stores/permission'
import { useUserStore } from '@/stores/user'

/**
 * 用户登录权限校验 路由守卫
 */
export function createCheckLoginGuard(router: Router) {
  router.beforeEach(async (to, from, next) => { // router.currentRoute.value === from

    const userStore = useUserStore()
    const permissionStore = usePermissionStore()

    // 网页加载后立即获取一次当前登录用户信息
    await userStore.fetchUserInfo({ skipWhenExists: true })

    const isLogin = userStore.isLogin
    const isInLoginPage = to.name === 'Login'

    if (isLogin) {
      if (isInLoginPage) {
        // 已登录, 访问登录页时跳转
        console.log('已登录, 在登录页')
        // TODO 如果有 redirect_url 则跳转，否则跳到 dashboard
        redirectAfterLoginBeforeRoute(to, next)
        return
      } else {
        // 已登录, 不在登录页
        console.log('已登录, 不在登录页')
      }
    } else {
      if (isInLoginPage) {
        // 未登录, 在登录页
        console.log('未登录, 在登录页')
      } else {
        // 未登录, 不在登录页时跳转
        console.log('未登录, 不在登录页')
        redirectToLoginBeforeRoute(from, next)
        return
      }
    }

    // TODO
    /*
    if (to.meta.permission && !permissionStore.key.includes(to.meta.permission)) {
      // 如果没有权限，则进入403
      to.meta.showErrorPage = true
      to.meta.errorCode = 403
      next()
      // next('/403')
    }
    */

    to.meta.showErrorPage = false
    to.meta.errorCode = undefined
    next()
  })
}
