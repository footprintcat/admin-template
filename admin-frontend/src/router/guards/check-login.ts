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

    // 网页加载后先确保用户信息、用户身份已初始化
    await userStore.init({ reInit: false })
    await identityStore.init({ reInit: false })

    const isLogin = userStore.isLogin
    const isInLoginPage = to.name === 'Login'
    const isInChooseIdentityPage = to.name === 'ChooseIdentity'
    // console.log('createCheckLoginGuard -> isLogin:', isLogin, 'isInLoginPage:', isInLoginPage, 'isInChooseIdentityPage:', isInChooseIdentityPage)

    if (isLogin) {
      if (isInLoginPage) {
        // 已登录, 访问登录页时跳转
        // console.log('已登录, 在登录页')
        // 如果有 redirect_url 则跳转，否则跳到 dashboard
        redirectAfterLoginBeforeRoute(to, next)
        return
      } else if (isInChooseIdentityPage) {
        // 已登录, 在切换身份页
        // console.log('已登录, 在切换身份页')
        // 不做跳转，在页面中点击才跳转
      } else {
        // 已登录, 不在登录页、切换身份页
        // console.log('已登录, 不在登录页、切换身份页')
        if (!identityStore.currentIdentity) {
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
