<template>
  <template v-for="item in props.sidebarItem">
    <template v-if="item.subs">
      <el-sub-menu :index="item.index" :key="item.index" v-permission="item.permission">
        <template #title>
          <el-icon>
            <component :is="item.icon"></component>
          </el-icon>
          <span>{{ item.title }}</span>
        </template>

        <sidebar-menu-item :force-show-icon="false" :sidebar-item="item.subs" />
      </el-sub-menu>
    </template>
    <template v-else>
      <el-menu-item :index="item.index" :key="item.index" v-permission="item.permission">
        <el-icon v-if="props.forceShowIcon || item.icon">
          <component :is="item.icon"></component>
        </el-icon>
        <template #title>{{ item.title }}</template>
      </el-menu-item>
    </template>
  </template>
</template>

<script setup lang="ts">
import type { SidebarItem } from '@/router/menus/types/sidebar-item'

defineOptions({
  name: 'sidebar-menu-item',
})

const props = defineProps<{
  forceShowIcon: boolean
  sidebarItem: Array<SidebarItem>
}>()
</script>
