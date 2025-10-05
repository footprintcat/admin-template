import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { PersistenceOptions } from 'pinia-plugin-persistedstate'

export type SidebarType = 'Classic' | 'DoubleColumn'
export type TopLevelBarStyle = 'round-button' | 'text' | 'horizontal'

export const useSidebarStore = defineStore('sidebar', () => {

  // 是否折叠菜单
  const collapse = ref<boolean>(false)

  // 临时覆盖折叠菜单配置项时，将原配置保存在这，以备后续还原
  const _collapse_real = ref<boolean | undefined>(undefined)

  const handleCollapse = () => {
    collapse.value = !collapse.value
  }

  // 默认菜单类型
  const DEFAULT_SIDEBAR_TYPE: SidebarType = 'DoubleColumn'

  // 当前菜单类型
  const sidebarType = ref<SidebarType>(DEFAULT_SIDEBAR_TYPE)

  // 顶级菜单的 index (全部菜单为 '')
  const topIndex = ref<string | undefined>(undefined)

  // 当前用户选择的 顶级菜单的 index (全部菜单为 '')
  const userSelectTopIndex = ref<string | undefined>(undefined)

  const canSetTopIndex = (newTopIndex: string, updateWhenInHomeItem: boolean = false) => {
    const can = topIndex.value === undefined || updateWhenInHomeItem || topIndex.value !== ''
    console.log(can ? '可以' : '不可以', '更新当前的顶级菜单', 'old:', topIndex.value === undefined ? undefined : `[${topIndex.value}]`, 'new:', `[${newTopIndex}]`)
    return can
  }

  /**
   * 更新当前的顶级菜单index
   * @param newTopIndex 新的菜单项对应顶级菜单index
   * @param updateWhenInHomeItem 当原来顶级处在全部菜单页面时，是否更新新的顶级菜单项
   */
  const setTopIndex = (newTopIndex: string) => {
    topIndex.value = newTopIndex
  }
  const setUserSelectTopIndex = (newTopIndex: string) => {
    userSelectTopIndex.value = newTopIndex
  }
  const getTopIndex = () => {
    return topIndex.value
  }
  const getUserSelectTopIndex = () => {
    return userSelectTopIndex.value
  }


  /**
   * DoubleColumn 菜单特有配置
   */
  const DEFAULT_TOP_LEVEL_BAR_STYLE: TopLevelBarStyle = 'round-button'
  const doubleColumnConfig = ref<{
    topLevelBarStyle: TopLevelBarStyle
  }>({
    topLevelBarStyle: DEFAULT_TOP_LEVEL_BAR_STYLE,
  })


  return {
    collapse,
    _collapse_real,
    handleCollapse,

    DEFAULT_SIDEBAR_TYPE,
    sidebarType,
    topIndex,
    userSelectTopIndex,
    canSetTopIndex,
    setTopIndex,
    setUserSelectTopIndex,
    getTopIndex,
    getUserSelectTopIndex,

    DEFAULT_TOP_LEVEL_BAR_STYLE,
    doubleColumnConfig,
  }
}, {
  // 持久化配置
  persist: <PersistenceOptions>{
    // 存储到 localStorage 中的键名
    key: 'sidebar-store',
    // 需要持久化的数据，如果是全部则不需要配置 paths
    // paths: [
    //     'collapse',
    //     'sidebarType',
    //     'topIndex',
    // ],
  },
})
