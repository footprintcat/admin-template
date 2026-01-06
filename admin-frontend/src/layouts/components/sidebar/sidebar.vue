<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <component :is="sidebarComponent" />
</template>

<script setup lang="ts">
import { computed, defineAsyncComponent, watch } from 'vue'
import { useSidebarStore } from '@/stores/sidebar'

const sidebar = useSidebarStore()

// 动态异步加载组件（推荐）
const componentsMap = {
  Classic: defineAsyncComponent(() => import('./ClassicSidebar.vue')),
  DoubleColumn: defineAsyncComponent(() => import('./DoubleColumnSidebar.vue')),
}

const sidebarComponent = computed(() => componentsMap[sidebar.sidebarType])

// 切换侧边栏时更新 html 上的 class (避免在元素中通过 js 更新，因为刷新时会闪一下)
watch(
  () => sidebar.sidebarType,
  () => {
    document.documentElement.classList.remove('sidebar-double-column')
    document.documentElement.classList.remove('sidebar-classic')
    switch (sidebar.sidebarType) {
      case 'Classic':
        document.documentElement.classList.add('sidebar-classic')
        break
      case 'DoubleColumn':
        document.documentElement.classList.add('sidebar-double-column')
        break
    }
  },
  { immediate: true },
)
</script>
