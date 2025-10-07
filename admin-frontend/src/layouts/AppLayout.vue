<template>
  <v-header />
  <v-sidebar />
  <div class="content-box" :class="{ 'content-collapse': sidebar.collapse }">
    <v-tabs></v-tabs>
    <div class="content">
      <!-- <component v-if="route.meta.showErrorPage" :is="errorPage403" />
			<router-view v-else v-slot="{ Component }"> -->
      <router-view v-slot="{ Component }">
        <transition name="move" mode="out-in">
          <keep-alive :include="tabs.nameList">
            <div class="container" style="height: 100%;">
              <component :is="Component" />
            </div>
          </keep-alive>
        </transition>
      </router-view>
    </div>
  </div>
</template>

<script setup lang="ts">
// import { defineAsyncComponent } from 'vue'
import { useRoute } from 'vue-router'
import { useSidebarStore } from '@/stores/sidebar'
import { useTabsStore } from '@/stores/tabs'
import vHeader from './components/header.vue'
import vSidebar from './components/sidebar/sidebar.vue'
import vTabs from './components/tabs.vue'

const sidebar = useSidebarStore()
const tabs = useTabsStore()
const route = useRoute()

// const errorPage403 = defineAsyncComponent(() => import('./error-page/403.vue'))
</script>
