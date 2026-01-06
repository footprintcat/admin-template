<template>
  <!-- {{ activeTopItemIndex }} -->
  <div class="sidebar-grid" @mouseleave="sidebarMouseHover(false)">
    <!-- 顶级菜单 -->
    <div class="top-level-sidebar" :class="[
      // 根据不同的 style 添加不同的 class 以实现样式调整
      doubleColumnConfig.topLevelBarStyle,
    ]">
      <!-- 菜单项 -->
      <div class="top-level-sidebar-container" @mouseenter="sidebarMouseHover(true)">
        <div v-for="(item, index) in sidebarMenuItemListWithHomeItem" :key="`sidebar_${index}`"
          class="top-level-sidebar-item"
          :class="item.index === (activeTopItem ? activeTopItem.index : '') ? 'active' : 'inactive'"
          @mouseenter="topItemMouseEnter(item, true)" @mouseleave="topItemMouseEnter(item, false)"
          @click="onTopItemClicked(item)" v-permission="item.permission">
          <el-icon :size="20" class="top-level-sidebar-item-icon">
            <component :is="item.icon"></component>
          </el-icon>
          <p style="white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 90%;"
            class="top-level-sidebar-item-text">
            {{ item.shortTitle || item.title }}
          </p>
        </div>
      </div>

      <!-- 折叠按钮上方渐变色块 -->
      <div class="top-level-sidebar-gradient-background"></div>

      <!-- 折叠按钮 -->
      <div class="top-level-sidebar-collapse-btn">
        <div
          style="background-color: var(--el-color-primary); width: 30px; height: 30px; display: grid; place-items: center; border-radius: 5px; cursor: pointer;"
          @click="() => sidebar.collapse = !sidebar.collapse">
          <el-icon style="transition: all 0.3s;"
            :style="{ transform: sidebar.collapse ? 'rotate(-90deg)' : 'rotate(90deg)' }">
            <Download />
          </el-icon>
        </div>
      </div>
    </div>
    <!-- 次级菜单 -->
    <div class="bottom-level-sidebar" style="transition: all 0.25s;"
      :style="{ 'opacity': currentMouseEnteredTopItem ? 0.8 : undefined }">
      <el-menu class="sidebar-el-menu" :default-active="route.path" :collapse="sidebar.collapse"
        background-color="#324157" text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
        <template v-if="computedFinalActiveTopItem">
          <!-- [全部菜单] 菜单项 -->
          <sidebar-menu-item v-if="isAtHome" :sidebar-item="sidebarMenuItemListClassic" :force-show-icon="true" />
          <!-- 其他菜单项 -->
          <sidebar-menu-item v-else :sidebar-item="computedFinalActiveTopItem.subs || []" :force-show-icon="true" />
        </template>
        <sidebar-menu-item v-else :sidebar-item="sidebarMenuItemListClassic" :force-show-icon="true" />
      </el-menu>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, nextTick, onMounted, ref, toRaw, watch } from 'vue'
import { useRoute } from 'vue-router'
import { storeToRefs } from 'pinia'
import { Download } from '@element-plus/icons-vue'
import { doubleColumnHomeItem, sidebarMenuItemListClassic, sidebarMenuItemListDoubleColumn } from '@/router/menus'
import { getTopItemByChildIndex } from '@/router/menus/sidebar_item'
import type { SidebarItem } from '@/router/menus/types/sidebar-item'
import { useSidebarStore } from '@/stores/sidebar'
import SidebarMenuItem from './SidebarMenuItem.vue'

const route = useRoute()
const sidebar = useSidebarStore()

const { doubleColumnConfig } = storeToRefs(sidebar)

const sidebarMenuItemListWithHomeItem: Array<SidebarItem> = [
  doubleColumnHomeItem,
  ...sidebarMenuItemListDoubleColumn,
]

// 激活菜单对应的顶级菜单项
const activeTopItem = ref<SidebarItem | undefined>(undefined)
// 鼠标悬浮的菜单项
const currentMouseEnteredTopItem = ref<SidebarItem | undefined>(undefined)
const computedFinalActiveTopItem = computed(() => {
  const _currentMouseEnteredTopItem = currentMouseEnteredTopItem.value
  const _activeTopItem = activeTopItem.value // 需要先访问一般这个变量，如果直接 a.value || b.value 这样写，a 初始为 false时computed不生效
  return _currentMouseEnteredTopItem || _activeTopItem
})

