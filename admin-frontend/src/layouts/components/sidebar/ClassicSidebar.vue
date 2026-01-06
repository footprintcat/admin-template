<template>
  <div class="sidebar">
    <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="sidebar.collapse" background-color="#324157"
      text-color="#bfcbd9" active-text-color="#20a0ff" unique-opened router>
      <sidebar-menu-item :sidebar-item="sidebarMenuItemListClassic" :force-show-icon="true" />
    </el-menu>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { sidebarMenuItemListClassic } from '@/router/menus'
import { useSidebarStore } from '@/stores/sidebar'
import SidebarMenuItem from './SidebarMenuItem.vue'

const route = useRoute()
const onRoutes = computed(() => {
  return route.path
})

const sidebar = useSidebarStore()

onMounted(() => {
  // document.documentElement.style.setProperty('--sidebar-width', '200px');
  // document.documentElement.style.setProperty('--sidebar-collapse-width', '0');
})
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: var(--header-height);
  bottom: 0;
  overflow-y: scroll;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: var(--sidebar-width);
}

.sidebar-el-menu {
  min-height: 100%;
}
</style>
