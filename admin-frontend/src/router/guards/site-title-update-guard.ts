import type { Router } from 'vue-router'
import settings from '@/utils/settings'

/**
 * 网页标题更新路由守卫
 */
export function createSiteTitleUpdateGuardGuard(router: Router) {
  router.beforeEach((to) => {
    let title: string
    if (to.meta.title) {
      title = `${to.meta.title} - ${settings.siteTitle}`
    } else {
      title = settings.siteTitle
    }

    document.title = settings.isLAN ? '[DEV] ' + title : title
  })
}
