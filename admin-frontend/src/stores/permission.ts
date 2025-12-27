import type { App } from 'vue'
import { defineStore } from 'pinia'
import { getCurrentUserPrivilegeList } from '@/api/system/privilege'

export const usePermissionStore = defineStore('permission', () => {

  // directory, menu 菜单列表
  const menuCodeList = ref<Array<string>>([])
  // 操作按钮权限 (格式: menu_code:action_code)
  const combinedCodeList = ref<Array<string>>([])
  const isInitialized = ref<boolean>(false)

  function clearCurrentPermission() {
    menuCodeList.value = []
    combinedCodeList.value = []
    isInitialized.value = false
  }

  /**
   * 页面加载后，异步获取最新的权限信息
   * @param roleId 用户RoleId，Number类型
   * @param refreshPageWhenPermissionMismatch 在本地保存的 PermissionList 和最新拉取的 PermissionList 不一致时，是否刷新页面
   */
  async function asyncUpdatePermissionList(refreshPageWhenPermissionMismatch: boolean = false) {
    // TODO 考虑更新失败情况
    if (isInitialized.value) return

    // TODO 逻辑待确认
    try {
      const { data } = await getCurrentUserPrivilegeList()
      if (!data) {
        isInitialized.value = true
        return
      }

      const newMenuCodeList = data.menuCodeList || []
      const newCombinedCodeList = data.combinedCodeList || []

      const newMenuJsonString = JSON.stringify(newMenuCodeList)
      const oldMenuJsonString = JSON.stringify(menuCodeList.value)
      const newCombinedJsonString = JSON.stringify(newCombinedCodeList)
      const oldCombinedJsonString = JSON.stringify(combinedCodeList.value)

      const isMenuChanged = newMenuJsonString !== oldMenuJsonString
      const isCombinedChanged = newCombinedJsonString !== oldCombinedJsonString

      menuCodeList.value = newMenuCodeList
      combinedCodeList.value = newCombinedCodeList
      isInitialized.value = true

      if ((isMenuChanged || isCombinedChanged) && refreshPageWhenPermissionMismatch) {
        console.log('权限发生变化，刷新页面...')
        location.reload()
      }
    } catch (error) {
      console.error('获取权限列表失败:', error)
      isInitialized.value = true
    }
  }

  function checkMenuPermission(code: string): boolean {
    return menuCodeList.value.includes(code)
  }

  function checkActionPermission(menuCode: string, actionCode: string): boolean {
    const combinedCode = `${menuCode}:${actionCode}`
    return combinedCodeList.value.includes(combinedCode)
  }

  // 自定义权限指令
  // 在 main.ts 中注册
  function registerDirective(app: App) {
    app.directive('permission', {
      mounted(el, binding) {
        const bindingValue: string = String(binding.value)
        if (!checkMenuPermission(bindingValue)) {
          el['hidden'] = true
        }
      },
    })

  }

  return {
    menuCodeList,
    combinedCodeList,
    isInitialized,
    checkMenuPermission,
    checkActionPermission,
    registerDirective,
    clearCurrentPermission,
    asyncUpdatePermissionList,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: true,
})


/*
export const usePermissionStore = defineStore('permission', {
  state: () => {
    const keys = localStorage.getItem('ms_keys')
    return {
      key: keys ? JSON.parse(keys) : <string[]>[],
    }
  },
  actions: {
    / **
     * 页面加载后，异步获取最新的权限信息
     * @param roleId 用户RoleId，Number类型
     * @param refreshPageWhenPermissionMismatch 在本地保存的 PermissionList 和最新拉取的 PermissionList 不一致时，是否刷新页面
     * /
    async asyncUpdatePermissionList(roleId: string | null = null, refreshPageWhenPermissionMismatch: boolean = false) {
      const userStore = useUserStore()

      // 获取当前登录用户信息 (如果为空则会跳转登录页面)
      await send_request_without_loading('v1/user/getInfo', 'POST', {}, (userInfo: UserInfo) => {
        console.log('userInfo', userInfo)
        // TODO
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
*/
