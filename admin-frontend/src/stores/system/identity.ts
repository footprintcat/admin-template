import { readonly, ref } from 'vue'
import { defineStore } from 'pinia'
import { exitIdentity, getIdentityInfo, switchIdentity } from '@/api/system/identity'
import type { IdentityDto } from '@/types/backend/dto/IdentityDto'
import { usePermissionStore } from '../permission'

export const useIdentityStore = defineStore('identity', () => {

  // 获取用户权限
  const permissionStore = usePermissionStore()

  // current identity
  const currentIdentity = ref<IdentityDto | null>(null)
  const currentIdentityReadonly = readonly(currentIdentity)

  async function fetchIdentityInfo() {
    if (userIdentityDtoList.value && currentIdentity.value) return
    const { data } = await getIdentityInfo()
    userIdentityDtoList.value = data.identityList
    currentIdentity.value = data.currentIdentity

    // 更新权限
    await permissionStore.asyncUpdatePermissionList()
  }

  function switchCurrentIdentity(identity: IdentityDto) {
    // 先调用接口保存到后端

    return switchIdentity(identity.id)
      .then(async () => {
        // 然后更新 currentIdentity
        currentIdentity.value = identity

        // 最后更新权限
        permissionStore.clearCurrentPermission()
        await permissionStore.asyncUpdatePermissionList()
      })
  }

  function exitCurrentIdentity(clearIdentityList: boolean): Promise<unknown> {
    currentIdentity.value = null
    permissionStore.clearCurrentPermission()
    return exitIdentity()
  }

  // identity list
  const userIdentityDtoList = ref<Array<IdentityDto> | null>(null)
  const userIdentityDtoListReadonly = readonly(userIdentityDtoList)

  function setUserIdentityListAfterLogin(list: Array<IdentityDto> | null) {
    userIdentityDtoList.value = list
  }

  function clearIdentityList() {
    userIdentityDtoList.value = []
  }

  return {
    // current identity
    currentIdentity: currentIdentityReadonly,
    userIdentityDtoList: userIdentityDtoListReadonly,
    fetchIdentityInfo,
    switchCurrentIdentity,
    exitCurrentIdentity,

    // identity list
    setUserIdentityListAfterLogin,
    clearIdentityList,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: false,
})
