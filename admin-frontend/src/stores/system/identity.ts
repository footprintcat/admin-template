import { readonly, ref } from 'vue'
import { defineStore } from 'pinia'
import type { IdentityDto } from '@/types/backend/dto/IdentityDto'

export const useIdentityStore = defineStore('identity', () => {

  const identityDto = ref<IdentityDto | null>(null)
  const identityDtoReadonly = readonly(identityDto) // 只读属性

  const userIdentityDtoList = ref<Array<IdentityDto> | null>(null)
  const userIdentityDtoListReadonly = readonly(userIdentityDtoList) // 只读属性

  function setUserIdentityListAfterLogin(list: Array<IdentityDto> | null) {
    userIdentityDtoList.value = list
  }

  return {
    identityDto: identityDtoReadonly,
    userIdentityDtoList: userIdentityDtoListReadonly,
    setUserIdentityListAfterLogin,
  }
}, {
  // docs: https://prazdevs.github.io/pinia-plugin-persistedstate/zh/guide/
  persist: false,
})
