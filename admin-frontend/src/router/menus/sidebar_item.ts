import { usePermissionStore } from '../../stores/permission'
import type { SidebarItem } from './types/sidebar-item'

// 将菜单 copy 一份
function copyMenuWithoutSubs(menu: SidebarItem): SidebarItem {
  const result = {
    ...menu,
  }
  delete result.subs
  return result
}

export function filterMenu(searchText: string | undefined, sidebarItemList: SidebarItem[]): SidebarItem[] {
  const permissionStore = usePermissionStore()

  const result: SidebarItem[] = []
  for (const sidebarItem of sidebarItemList) {
    if (sidebarItem.permission && !permissionStore.checkMenuPermission(sidebarItem.permission)) {
      // console.log('跳过没有权限的菜单', sidebarItem)
      continue
    }

    // 菜单标题匹配
    if (!searchText || sidebarItem.title.includes(searchText)) {
      const cloneMenu = copyMenuWithoutSubs(sidebarItem)
      // 不管子菜单标题是否匹配都保留
      if (sidebarItem.subs && sidebarItem.subs.length > 0) {
        const subResult = filterMenu(undefined, sidebarItem.subs) // 走一遍 filterMenu 是为确保菜单经过 permission 过滤
        if (subResult.length > 0) {
          cloneMenu.subs = subResult
        }
      }
      result.push(cloneMenu)
      continue
    }

    // 菜单标题不匹配，但如果子菜单标题匹配，那么保留当前菜单及其下属匹配的子菜单
    if (sidebarItem.subs && sidebarItem.subs.length > 0) {
      const subResult = filterMenu(searchText, sidebarItem.subs)
      if (subResult.length > 0) {
        const cloneMenu = copyMenuWithoutSubs(sidebarItem)
        cloneMenu.subs = subResult
        result.push(cloneMenu)
      }
    }
  }
  return result
}

export function getTopItemByChildIndex(sidebarMenuItemList: Array<SidebarItem> | undefined, childIndexStr: string, type: 'GetCurrent' | 'GetTop'): SidebarItem | undefined {
  if (!sidebarMenuItemList || sidebarMenuItemList.length === 0) {
    return undefined
  }

  for (const sidebarMenuItem of sidebarMenuItemList) {
    if (sidebarMenuItem.index === childIndexStr) {
      return sidebarMenuItem
    }
  }

  for (const sidebarMenuItem of sidebarMenuItemList) {
    const returnVal = getTopItemByChildIndex(sidebarMenuItem.subs, childIndexStr, type)
    if (returnVal) {
      return type === 'GetCurrent'
        ? returnVal // 获取当前菜单项
        : sidebarMenuItem // 获取当前菜单项所属顶级菜单项
    }
  }
  return undefined
}
