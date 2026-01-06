import { post } from '@/utils/api'
import type { RequestParam } from '@/components/core/manage-list/types/request-param'
import type { ManageListResponse } from '@/types/backend/common/manage-list-response'

const API_PREFIX = '/manage/v1/system/user'
const getUrl = (url: string) => API_PREFIX + url

// 接口请求参数
export interface ManageSystemUserAuthLoginRequest {
  username: string
  password: string
}

/**
 * 用户管理 分页查询
 *
 * @param params
 * @returns
 */
export function systemUserPage(params: RequestParam<Record<string, unknown>>) {
  const url = getUrl('/list')
  return post<ManageListResponse<unknown>>(url, params)
}
