import { computed, ref } from 'vue'
import { defineStore } from 'pinia'
import type { PersistenceOptions } from 'pinia-plugin-persistedstate'

interface ListItem {
  name: string
  path: string
  title: string
  isErrorPage?: boolean
}

// 总是添加 dashboard 标签
const dashboardTab: ListItem = {
  name: 'Dashboard',
  path: '/dashboard',
  title: '系统首页',
}

export const useTabsStore = defineStore('tabs', () => {
  const list = ref<Array<ListItem>>([])

  const show = computed(() => list.value.length > 0)
  const nameList = computed(() => list.value.map((item: ListItem) => item.name))

  function delTabsItem(index: number) {
    list.value.splice(index, 1)

    // 至少保留1个tab
    if (list.value.length === 0) {
      list.value.push(dashboardTab)
    }
  }
  function setTabsItem(data: ListItem) {
    // TODO 临时解决方案，待优化
    const index = list.value.findIndex(item => item.name === data.name)
    if (index !== -1) {
      delTabsItem(index)
    }

    if (data.isErrorPage) {
      console.log('Error Page 不显示到 Tab 标题栏')
      return
    }
    list.value.push(data)
  }
  function clearTabs() {
    list.value = [
      // 关闭所有tab后，保留系统首页
      dashboardTab,
    ]
  }
  function closeTabsOther(data: Array<ListItem>) {
    list.value = data
  }

  return {
    // state
    list,

    // getters
    show,
    nameList,

    // action
    delTabsItem,
    setTabsItem,
    clearTabs,
    closeTabsOther,
  }
}, {
  // 持久化配置
  persist: <PersistenceOptions>{
    // 存储到 localStorage 中的键名
    key: 'tab-store',
    // 需要持久化的数据，如果是全部则不需要配置 paths
    paths: ['list'],
  },
})
