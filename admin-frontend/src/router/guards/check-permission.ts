import type { Router } from 'vue-router'
import { usePermissionStore } from '@/stores/permission'
import { useErrorPage } from '@/views/core/error-page/useErrorPage'

const { setErrorCode } = useErrorPage()

/**
 * 用户权限校验 路由守卫
 */
export function createCheckPermissionGuard(router: Router) {
  router.beforeEach(async (to, from, next) => { // router.currentRoute.value === from

    // 检查页面是否存在
    if (to.matched.length === 0) {
      setErrorCode('404', to)
      // 跳到 404 页，这样在点击上一个页面还能回来（不然 router path 没变化不会进行跳转）
      next({
        name: '404',
        query: {
          // 添加一个 path 参数, 这样在不同的 404 页面之间也可以跳转
          path: to.path,
        },
      })
      return
    }

    // const userStore = useUserStore()
    const permissionStore = usePermissionStore()

    const targetPagePermission: string = to.meta.permission as string

    const isNeedPermission: boolean = !!targetPagePermission
    const isIdentityHasPermission: boolean = permissionStore.checkMenuPermission(targetPagePermission)
    // console.log('isNeedPermission', isNeedPermission)
    // console.log('isIdentityHasPermission', isIdentityHasPermission)

    if (isNeedPermission && !isIdentityHasPermission) {
      // 如果没有权限，则进入403
      setErrorCode('403', to)
      // next(false)
      next({
        name: '403',
        query: {
          // 添加一个 path 参数, 这样在不同的 403 页面之间也可以跳转
          path: to.path,
        },
      })
      return
    }

    setErrorCode(null, to)
    next()
  })
}
