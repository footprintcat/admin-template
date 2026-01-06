import type { SidebarItem } from './types/sidebar-item'

export const FrameworkMenuItem_dashboard: SidebarItem = {
  icon: 'HomeFilled', // '/assets/image/svg/alert_warning.svg',
  index: '/dashboard',
  title: '系统首页', // 站点基础信息
  permission: null, // 'dashboard',
}

export const FrameworkMenuItem_navigation: SidebarItem = {
  icon: 'Guide',
  index: '/navigation',
  title: '系统导航',
  permission: 'navigation',
}

export const FrameworkMenuItemList: Array<SidebarItem> = [
  FrameworkMenuItem_dashboard,
  FrameworkMenuItem_navigation,
]

export const menuFramework: SidebarItem = {
  icon: 'Odometer',
  index: '/',
  title: '仪表盘',
  permission: null, // 'data',
  subs: [
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
    ...FrameworkMenuItemList,
  ],
}
