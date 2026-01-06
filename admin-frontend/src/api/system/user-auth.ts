import { post } from '@/utils/api'
import type { IdentityDto } from '@/types/backend/dto/system/IdentityDto'
import type { UserDto } from '@/types/backend/dto/system/UserDto'

const API_PREFIX = '/manage/v1/system/user-auth'
const getUrl = (url: string) => API_PREFIX + url

// 接口请求参数
export interface ManageSystemUserAuthLoginRequest {
  username: string
  password: string
}

// 接口响应参数
export interface ManageSystemUserAuthLoginResponse {
  userInfo: UserDto
  identityList: Array<IdentityDto>
}

/**
 * 后台管理登录接口
 *
 * @param params
 * @returns
 */
export function systemUserAuthLogin(params: ManageSystemUserAuthLoginRequest) {
  const url = getUrl('/login')
  return post<ManageSystemUserAuthLoginResponse>(url, params)
}

/**
 * 退出登录接口
 *
 * @param params
 * @returns
 */
export function systemUserAuthLogout() {
  const url = getUrl('/logout')
  return post<string>(url)
}

/**
 * 获取当前登录的用户信息
 *
 * @returns
 */
export function systemUserAuthGetInfo() {
  const url = getUrl('/getInfo')
  return post<UserDto | null>(url, undefined, {
    showErrorWhenFailed: false,
    showLoading: true,
  })
}
