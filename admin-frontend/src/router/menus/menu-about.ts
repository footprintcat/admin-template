import type { SidebarItem } from './types/sidebar-item'

export const menuAbout: SidebarItem = {
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
}
