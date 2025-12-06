import type { Router } from 'vue-router'
import settings from '@/utils/settings'

/**
 * 网页标题更新路由守卫
 */
export function createSiteTitleUpdateGuardGuard(router: Router) {
  router.beforeEach((to) => {
    if (to.meta.title) {
      document.title = `${to.meta.title} - ${settings.siteTitle}`
    } else {
      document.title = settings.siteTitle
    }
  })
}
