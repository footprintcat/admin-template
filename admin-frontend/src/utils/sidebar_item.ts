import { usePermissionStore } from '../stores/permission'

export interface SidebarItem {
  icon?: string
  index: string
  title: string
  shortTitle?: string // <=4 个字的标题
  permission: string | null // permission 为 null 的菜单项不需要校验权限
  subs?: Array<SidebarItem>
}

const dashboardItemList: Array<SidebarItem> = [
  {
    icon: 'HomeFilled',//'/assets/image/svg/alert_warning.svg',
    index: '/dashboard',
    title: '系统首页', // 站点基础信息
    permission: null, // 'dashboard',
  },
  {
    icon: 'Guide',
    index: '/navi',
    title: '系统导航',
    permission: 'navi',
  },
]

const dashboardSidebarItem: SidebarItem = {
  icon: 'Odometer',
  index: '/',
  title: '仪表盘',
  permission: 'data',
  subs: dashboardItemList,
}

export const sidebarItemsClassic: Array<SidebarItem> = [
  /**
   * 图标
   * element-plus 自带图标: https://element-plus.org/zh-CN/component/icon.html
   *
   * 扩展图标
   * [iconify]
   *   - Font-GIS: https://icon-sets.iconify.design/gis/
   *   - Flowbite Icons: https://icon-sets.iconify.design/flowbite/
   *   - Material Symbols: https://icon-sets.iconify.design/material-symbols/
   * [iconfont]
   *   - iconfont: https://www.iconfont.cn/
   */
  {
    icon: 'Setting',
    index: '/system',
    title: '系统管理',
    permission: 'system',
    subs: [
      {
        icon: 'User',
        index: '/system/user/manage',
        title: '用户管理',
        permission: ':system:user:manage',
      },
      {
        icon: 'User',
        index: '/system/system-user',
        title: '用户管理',
        permission: 'system-user',
      },
      {
        icon: 'Avatar',
        index: '/system/system-role',
        title: '角色管理',
        permission: 'system-role',
      },
      {
        icon: 'Lock',
        index: '/system/system-privilege',
        title: '权限管理',
        permission: 'system-privilege',
      },
      {
        icon: 'Menu',
        index: '/system/system-menu',
        title: '菜单管理',
        permission: 'system-menu',
      },
      {
        icon: 'Setting',
        index: '/system/system-setting',
        title: '系统设置',
        permission: 'system-setting',
      },
      {
        icon: 'FlowbiteFixTablesOutline',
        index: '/system/system-log',
        title: '系统日志',
        permission: 'system-log',
      },
    ],
  },
  {
    icon: 'Sunset',
    index: '/about',
    title: '关于站点',
    permission: 'site-info',
    subs: [
      {
        icon: 'Sunset',
        index: '/site-info',
        title: '站点信息',
        permission: 'site-info',
      },
    ],
  },
]

export const sidebarItemsDoubleColumn: Array<SidebarItem> = [
  ...sidebarItemsClassic,
]

export const doubleColumnHomeItem: SidebarItem = {
  icon: 'House',
  index: '',
  title: '全部菜单',
  permission: 'data',
  subs: sidebarItemsDoubleColumn, //.filter(i => i.subs).map(i => i.subs).flat(),
}

/**
 * 基础菜单项: 用于 navi 页面展示
 */
export const sidebarItems: Array<SidebarItem> = sidebarItemsClassic

/**
 * 左侧侧边栏菜单项
 */
// 经典
export const sidebarMenuItemListClassic: Array<SidebarItem> = [...dashboardItemList, ...sidebarItems]
// 双栏
export const sidebarMenuItemListDoubleColumn: Array<SidebarItem> = sidebarItems

/**
 * dashboard 页面菜单项
 */
export const dashboardMenuItemList: Array<SidebarItem> = [dashboardSidebarItem, ...sidebarItemsClassic]

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
