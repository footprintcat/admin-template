import { computed } from 'vue'
import { defineStore } from 'pinia'

interface UserState {
  isEmpty: boolean
  userId: string | undefined
  roleId: string | undefined
  username: string | undefined
}

const useUserStore = defineStore('user', {
  state: (): UserState => ({
    isEmpty: true,
    userId: undefined,
    roleId: undefined,
    username: undefined,
  }),
  actions: {
    isLogin() {
      return !this.isEmpty
    },
    isSu() {
      // TODO
      return computed(() => this.roleId === '1')
    },
    set(userId: string, roleId: string, username: string) {
      this.isEmpty = false
      this.userId = userId
      this.roleId = roleId
      this.username = username
    },
    clear() {
      this.isEmpty = true
      this.userId = undefined
      this.roleId = undefined
      this.username = undefined
    },
  },

  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: true,
})

export default useUserStore
