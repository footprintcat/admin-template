import type { Router } from 'vue-router'
import { systemUserAuthLogout } from '@/api/system/user-auth'
import { redirectToLogin } from '@/router/guards/scripts/redirect_to'
import { useIdentityStore } from '@/stores/system/identity'
import { useTabsStore } from '@/stores/tabs'
import { useUserStore } from '@/stores/user'
// import { clearFrontendLocalStorage } from './local_storage_util'

export function userLogout(router: Router, includeRedirectToParamWhenRedirect: boolean) {

  // 清除身份信息 (需要在退出登录接口调用前)
  const identityStore = useIdentityStore()
  identityStore.exitCurrentIdentity(true)

  // 清除登录信息
  const userStore = useUserStore()
  userStore.clear()
  identityStore.clearIdentityList()

  // 发送退出登录请求
  systemUserAuthLogout()

  // 关闭全部标签 (销毁页面对象)
  const tabs = useTabsStore()
  tabs.clearTabs()

  // // 清除本地 localStorage
  // clearFrontendLocalStorage()

  // 跳转到登录页面
  redirectToLogin(router, includeRedirectToParamWhenRedirect)
}
