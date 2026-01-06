import { computed, readonly, ref } from 'vue'
import { defineStore } from 'pinia'
import { ElMessageBox } from 'element-plus'
import { systemUserAuthGetInfo, systemUserAuthLogout } from '@/api/system/user-auth'
import type { UserDto } from '@/types/backend/dto/system/UserDto'

export const useUserStore = defineStore('user', () => {

  // 状态
  const isInit = ref<boolean>(false)

  const userDto = ref<UserDto | null>(null)
  const isFetchedUserDto = ref<boolean>(false)

  // 只读属性
  const isInitReadonly = readonly(isInit)

  const userDtoReadonly = readonly(userDto)

  // 计算属性
  const isLogin = computed(() => {
    return userDto.value !== null
  })

  const isSu = computed(() => {
    return userDto.value !== null && userDto.value.type === 'super_admin'
  })

  // 网页加载后 check-login.ts 中会调用一次 userStore.init
  async function init(params: {
    /** 如果已经初始化，那么是否需要重新初始化 */
    reInit: boolean
  }) {
    if (params.reInit) {
      clear({ exit: false })
    }

    if (isInit.value) {
      return
    }
    // 网页加载后立即获取一次当前登录用户信息
    const result = await fetchUserInfo({ skipIfExists: true })
    isInit.value = true
    return result
  }

  function clear(params: { exit: boolean }): Promise<unknown> {
    isInit.value = false
    userDto.value = null
    isFetchedUserDto.value = false
    if (params.exit) {
      // 发送退出登录请求
      return systemUserAuthLogout()
    }
    return Promise.resolve()
  }

  async function fetchUserInfo(params: {
    /** 当用户信息存在时跳过拉取 */
    skipIfExists: boolean
  }) {
    if (params.skipIfExists && isFetchedUserDto.value) {
      return
    }
    return systemUserAuthGetInfo()
      .then((result) => {
        const userInfo: UserDto | null = result.data
        userDto.value = userInfo
        isFetchedUserDto.value = true
      })
      .catch(async(err) => {
        console.error('systemUserAuthGetInfo', err)
        await ElMessageBox
          .alert('服务器连接失败，请检查网络连接', '网络异常', {
            showClose: false,
            closeOnClickModal: false,
            confirmButtonText: '点击重试',
            type: 'error',
          })
          .then(async () => {
            await fetchUserInfo(params)
          })
      })
  }

  // 登录后接口会带回 UserDto, 此时不需要再调用 fetchUserInfo 多查询一次
  async function updateUserInfo(userInfo: UserDto) {
    isInit.value = true
    userDto.value = userInfo
    isFetchedUserDto.value = true
  }

  return {
    // 只读属性
    isInit: isInitReadonly,
    user: userDtoReadonly,

    // 计算属性
    isLogin,
    isSu,

    // 方法
    init,
    clear,

    // fetchUserInfo,
    updateUserInfo,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: false,
})
