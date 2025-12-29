import type { Router } from 'vue-router'
import { redirectAfterLoginBeforeRoute, redirectToChooseIdentityBeforeRoute, redirectToLoginBeforeRoute } from '@/router/guards/scripts/redirect_to'
import { useIdentityStore } from '@/stores/system/identity'
import { useUserStore } from '@/stores/user'

/**
 * 用户登录校验 路由守卫
 */
export function createCheckLoginGuard(router: Router) {
  router.beforeEach(async (to, from, next) => { // router.currentRoute.value === from

    const userStore = useUserStore()
    const identityStore = useIdentityStore()

    // 网页加载后立即获取一次当前登录用户信息
    await userStore.fetchUserInfo({ skipIfExists: true })

    const isLogin = userStore.isLogin
    const isInLoginPage = to.name === 'Login'
    const isInChooseIdentityPage = to.name === 'ChooseIdentity'

    if (isLogin) {
      if (isInLoginPage) {
        // 已登录, 访问登录页时跳转
        // console.log('已登录, 在登录页')
        // 如果有 redirect_url 则跳转，否则跳到 dashboard
        redirectAfterLoginBeforeRoute(to, next)
        return
      } else {
        // 已登录, 不在登录页
        // console.log('已登录, 不在登录页')
        await identityStore.fetchIdentityInfo()

        if (!isInChooseIdentityPage && !identityStore.currentIdentity) {
          if (identityStore.userIdentityDtoList && identityStore.userIdentityDtoList.length > 1) {
            redirectToChooseIdentityBeforeRoute(to, next)
            return
          }
        }
      }
    } else {
      if (isInLoginPage) {
        // 未登录, 在登录页
        // console.log('未登录, 在登录页')
      } else {
        // 未登录, 不在登录页时跳转
        // console.log('未登录, 不在登录页')
        redirectToLoginBeforeRoute(from, next)
        return
      }
    }
    next()
  })
}
