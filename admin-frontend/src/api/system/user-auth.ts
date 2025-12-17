import { post } from '@/utils/api'
import type { IdentityDto } from '@/types/backend/dto/IdentityDto'
import type { SystemUserDto } from '@/types/backend/dto/SystemUserDto'

const API_PREFIX = '/manage/v1/system/user-auth'
const getUrl = (url: string) => API_PREFIX + url


// 接口请求参数
export interface ManageSystemUserAuthLoginRequest {
  username: string
  password: string
}

// 接口响应参数
export interface ManageSystemUserAuthLoginResponse {
  userInfo: SystemUserDto
  identityList: Array<IdentityDto>
}

/**
 * 后台管理登录接口
 *
 * @param params
 * @returns
 */
export function systemUserAuthLoginPost(params: ManageSystemUserAuthLoginRequest) {
  const url = getUrl('/login')
  return post<ManageSystemUserAuthLoginResponse>(url, params)
}

/**
 * 退出登录接口
 *
 * @param params
 * @returns
 */
export function systemUserAuthLogoutPost() {
  const url = getUrl('/logout')
  return post<SystemUserDto>(url)
}
