import { readonly, ref } from 'vue'
import { defineStore } from 'pinia'
import type { IdentityInfoResponse } from '@/api/system/identity'
import { exitIdentity, getIdentityInfo, switchIdentity } from '@/api/system/identity'
import { BusinessErrorCode } from '@/types/backend/common/business-error-code'
import type { IdentityDto } from '@/types/backend/dto/system/IdentityDto'
import { usePermissionStore } from '../permission'
import { useUserStore } from '../user'

export const useIdentityStore = defineStore('identity', () => {

  const userStore = useUserStore()
  const permissionStore = usePermissionStore()

  const isInit = ref<boolean>(false)
  const isInitReadonly = readonly(isInit)

  async function init(params: {
    /** 如果已经初始化，那么是否需要重新初始化 */
    reInit: boolean
  }) {
    if (!userStore.isInit) {
      console.error('应先初始化 userStore 再初始化 identityStore')
      return
    }

    if (params.reInit) {
      clear({ exit: false })
    }

    if (isInit.value) {
      return
    }

    const isLogin = userStore.isLogin
    if (isLogin) { // 已登录
      // 网页加载后立即获取一次当前登录身份信息
      await fetchIdentityInfo()
    } else { // 未登录
      clear({ exit: false })
    }
    isInit.value = true
  }

  function clear(params: { exit: boolean }): Promise<unknown> {
    _clearCurrentIdentity()
    _clearIdentityList()
    isInit.value = false
    if (params.exit) {
      return exitIdentity()
    }
    return Promise.resolve()
  }

  // current identity
  const currentIdentity = ref<IdentityDto | null>(null)
  const currentIdentityReadonly = readonly(currentIdentity)

  const isFetching = ref<boolean>(false)

  async function fetchIdentityInfo(): Promise<void> {
    isFetching.value = true

    let data: IdentityInfoResponse
    let errCode: BusinessErrorCode | null
    try {
      // const { data, errCode } = await getIdentityInfo()
      const result = await getIdentityInfo()
      data = result.data
      errCode = result.errCode
    } catch (err) {
      console.error('fetchIdentityInfo err', err)
      await ElMessageBox
        .alert('服务器连接失败，请检查网络连接', '网络异常', {
          showClose: false,
          closeOnClickModal: false,
          confirmButtonText: '点击重试',
          type: 'error',
        })
        .then(async () => {
          await fetchIdentityInfo()
        })
      return
    }

    if (!errCode) {
      userIdentityDtoList.value = data.identityList
      currentIdentity.value = data.currentIdentity
      // 更新权限
      permissionStore.savePermissionList(data.menuList, { clearFirst: true })
    } else {
      if (errCode === BusinessErrorCode.USER_NOT_LOGIN) {
        clear({ exit: false })
      }
    }

    isFetching.value = false
  }

  async function switchCurrentIdentity(identity: IdentityDto) {
    await switchIdentity(identity.id)
    return fetchIdentityInfo()
  }

  function _clearCurrentIdentity(): void {
    currentIdentity.value = null
    permissionStore.clearCurrentPermission()
  }

  // identity list
  const userIdentityDtoList = ref<Array<IdentityDto> | null>(null)
  const userIdentityDtoListReadonly = readonly(userIdentityDtoList)

  function setUserIdentityListAfterLogin(list: Array<IdentityDto> | null) {
    userIdentityDtoList.value = list
  }

  function _clearIdentityList() {
    userIdentityDtoList.value = []
  }

  return {
    isInit: isInitReadonly,
    init,
    clear,

    // current identity
    currentIdentity: currentIdentityReadonly,
    userIdentityDtoList: userIdentityDtoListReadonly,
    // fetchIdentityInfo,
    switchCurrentIdentity,
    // _clearCurrentIdentity,

    // identity list
    setUserIdentityListAfterLogin,
    // _clearIdentityList,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: false,
})
