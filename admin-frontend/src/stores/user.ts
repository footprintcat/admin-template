import { computed, ref } from 'vue'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', () => {
  // 状态
  const userId = ref<string | undefined>()
  const username = ref<string | undefined>()

  // 计算属性
  const isLogin = computed(() => {
    return !!userId.value && !!username.value
  })

  const isSu = computed(() => {
    // TODO
    return false
  })

  const set = (id: string, name: string) => {
    userId.value = id
    username.value = name
  }

  const clear = () => {
    userId.value = undefined
    username.value = undefined
  }

  return {
    // 状态
    userId,
    username,

    // 计算属性
    isLogin,
    isSu,

    // 方法
    set,
    clear,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: true,
})
