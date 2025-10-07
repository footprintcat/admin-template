import type { Router } from 'vue-router'
import { useTabsStore } from '@/stores/tabs'
import useUserStore from '@/stores/user'
import send_request from './send_request'
// import { clearFrontendLocalStorage } from './local_storage_util'

export function userLogout(router: Router) {
    // 发送退出登录请求
    send_request('v1/user/logout', 'POST')

    // 关闭全部标签 (销毁页面对象)
    const tabs = useTabsStore()
    tabs.clearTabs()

    // 清除登录信息
    const userStore = useUserStore()
    userStore.clear()

    // // 清除本地 localStorage
    // clearFrontendLocalStorage()


    // 跳转到登录页面
    router.push({
        path: '/login',
        query: {
            // 2025.08.26 route.path 改为 route.fullPath (支持携带 hash 路由参数)
            redirectTo: router.currentRoute.value.fullPath, // window.location.href
        },
    })
}
