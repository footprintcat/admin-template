import { get } from '@/utils/api'

const API_PREFIX = '/manage/v1/system/privilege'
const getUrl = (url: string) => API_PREFIX + url

export interface PrivilegeResponse {
  menuCodeList: string[]
  combinedCodeList: string[]
}

export function getCurrentUserPrivilegeList() {
  const url = getUrl('/getCurrentUserPrivilegeList')
  return get<PrivilegeResponse>(url)
}
