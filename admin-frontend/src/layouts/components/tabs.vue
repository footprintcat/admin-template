<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="tabs" v-if="tabs.show">
    <ul>
      <li class="tabs-li" v-for="(item, index) in tabs.list" :key="index"
        :style="{ minWidth: `${Math.max(42, (12 * item.title.length) + 17)}px` }"
        :class="{ 'active': isActive(item.path), 'tab-always-open': isAlwaysOpen(item.path) }"
        :ref="(el) => setItemRef(el, item.path)" @click="handleTabClick(item.path, $event)">
        <span class="tabs-li-title">{{ item.title }}</span>
        <!-- 外侧有 @click 事件了，这里不再需要 router-link -->
        <!-- <router-link :to="item.path" class="tabs-li-title">{{ item.title }}</router-link> -->
        <el-icon @click.stop="closeTabs(index)" class="tab-close-btn">
          <Close />
        </el-icon>
      </li>
    </ul>
    <div class="tabs-close-box">
      <el-dropdown @command="handleTabs">
        <el-button size="small" plain>
          标签选项
          <el-icon class="el-icon--right">
            <ArrowDown />
          </el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu size="small">
            <el-dropdown-item command="other">关闭其他</el-dropdown-item>
            <el-dropdown-item command="all">关闭所有</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup lang="ts">
import { nextTick, onBeforeUpdate, onMounted, ref } from 'vue'
import { onBeforeRouteUpdate, type RouteLocationNormalizedLoaded, useRoute, useRouter } from 'vue-router'
import { ArrowDown, Close } from '@element-plus/icons-vue'
import { useTabsStore } from '@/stores/tabs'

const route = useRoute()
const router = useRouter()
const isActive = (path: string) => {
  return path === route.fullPath
}
const isAlwaysOpen = (path: string) => {
  return false
  // return path === '/dashboard'
}

const tabs = useTabsStore()

const itemRefs = ref<Record<string, unknown>>({})
const setItemRef = (el: unknown, path: string) => {
  itemRefs.value[path] = el!
}
onBeforeUpdate(() => {
  itemRefs.value = {}
})

// 关闭单个标签
const closeTabs = (index: number) => {
  const delItem = tabs.list[index]! // index 是 v-for 数组遍历渲染的 index, 所以这里 delItem 一定存在, 不会是 undefined
  tabs.delTabsItem(index)
  const item = tabs.list[index] ? tabs.list[index] : tabs.list[index - 1]
  if (item) {
    if (delItem.path === route.fullPath) {
      router.push(item.path)
    }
  } else {
    router.push('/')
  }
}

// 设置标签
const setTabs = (route: RouteLocationNormalizedLoaded) => {
  const isExist = tabs.list.some((item) => {
    return item.path === route.fullPath
  })
  if (!isExist) {
    // if (tabs.list.length >= 8) tabs.delTabsItem(0)
    tabs.setTabsItem({
      name: route.name as string,
      title: route.meta.title as string,
      path: route.fullPath,
      isErrorPage: !!route.meta.isErrorPage,
    })
  }
}
setTabs(route)
onBeforeRouteUpdate((to) => {
  // console.log('onBeforeRouteUpdate', to.path)
  setTabs(to)

  const path = to.path
  scrollToTarget(path)
})

onMounted(() => {
  scrollToTarget(route.path)
})

const scrollToTarget = (path: string) => {
  // tab标签如果显示了横向滚动条 滚动到指定tab元素
  nextTick(() => {
    const el = itemRefs.value[path]
    // console.log('itemRefs', itemRefs.value, path)
    // console.log('el', el)

    // 这样有可能会带着整个页面一起 scroll
    // el.scrollIntoView({ behavior: 'smooth' })

    const child = el as HTMLElement | undefined
    if (!child) {
      // console.log('菜单已关闭')
      return
    }
    // const childPosition = child.offsetTop // 获取子元素到父容器顶部的距离
    const childPositionX = child.offsetLeft // 获取子元素到父容器左边的距离
    child.parentElement!.scrollTo({
      // top: childPosition,
      left: childPositionX - 10, // 左侧留一点边距
      behavior: 'smooth', // 平滑滚动
    })
  })
}

