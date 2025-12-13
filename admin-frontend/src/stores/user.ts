import { computed } from 'vue'
import { defineStore } from 'pinia'

interface UserState {
  userId: string | undefined
  username: string | undefined
}

const useUserStore = defineStore('user', {
  state: (): UserState => ({
    userId: undefined,
    username: undefined,
  }),
  actions: {
    isLogin() {
      return !!this.userId && !!this.username
    },
    isSu() {
      // TODO
      return computed(() => false)
    },
    set(userId: string, username: string) {
      this.userId = userId
      this.username = username
    },
    clear() {
      this.userId = undefined
      this.username = undefined
    },
  },

  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: true,
})

export default useUserStore