const isAtHome = computed(() => toRaw(computedFinalActiveTopItem.value) === doubleColumnHomeItem)

watch(() => route.path, () => {
  nextTick(() => {
    sidebarMouseHover(false)
  })

  const topItem: SidebarItem | undefined = getTopItemByChildIndex(sidebarMenuItemListDoubleColumn, route.path, 'GetTop')
  // console.log('topItem', topItem)
  if (!topItem) {
    // 未匹配到顶级菜单, fallback到全部菜单
    // console.log('未匹配到顶级菜单, fallback!')
    if (sidebar.getTopIndex() === undefined) {
      sidebar.setTopIndex('')
      sidebar.setUserSelectTopIndex('')
      activeTopItem.value = doubleColumnHomeItem
    }
  } else {
    // 匹配到了顶级菜单
    if (sidebar.getUserSelectTopIndex() === '') {
      // sidebar.setUserSelectTopIndex('')
      sidebar.setTopIndex('')
      activeTopItem.value = doubleColumnHomeItem
      return // 顶级菜单为 [全部菜单], 跳过一级菜单更新
    }
    sidebar.setTopIndex(topItem.index)
    sidebar.setUserSelectTopIndex(topItem.index)
    activeTopItem.value = topItem
  }
}, { immediate: true })

onMounted(() => {
  // nextTick(() => {
  //     const element: any = document.querySelector('.sidebar-grid')
  //     if (!element) {
  //         console.error('[DoubleColumnSidebar] 找不到 sidebar-grid')
  //         return
  //     }
  //     const computedStyle = getComputedStyle(element);
  //     const totalWidth = computedStyle.getPropertyValue('--total-width');
  //     const collapseWidth = computedStyle.getPropertyValue('--collapse-width');
  //     // console.log('totalWidth', totalWidth, 'collapseWidth', collapseWidth)

  //     document.documentElement.style.setProperty('--sidebar-width', totalWidth);
  //     document.documentElement.style.setProperty('--sidebar-collapse-width', collapseWidth);
  //     // document.documentElement.style.setProperty('--sidebar-collapse-width', '0');
  // })
})

function onTopItemClicked(item: SidebarItem) {
  sidebar.setUserSelectTopIndex(item.index)
  // if (item.index === '') {
  //     // sidebar.setTopIndex(item.index)
  // }
  activeTopItem.value = item
  currentMouseEnteredTopItem.value = undefined
}

function sidebarMouseHover(hover: boolean) {
  // console.log('sidebarMouseHover', hover)
  if (hover) {
    if (sidebar.collapse === true) {
      sidebar._collapse_real = sidebar.collapse
      sidebar.collapse = false
    }
  } else {
    if (sidebar._collapse_real !== undefined) {
      sidebar.collapse = sidebar._collapse_real
      sidebar._collapse_real = undefined
    }
  }
}

function topItemMouseEnter(sidebarItem: SidebarItem, hover: boolean) {
  if (sidebarItem === toRaw(activeTopItem.value)) {
    return // 当前悬浮菜单项就是当前菜单，跳过处理
  }
  // console.log('topItemMouseEnter', hover)
  currentMouseEnteredTopItem.value = hover ? sidebarItem : undefined
}
</script>

<style lang="css" scoped>
.sidebar-grid {
  /* Sidebar 宽度修改后，global-vars.css 中宽度也需要更新 */
  /* 顶级菜单宽度 */
  --collapse-width: 72px;
  /* 下级菜单宽度 */
  --menu-width: 200px;
  --total-width: calc(var(--collapse-width) + var(--menu-width));

  --top-level-sidebar-button-size: 54px;
  --space: 12px;
  --margin: calc((var(--collapse-width) - var(--top-level-sidebar-button-size)) / 2);
  --radius: 6px;
  --item-font-size: 10px;

  --menu-bg-color: #324157;

  user-select: none;

  width: var(--total-width);
  display: grid;
  grid-template-columns: var(--collapse-width) var(--menu-width);
  /* background-color: rebeccapurple; */

  /* el-tree 菜单项缩进样式↓ */
  /* padding-left: calc(var(--el-menu-base-level-padding) + var(--el-menu-level)* var(--el-menu-level-padding)); */
  /* --el-menu-base-level-padding, --el-menu-level-padding 默认为 20px */
  /* --el-menu-base-level-padding: 18px; */
  --el-menu-level-padding: 17px;
}

