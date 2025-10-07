import { computed } from 'vue'
import { defineStore } from 'pinia'

interface UserState {
  isEmpty: boolean
  userIdStr: string | undefined
  roleId: number | undefined
  username: string | undefined
}

const useUserStore = defineStore('user', {
  state: (): UserState => ({
    isEmpty: true,
    userIdStr: undefined,
    roleId: undefined,
    username: undefined,
  }),
  actions: {
    isLogin() {
      return !this.isEmpty
    },
    isSu() {
      return computed(() => this.roleId === 1)
    },
    set(userIdStr: string, roleId: number, username: string) {
      this.isEmpty = false
      this.userIdStr = userIdStr
      this.roleId = roleId
      this.username = username
    },
    clear() {
      this.isEmpty = true
      this.userIdStr = undefined
      this.roleId = undefined
      this.username = undefined
    },
  },

  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: true,
})

export default useUserStore
