<template>
  <component :is="currentLoginComponent" />
</template>

<script setup lang="ts">
import { computed, defineAsyncComponent } from 'vue'
// import { settings } from '@/utils/settings'

const currentLoginComponent = computed(() => {
  const loginComponents = {
    // v1: () => import('./login-v1.vue'),
    v2: () => import('./login-v2.vue'),
  } as const
  // const componentPath = loginComponents[settings.loginUI]
  const componentPath = loginComponents.v2
  return defineAsyncComponent(componentPath)
})
</script>
