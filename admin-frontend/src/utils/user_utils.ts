import type { Router } from 'vue-router'
import { redirectToLogin } from '@/router/guards/scripts/redirect_to'
import { useIdentityStore } from '@/stores/system/identity'
import { useTabsStore } from '@/stores/tabs'
import { useUserStore } from '@/stores/user'

export async function userLogout(router: Router, includeRedirectToParamWhenRedirect: boolean) {

  // 关闭全部标签 (销毁页面对象)
  const tabs = useTabsStore()
  tabs.clearTabs()

  // 清除身份信息 (需要在退出登录接口调用前)
  const identityStore = useIdentityStore()
  await identityStore.clear({ exit: true })

  // 清除登录信息
  const userStore = useUserStore()
  await userStore.clear({ exit: true })

  // 跳转到登录页面
  redirectToLogin(router, includeRedirectToParamWhenRedirect)
}
