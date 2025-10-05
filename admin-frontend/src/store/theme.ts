import { ref } from 'vue'
import { defineStore } from 'pinia'
import type { PersistenceOptions } from 'pinia-plugin-persistedstate'

export const useThemeStore = defineStore('theme', () => {

  // 演示模式
  const presentationMode = ref<boolean>(false)

  return {
    presentationMode,
  }
}, {
  // 持久化配置
  persist: <PersistenceOptions>{
    // 存储到 localStorage 中的键名
    key: 'theme-store',
  },
})
