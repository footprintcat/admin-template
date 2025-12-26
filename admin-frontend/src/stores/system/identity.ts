import { readonly, ref } from 'vue'
import { defineStore } from 'pinia'
import { getIdentityInfo, switchIdentity } from '@/api/system/identity'
import type { IdentityDto } from '@/types/backend/dto/IdentityDto'

export const useIdentityStore = defineStore('identity', () => {

  const currentIdentity = ref<IdentityDto | null>(null)
  const currentIdentityReadonly = readonly(currentIdentity)

  const userIdentityDtoList = ref<Array<IdentityDto> | null>(null)
  const userIdentityDtoListReadonly = readonly(userIdentityDtoList)

  async function fetchIdentityInfo() {
    if (userIdentityDtoList.value && currentIdentity.value) return
    const { data } = await getIdentityInfo()
    userIdentityDtoList.value = data.identityList
    currentIdentity.value = data.currentIdentity
  }

  function setUserIdentityListAfterLogin(list: Array<IdentityDto> | null) {
    userIdentityDtoList.value = list
  }

  function setCurrentIdentity(identity: IdentityDto | null) {
    currentIdentity.value = identity
  }

  async function saveSelectedIdentityToBackend(identityId: number) {
    await switchIdentity(identityId)
  }

  return {
    currentIdentity: currentIdentityReadonly,
    userIdentityDtoList: userIdentityDtoListReadonly,
    fetchIdentityInfo,
    setUserIdentityListAfterLogin,
    setCurrentIdentity,
    saveSelectedIdentityToBackend,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: false,
})
