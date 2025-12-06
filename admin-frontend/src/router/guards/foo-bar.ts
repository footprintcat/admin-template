import type { Router } from 'vue-router'

/**
 * foo bar 路由守卫
 *
 * 此守卫仅用作代码示例, 正式项目中可以安全的删除
 */
export function createFooBarGuard(router: Router) {
  router.beforeEach((to, from) => {
    console.log('router.beforeEach triggered!')
    // console.log('to', to, 'from', from)
  })
}