// 关闭全部标签
const closeAll = () => {
  tabs.clearTabs()
  router.push('/')
}
// 关闭其他标签
const closeOther = () => {
  const curItem = tabs.list.filter(item => {
    return item.path === route.fullPath
  })
  tabs.closeTabsOther(curItem)
}
const handleTabs = (command: string) => {
  if (command === 'other') {
    closeOther()
  } else {
    closeAll()
  }
}

// 关闭当前页面的标签页
// tabs.closeCurrentTab({
//     $router: router,
//     $route: route
// })

function handleTabClick(path: string, event: PointerEvent) {
  // 检查是否点击了关闭按钮，如果没有点击关闭按钮
  if ((event.target as Element).closest('.tab-close-btn')) {
    console.log('点了关闭按钮')
    return // 如果是点击关闭按钮，就不处理
  }
  // 这里模拟点击 router-link
  // console.log('点了四周区域')
  router.push(path)
}
</script>

<style scoped>
.tabs {
  position: relative;
  height: var(--tab-title-height);
  overflow: hidden;
  background: #fff;
  padding-right: 120px;
  /* box-shadow: 0 5px 10px #ddd; */
  box-shadow: 5px 5px 10px #ddd;
}

.tabs ul {
  box-sizing: border-box;
  width: 100%;
  height: 100%;

  padding-left: 5px;

  /* 标签过多时显示横向滚动条 开始 */
  display: flex;
  /* 左对齐 */
  overflow-x: auto;
  overflow-y: hidden;
  scrollbar-width: thin;
  /* 标签过多时显示横向滚动条 结束 */

  user-select: none;
}

/* 隐藏滚动条 */
.tabs ul::-webkit-scrollbar {
  /* display: none; */
  background-color: transparent;
}

.tabs-li {
  display: flex;
  align-items: center;
  float: left;
  /* margin: 3px 5px 2px 3px; */
  margin: auto 2px;
  border-radius: 3px;
  font-size: 12px;
  overflow: hidden;
  cursor: pointer;
  /* height: 23px; */
  height: 76%;
  border: 1px solid #e9eaec;
  background: #fff;
  /* padding: 0 5px 0 12px; */
  padding: 0 10px 0 15px;
  color: #666;
  -webkit-transition: all 0.3s ease-in;
  -moz-transition: all 0.3s ease-in;
  transition: all 0.3s ease-in;

  /* 标签最小宽度 */
  /* 建议值: 2个字 42px; 4个字 66px */
  /* 2024.12.05 tab标签宽度根据标题名称长度确定，此处为兜底 */
  min-width: 42px;
}

.tabs-li:not(.active):hover {
  background: #f8f8f8;
}

.tabs-li.active {
  color: #fff;
  border: 1px solid #409eff;
  background-color: #409eff;
}

.tabs-li .tabs-li-title {
  float: left;
  max-width: 120px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  margin-right: 5px;
  color: #666;
}

.tabs-li.active .tabs-li-title {
  color: #fff;
}

.tabs-li.tab-always-open {
  place-content: center;
  width: initial;
  padding: 0 5px;
}

.tabs-li.tab-always-open .tabs-li-title {
  margin: 0px;
}

.tabs-li.tab-always-open .tab-close-btn {
  display: none;
}

.tabs-close-box {
  position: absolute;
  right: 0;
  top: 0;
  box-sizing: border-box;
  place-content: center;
  text-align: center;
  width: 110px;
  height: 100%;
  background: #fff;
  box-shadow: -3px 0 15px 3px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

:deep(.tabs-close-box .el-dropdown) {
  height: 78%;
}

:deep(.tabs-close-box .el-dropdown .el-button) {
  height: 100%;
}
</style>