.top-level-sidebar {
  background-color: #0b111c;
  color: white;
  height: var(--tab-and-content-height);
  width: var(--collapse-width);

  --collapse-btn-width: 50px;
  --collapse-gradient-height: 10px;
  --collapse-toggle-btn-width: 52px;
  --collapse-bottom-empty-height: 10px;
}

.top-level-sidebar-container {
  height: calc(100% - var(--collapse-toggle-btn-width) - var(--collapse-bottom-empty-height));
  overflow-y: scroll;
  overflow-x: hidden;

  /* 保留颜色渐变高度 */
  padding-bottom: var(--collapse-gradient-height);
  /* padding 算在盒模型内部 */
  box-sizing: border-box;
}

.top-level-sidebar-gradient-background {
  height: var(--collapse-gradient-height);
  margin-top: calc(-1 * var(--collapse-gradient-height));
  background: linear-gradient(to bottom, #00000000 0%, #0b111c 100%);
  position: relative;
}

.top-level-sidebar-collapse-btn {
  height: var(--collapse-toggle-btn-width);
  display: grid;
  place-items: center;
}

.top-level-sidebar-item {
  width: var(--top-level-sidebar-button-size);
  height: var(--top-level-sidebar-button-size);
  margin-left: var(--margin);
  margin-top: var(--space);
  border-radius: var(--radius);
  font-size: var(--item-font-size);
  box-sizing: border-box;
  /* border: 1px solid white; */

  display: grid;
  padding: 3px;
  place-items: center;

  cursor: pointer;
  transition: background-color 0.13s;
}

/* 侧边栏顶级菜单样式 diy 开始 */
/* 圆角按钮 */
.round-button .top-level-sidebar-item {
  grid-template-rows: auto 15px 2.5px;
  /* 2.5px 为文字底部占位 */
}

/* 仅标题 */
.text .top-level-sidebar-item {
  grid-template-rows: auto;
  height: 25px;
  margin-top: calc(var(--space) - 5px);
}

/* 仅图标 */
.horizontal .top-level-sidebar-item-icon {
  grid-template-rows: auto;
  font-size: 24px !important;
}

/* 仅标题的图标 和 仅图标的标题 都需要隐藏 */
.text .top-level-sidebar-item-icon,
.horizontal .top-level-sidebar-item-text {
  display: none;
}

/* 侧边栏顶级菜单样式 diy 结束 */

.top-level-sidebar-item.active {
  background-color: var(--el-color-primary);
  box-shadow: 2px 2px 8px rgb(255 255 255 / 22%);
}

.top-level-sidebar-item.inactive:hover {
  background-color: #ffffff35;
}

.bottom-level-sidebar {
  height: var(--tab-and-content-height);
  overflow-y: auto;
  overflow-x: hidden;
}

.bottom-level-sidebar .sidebar-el-menu {
  min-height: 100%;
  width: var(--menu-width);
}

/* 滚动配置开始 */
/* https://developer.mozilla.org/zh-CN/docs/Web/CSS/::-webkit-scrollbar */
/* 滚动条 */
.top-level-sidebar-container::-webkit-scrollbar,
.bottom-level-sidebar::-webkit-scrollbar {
  display: none;
  width: 4.2px;
  /* background-color: red; */
}

.bottom-level-sidebar::-webkit-scrollbar {
  background-color: var(--menu-bg-color);
}

/* 滚动条上下箭头 */
.top-level-sidebar-container::-webkit-scrollbar-button,
.bottom-level-sidebar::-webkit-scrollbar-button {
  display: none;
}

/* 滚动条滑块 */
.top-level-sidebar-container::-webkit-scrollbar-thumb,
.bottom-level-sidebar::-webkit-scrollbar-thumb {
  height: 50vh;
  border-radius: 5px;
}

.top-level-sidebar-container::-webkit-scrollbar-thumb {
  background-color: rgba(173, 173, 173, 0.639);
}

.bottom-level-sidebar::-webkit-scrollbar-thumb {
  background-color: #ffffff86;
}

/* 滚动条底部滑动部分 */
.bottom-level-sidebar:hover::-webkit-scrollbar,
.top-level-sidebar-container:hover::-webkit-scrollbar {
  display: inherit;
}

/* 滚动配置结束 */
</style>
