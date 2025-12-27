import { get, post } from '@/utils/api'
import type { IdentityDto } from '@/types/backend/dto/IdentityDto'

const API_PREFIX = '/manage/v1/system/identity'
const getUrl = (url: string) => API_PREFIX + url

export interface IdentityInfoResponse {
  identityList: IdentityDto[]
  currentIdentity: IdentityDto | null
}

/**
 * 获取用户身份信息（合并接口，返回身份列表和当前身份）
 *
 * @returns
 */
export function getIdentityInfo() {
  const url = getUrl('/info')
  return get<IdentityInfoResponse>(url)
}

/**
 * 切换身份
 *
 * @param identityId 身份ID
 * @returns
 */
export function switchIdentity(identityId: number) {
  const url = getUrl('/switch')
  return post<null>(url, { identityId })
}

/**
 * 退出身份
 *
 * @param identityId 身份ID
 * @returns
 */
export function exitIdentity() {
  const url = getUrl('/exit')
  return post<null>(url, {})
}
