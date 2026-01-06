export interface SidebarItem {
  icon?: string
  index: string
  title: string
  shortTitle?: string // <=4 个字的标题
  permission: string | null // permission 为 null 的菜单项不需要校验权限
  subs?: Array<SidebarItem>
}
