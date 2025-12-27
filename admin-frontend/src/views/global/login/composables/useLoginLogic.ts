import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import settings from '@/utils/settings'
import { systemUserAuthLogin } from '@/api/system/user-auth'
import { redirectAfterLogin } from '@/router/guards/scripts/redirect_to'
import { useIdentityStore } from '@/stores/system/identity'
import { useTabsStore } from '@/stores/tabs'
import { useUserStore } from '@/stores/user'
import type { ValidateError, ValidateFieldsError } from 'async-validator'

// 登录信息接口
export interface LoginInfo {
  username: string
  password: string
}

/**
 * 登录逻辑 组合式函数
 */
export function useLoginLogic() {
  const router = useRouter()

  const tabs = useTabsStore()

  const userStore = useUserStore()
  const identityStore = useIdentityStore()

  const loading = ref<boolean>(false)

  // 登录表单数据
  const param = reactive<LoginInfo>({
    username: '',
    password: '',
  })

  if (settings.debugMode) {
    param.username = 'admin'
    param.password = '123456'
  }

  // 表单验证规则
  const rules: FormRules = {
    username: [
      {
        required: true,
        message: '请输入用户名',
        trigger: 'blur',
      },
    ],
    password: [
      {
        required: true,
        message: '请输入密码',
        trigger: 'blur',
      },
    ],
  }

  /**
   * 提交登录表单
   * @param formEl - 表单实例
   */
  const submitForm = (formEl: FormInstance | null) => {
    if (!formEl) return

    formEl.validate(async (isValid: boolean, invalidFields?: ValidateFieldsError) => {
      if (!isValid) { // isValid = false 时, invalidFields 一定存在
        console.log('invalidFields', invalidFields)

        // 对表单中的每一个不合法输入框进行遍历
        Object.values(invalidFields!).forEach((input: ValidateError[]) => {
          // console.log("input", input)
          // 对该不合法输入框的提示信息进行遍历
          input.forEach((element: ValidateError) => {
            // console.log("element", element)
            ElMessage.error({ message: element.message, grouping: true })
          })
        })
        return
      }

      loading.value = true
      systemUserAuthLogin({
        username: param.username,
        password: param.password,
      }).then(async ({ data, raw }) => {
        if (!raw.isSuccess) {
          return // 后端返回异常已弹出错误提示
        }
        ElMessage.success('登录成功')
        console.log('登录成功', data)

        const userInfo = data.userInfo
        userStore.updateUserInfo(userInfo)

        const identityList = data.identityList
        identityStore.setUserIdentityListAfterLogin(identityList)

        console.log('当前用户拥有', identityList.length, '个身份', identityList)
        const haveAndOnlyHaveOneIdentity = identityList.length === 1
        if (haveAndOnlyHaveOneIdentity) {
          identityStore.switchCurrentIdentity(identityList[0])
        }
        redirectAfterLogin(router, { gotoChooseIdentity: !haveAndOnlyHaveOneIdentity })
      }).finally(() => {
        loading.value = false
      })
    })
  }

  // 清除标签页
  tabs.clearTabs()

  return {
    param,
    rules,
    submitForm,
    loading,
  }
}
