import { defineStore } from 'pinia'
import useUserStore from './user'

interface UserInfo {
  id: string
  roleId: string
  username: string
}

export const usePermissionStore = defineStore('permission', {
  state: () => {
    const keys = localStorage.getItem('ms_keys')
    return {
      key: keys ? JSON.parse(keys) : <string[]>[],
    }
  },
  actions: {
    /**
     * 页面加载后，异步获取最新的权限信息
     * @param roleId 用户RoleId，Number类型
     * @param refreshPageWhenPermissionMismatch 在本地保存的 PermissionList 和最新拉取的 PermissionList 不一致时，是否刷新页面
     */
    async asyncUpdatePermissionList(roleId: string | null = null, refreshPageWhenPermissionMismatch: boolean = false) {
      const userStore = useUserStore()

      // 获取当前登录用户信息 (如果为空则会跳转登录页面)
      await send_request_without_loading('v1/user/getInfo', 'POST', {}, (userInfo: UserInfo) => {
        console.log('userInfo', userInfo)
        userStore.set(userInfo.id, userInfo.roleId, userInfo.username)
      })

      // 获取新的页面权限集
      await send_request_without_loading('v1/privilege/getCurrentUserPrivilegeList', 'GET', {}, (privilegeList: Array<string>) => {
        // console.log("roleId", userStore.roleId)
        const newPermissionList = privilegeList
        if (!newPermissionList) return

        this.key = newPermissionList

        const newPermissionJsonString = JSON.stringify(newPermissionList)
        const oldPermissionJsonString = localStorage.getItem('ms_keys')
        if (newPermissionJsonString != oldPermissionJsonString) { // 权限发生变化了
          // 更新本地缓存
          localStorage.setItem('ms_keys', newPermissionJsonString)

          // 判断是否需要刷新页面
          if (refreshPageWhenPermissionMismatch) {
            console.log('权限发生变化，刷新页面...')
            location.reload()
          }
        }
      })
    },
  },
})
