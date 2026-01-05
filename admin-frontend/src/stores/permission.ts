import type { App } from 'vue'
import { readonly } from 'vue'
import { defineStore } from 'pinia'
import type { MenuDto } from '@/types/backend/dto/system/MenuDto'

export const usePermissionStore = defineStore('permission', () => {

  // directory, menu 菜单列表
  const menuCodeList = ref<Array<string>>([])
  // 操作按钮权限 (格式: menu_code:action_code)
  const actionCodeList = ref<Record<string, Array<string>>>({})
  const isInitialized = ref<boolean>(false)
  const updateTime = ref<Date>() // 响应式更新时间

  function clearCurrentPermission() {
    menuCodeList.value = []
    actionCodeList.value = {}
    isInitialized.value = false
  }

  /**
   * 页面加载后，保存获取到的最新的权限信息
   */
  async function savePermissionList(list: MenuDto[], { clearFirst }: { clearFirst: boolean }) {
    if (clearFirst) {
      clearCurrentPermission()
    }

    try {
      const newMenuCodeList: Array<string> = []
      const newActionCodeList: Record<string, Array<string>> = {}

      for (const item of list) {
        if (item.menuType === 'directory' || item.menuType === 'menu') {
          newMenuCodeList.push(item.menuCode)
        } else if (item.menuType === 'action') {
          if (newActionCodeList[item.menuCode] === undefined) {
            newActionCodeList[item.menuCode] = []
          }
          newActionCodeList[item.menuCode].push(item.actionCode)
        }
      }

      console.log('menuCodeList', newMenuCodeList)
      console.log('actionCodeList', newActionCodeList)

      menuCodeList.value = newMenuCodeList
      actionCodeList.value = newActionCodeList
      isInitialized.value = true
      updateTime.value = new Date()
    } catch (error) {
      console.error('获取权限列表失败:', error)
      isInitialized.value = false
      updateTime.value = undefined
    }
  }

  function checkMenuPermission(code: string | null | undefined): boolean {
    if (code === null || typeof code === 'undefined') {
      return true
    }
    const codeStr = String(code)
    return menuCodeList.value.includes(codeStr)
  }

  function checkActionPermission(menuCode: string, actionCode: string): boolean {
    if (actionCodeList.value[menuCode] === undefined) {
      return false
    }
    return actionCodeList.value[menuCode].includes(actionCode)
  }

  // 自定义权限指令
  // 在 main.ts 中注册
  function registerDirective(app: App) {
    app.directive('permission', {
      mounted(el, binding) {
        const bindingValue: string | null | undefined = binding.value
        if (!checkMenuPermission(bindingValue)) {
          el['hidden'] = true
        }
      },
    })
  }

  return {
    menuCodeList: readonly(menuCodeList),
    actionCodeList: readonly(actionCodeList),
    isInitialized: readonly(isInitialized),

    checkMenuPermission,
    checkActionPermission,

    clearCurrentPermission,
    savePermissionList,

    registerDirective,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: false,
  // 2025.12.29 当 persist 设置为 false 时, 返回 readonly 属性会提示 WARNING: Set operation on key "xxx" failed: target is readonly.
})
