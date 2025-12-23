import { computed, readonly, ref } from 'vue'
import { defineStore } from 'pinia'
import { systemUserGetInfo } from '@/api/system/user-auth'
import type { UserDto } from '@/types/backend/dto/UserDto'

export const useUserStore = defineStore('user', () => {

  // 状态
  const userDto = ref<UserDto | null>(null)
  const userDtoReadonly = readonly(userDto) // 只读属性

  // 计算属性
  const isLogin = computed(() => {
    return userDto.value !== null
  })

  const isSu = computed(() => {
    return userDto.value !== null && userDto.value.type === 'super_admin'
  })

  function clear() {
    userDto.value = null
  }

  async function fetchUserInfo() {
    return systemUserGetInfo().then((data) => {
      // TODO
      console.log('systemUserGetInfo', data)
    })
  }

  // 登录后接口会带回 UserDto, 此时不需要再调用 fetchUserInfo 多查询一次
  async function updateUserInfo(userInfo: UserDto) {
    userDto.value = userInfo
  }

  // 网页加载后立即获取一次当前登录用户信息
  fetchUserInfo()

  return {
    // 状态
    user: userDtoReadonly,

    // 计算属性
    isLogin,
    isSu,

    // 方法
    clear,

    fetchUserInfo,
    updateUserInfo,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: false,
})
