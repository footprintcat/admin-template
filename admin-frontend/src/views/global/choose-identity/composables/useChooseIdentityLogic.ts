import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { redirectAfterChooseIdentity } from '@/router/guards/scripts/redirect_to'
import { useIdentityStore } from '@/stores/system/identity'
import type { IdentityDto } from '@/types/backend/dto/system/IdentityDto'

/**
 * 登录逻辑 组合式函数
 */
export function useChooseIdentityLogic() {

  const router = useRouter()
  const route = useRoute()
  const identityStore = useIdentityStore()

  const loading = ref(false)
  const selectedId = ref<number | null>(null)

  const identityList = computed(() => {
    const list = identityStore.userIdentityDtoList || []
    // return list
    // 将当前的 identity 排在最上面
    // return [...list].sort((a, b) => a.id === currentIdentity.value?.id ? -1 : 0)
    const newList = [...list]
    // 找到当前项的索引
    const index = newList.findIndex((item) => item.id === currentIdentity.value?.id)

    // 如果找到了，移动到数组顶部
    if (index > 0) { // index > 0 表示不是已经在最顶部
      const [currentItem] = newList.splice(index, 1) // 删除并返回当前项
      newList.unshift(currentItem!) // 插入到数组开头
    }

    return newList
  })
  const currentIdentity = computed(() => identityStore.currentIdentity)

  const redirectTo = computed(() => route.query.redirectTo as string | undefined)
  // 已选择过身份，存在 redirectTo 参数时，才显示返回按钮
  const showBackBtn = computed(() => !!currentIdentity.value && !!redirectTo.value)

  onMounted(() => {
    console.log('身份选择页初始化')
    if (identityList.value.length === 0) {
      ElMessage.warning('未获取到身份列表，暂无法登录')
      // redirectToLogin(router, false)
    }
  })

  const handleSelectIdentity = async (identity: IdentityDto) => {
    const isFirstSwitch = !currentIdentity.value

    if (loading.value) return

    selectedId.value = identity.id
    loading.value = true

    try {
      await identityStore.switchCurrentIdentity(identity)
      ElMessage.success(isFirstSwitch ? '身份选择成功' : '身份切换成功')
      redirectAfterChooseIdentity(router)
    } catch {
      ElMessage.error('身份选择失败，请重试')
    } finally {
      loading.value = false
      selectedId.value = null
    }
  }

  const handleBack = () => {
    redirectAfterChooseIdentity(router)
  }

  return {
    selectedId,
    currentIdentity,
    identityList,
    handleSelectIdentity,
    loading,
    showBackBtn,
    handleBack,
  }
}
