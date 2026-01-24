import type { Router } from 'vue-router'
import settings from '@/utils/settings'

const siteTitle = import.meta.env.VITE_APP_TITLE

/**
 * 网页标题更新路由守卫
 */
export function createSiteTitleUpdateGuardGuard(router: Router) {
  router.beforeEach((to) => {
    let title: string
    if (to.meta.title) {
      title = `${to.meta.title} - ${siteTitle}`
    } else {
      title = siteTitle
    }

    document.title = settings.isLAN ? '[DEV] ' + title : title
  })
}
