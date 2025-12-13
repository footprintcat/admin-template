import type { CommonReturn } from '@/types/backend/common-return'
import type { SystemUserDto } from '@/types/backend/dto/SystemUserDto'
import { post } from '@/utils/api'

const API_PREFIX = '/v2/manage/system/user-auth'
const getUrl = (url: string) => API_PREFIX + url


// 接口请求参数
export interface ManageSystemUserAuthLoginRequest {
  username: string
  password: string
}

/**
 * 后台管理登录接口
 *
 * @param params
 * @returns
 */
export function systemUserAuthLoginPost(params: ManageSystemUserAuthLoginRequest) {
  const url = getUrl('/login')
  return post<CommonReturn<SystemUserDto>>(url, params)
}
