import { menuAbout } from './menu-about'
import { FrameworkMenuItemList, menuFramework } from './menu-framework'
import { menuSystem } from './menu-system'
import type { SidebarItem } from './types/sidebar-item'

export const menuTree: Array<SidebarItem> = [

  // 仪表盘
  // 在不同地方做了不同处理, 不在此处添加
  // menuFramework,

  // 系统管理
  menuSystem,

  // 关于站点
  menuAbout,

]

/*
 * 侧边栏
 */

// Classic
export const sidebarItemsClassic: Array<SidebarItem> = [
  ...menuTree,
]

// DoubleColumn
export const sidebarItemsDoubleColumn: Array<SidebarItem> = [
  ...menuTree,
]

export const doubleColumnHomeItem: SidebarItem = {
  icon: 'House',
  index: '',
  title: '全部菜单',
  permission: 'data',
  subs: sidebarItemsDoubleColumn, //.filter(i => i.subs).map(i => i.subs).flat(),
}

/**
 * 左侧侧边栏菜单项
 */
// 经典
export const sidebarMenuItemListClassic: Array<SidebarItem> = [
  ...FrameworkMenuItemList,
  ...menuTree,
]
// 双栏
export const sidebarMenuItemListDoubleColumn: Array<SidebarItem> = [
  ...menuTree,
]


/*
 * 导航页
 */
// 基础菜单项: 用于 navigation 页面展示
export const sidebarItems: Array<SidebarItem> = [
  ...menuTree,
]

/**
 * dashboard 页面菜单项
 */
export const dashboardMenuItemList: Array<SidebarItem> = [
  menuFramework,
  ...sidebarItemsClassic,
]
