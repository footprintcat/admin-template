import { get } from '@/utils/api'
import type { IdentityDto } from '@/types/backend/dto/IdentityDto'

const API_PREFIX = '/manage/v1/system/identity'
const getUrl = (url: string) => API_PREFIX + url

/**
 * 获取用户当前登录的身份
 *
 * @param params
 * @returns
 */
export function getCurrentIdentity() {
  const url = getUrl('/getCurrentIdentity')
  return get<IdentityDto>(url)
}
