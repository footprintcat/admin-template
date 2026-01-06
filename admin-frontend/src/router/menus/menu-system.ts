import type { SidebarItem } from './types/sidebar-item'

export const menuSystem: SidebarItem = {
  icon: 'Setting',
  index: '/system',
  title: '系统管理',
  permission: 'system',
  subs: [
    {
      icon: 'User',
      index: '/system/user/manage',
      title: '用户管理',
      permission: 'system:user',
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
}
