import { computed, readonly, ref } from 'vue'
import { defineStore } from 'pinia'
import { ElMessageBox } from 'element-plus'
import { systemUserAuthGetInfo } from '@/api/system/user-auth'
import type { UserDto } from '@/types/backend/dto/UserDto'

export const useUserStore = defineStore('user', () => {

  // 状态
  const userDto = ref<UserDto | null>(null)
  const userDtoReadonly = readonly(userDto) // 只读属性
  const isFetchedUserDto = ref<boolean>(false)

  // 计算属性
  const isLogin = computed(() => {
    return userDto.value !== null
  })

  const isSu = computed(() => {
    return userDto.value !== null && userDto.value.type === 'super_admin'
  })

  function clear() {
    userDto.value = null
    isFetchedUserDto.value = false
  }

  // 网页加载后 check-login.ts 中会调用一次 fetchUserInfo
  async function fetchUserInfo(params: {
    /** 当用户信息存在时跳过拉取 */
    skipWhenExists: boolean
  }) {
    if (params.skipWhenExists && isFetchedUserDto.value) {
      return
    }
    return systemUserAuthGetInfo()
      .then((result) => {
        const userInfo: UserDto | null = result.data
        userDto.value = userInfo
        isFetchedUserDto.value = true
      })
      .catch((err) => {
        console.error('systemUserAuthGetInfo', err)
        ElMessageBox
          .alert('服务器连接失败', '网络异常', {
            showClose: false,
            closeOnClickModal: false,
            confirmButtonText: '点击重试',
            type: 'error',
          })
          .then(() => {
            fetchUserInfo(params)
          })

      })
  }

  // 登录后接口会带回 UserDto, 此时不需要再调用 fetchUserInfo 多查询一次
  async function updateUserInfo(userInfo: UserDto) {
    userDto.value = userInfo
    isFetchedUserDto.value = true
  }

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
