import { defineStore } from 'pinia'
import type { PersistenceOptions } from 'pinia-plugin-persistedstate'

interface ListItem {
    name: string
    path: string
    title: string
}

// 总是添加 dashboard 标签
const dashboardTab = {
    id: 'dashboard',
    title: '系统首页',
    path: '/dashboard',
}

export const useTabsStore = defineStore('tabs', {
    state: () => {
        return {
            list: <ListItem[]>[],
        }
    },
    getters: {
        show: state => {
            return state.list.length > 0
        },
        nameList: state => {
            return state.list.map(item => item.name)
        },
    },
    actions: {
        delTabsItem(index: number) {
            this.list.splice(index, 1)

            // 至少保留1个tab
            if (this.list.length === 0) {
                this.list.push(dashboardTab)
            }
        },
        setTabsItem(data: ListItem) {
            // TODO 临时解决方案，待优化
            for (const index in this.list) {
                const item = this.list[index]
                if (item.name === data.name) {
                    this.delTabsItem(index)
                    break
                }
            }
            this.list.push(data)
        },
        clearTabs() {
            this.list = [
                // 关闭所有tab后，保留系统首页
                dashboardTab,
            ]
        },
        closeTabsOther(data: ListItem[]) {
            this.list = data
        },
    },
    // 持久化配置
    persist: <PersistenceOptions>{
        // 存储到 localStorage 中的键名
        key: 'tab-store',
        // 需要持久化的数据，如果是全部则不需要配置 paths
        paths: ['list'],
    },
})
