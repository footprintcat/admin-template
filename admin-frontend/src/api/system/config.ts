import { get } from '@/utils/api'

const API_PREFIX = '/manage/v1/system/config'
const getUrl = (url: string) => API_PREFIX + url

/**
 * 获取系统配置
 *
 * @returns 系统配置
 */
export function getSystemConfig() {
  const url = getUrl('/info')
  return get<SystemConfig>(url)
}

export interface SystemConfig {
  loginCaptchaEnabled: boolean
}
