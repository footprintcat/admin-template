interface MenuBase {
  id: string
  parentId: string | null
  level: number
  menuType: 'directory' | 'menu' | 'action'
  menuCode: string
  actionCode: string | null
  menuName: string
  menuPath: string | null
  sortOrder: number
  canEdit: boolean
  isHide: boolean
}

interface MenuDirectory extends MenuBase {
  menuType: 'directory'
  actionCode: null
  menuPath: null
}

interface MenuMenu extends MenuBase {
  menuType: 'menu'
  actionCode: null
  menuPath: string
}

interface MenuAction extends MenuBase {
  menuType: 'action'
  actionCode: string
  menuPath: null
}

export type MenuDto =
  | MenuDirectory
  | MenuMenu
  | MenuAction
