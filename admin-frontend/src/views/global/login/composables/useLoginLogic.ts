import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { usePermissionStore } from '@/stores/permission'
import { useSidebarStore } from '@/stores/sidebar'
import { useTabsStore } from '@/stores/tabs'
import useUserStore from '@/stores/user'
import type { ValidateError, ValidateFieldsError } from 'async-validator'

// 登录信息接口
export interface LoginInfo {
  username: string
  password: string
}

// 用户信息接口
export interface UserInfo {
  username: string
  id: string
  roleId: number
  telephone: string | null
}

// 权限信息接口
export interface PrivilegeInfo {
  id: number
  roleId: number
  privilegeName: string
  module: string
}

/**
 * 登录逻辑 组合式函数
 */
export function useLoginLogic() {
  const router = useRouter()

  const tabs = useTabsStore()

  const permissionStore = usePermissionStore()
  const sidebarStore = useSidebarStore()
  const userStore = useUserStore()

  const loading = ref<boolean>(false)

  // 登录表单数据
  const param = reactive<LoginInfo>({
    username: '',
    password: '',
  })

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

  // 表单引用
  const loginFormRef = ref<FormInstance>()

  /**
   * 提交登录表单
   * @param formEl - 表单实例
   */
  const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return

    formEl.validate(async (isValid: boolean, invalidFields?: ValidateFieldsError) => {
      if (!isValid) {
        // ElMessage.error('请填写用户名或密码')

        console.log('invalidFields', invalidFields)
        // 对表单中的每一个不合法输入框进行遍历
        Object.values(invalidFields).forEach((input: ValidateError[]) => {
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
      // await send_request_without_loading('v1/user/login', 'POST', {
      //   userName: param.username,
      //   passWord: param.password,
      // }, async (data: UserInfo) => {
      //   // 判断用户是否登录成功
      //   if (!data) {
      //     ElMessage.error('用户名或密码错误')
      //     return
      //   }
      //   ElMessage.success('登录成功')

      //   userStore.set(data.id, data.roleId, data.username)

      //   await permiss.asyncUpdatePermissList(data.roleId, false)

      //   const fullPath = router.currentRoute?.value?.query?.redirectTo as string
      //   const path = fullPath?.split('?')[0]
      //   if (path && !path.includes('/login')) {
      //     router.push(fullPath)
      //   } else {
      //     router.push({ name: 'dashboard' })
      //   }

      //   try {
      //     // 如果是大屏
      //     const isBigScreen = data.username == 'screen'
      //     if (isBigScreen) {
      //       // 默认折叠 Sidebar
      //       sidebarStore.collapse = true
      //       // 跳转到 模型总览
      //       router.push({ name: '3dmodel-general' })
      //     }
      //   } catch (err) {
      //     console.error(err)
      //   }
      // })
      loading.value = false
    })
  }

  // 清除标签页
  tabs.clearTabs()

  return {
    param,
    rules,
    loginFormRef,
    submitForm,
    loading,
  }
}
