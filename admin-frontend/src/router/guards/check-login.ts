import type { Router } from 'vue-router'
import { usePermissionStore } from '@/stores/permission'
import useUserStore from '@/stores/user'

/**
 * 用户登录权限校验 路由守卫
 */
export function createCheckLoginGuard(router: Router) {
  router.beforeEach((to, from, next) => {

    const userStore = useUserStore()
    const permissionStore = usePermissionStore()

    if (!userStore.isLogin() && to.name !== 'Login') {
      next({
        name: 'Login',
        query: {
          // 2025.08.26 route.path 改为 route.fullPath (支持携带 hash 路由参数)
          redirectTo: router.currentRoute.value.fullPath,
        },
      })
    } else if (to.meta.permission && !permissionStore.key.includes(to.meta.permission)) {
      // 如果没有权限，则进入403
      to.meta.showErrorPage = true
      to.meta.errorCode = 403
      next()
      // next('/403')
    } else {
      to.meta.showErrorPage = false
      to.meta.errorCode = undefined
      next()
    }
  })
}
