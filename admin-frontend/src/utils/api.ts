/* eslint-disable @typescript-eslint/no-explicit-any */
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import axios, { type AxiosInstance, type AxiosRequestConfig, type ResponseType } from 'axios'
import { usePromiseLoading } from '@/composables/useLoading'
import { BusinessErrorCode } from '@/types/backend/common/business-error-code'
import type { CommonReturn } from '@/types/backend/common/common-return'
import { userLogout } from './user_utils'

interface Extra {
  timeout?: number
  responseType?: ResponseType
  /**
   * 请求失败后，是否弹出失败信息
   * 默认为 true, 除非显式指定 false
   */
  showErrorWhenFailed?: boolean
  /**
   * 请求时是否展示全局 Loading 提示
   */
  showLoading?: boolean
}

const baseURL = import.meta.env.VITE_API_BASE_URL

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL,
  timeout: 6000,
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // 允许携带 Cookie
})

// GET请求 - 直接返回Promise
export const rawGet = <T = any>(url: string, params?: any, extra?: Extra): Promise<T> => {
  const axiosConfig: AxiosRequestConfig = {
    params,
    timeout: extra?.timeout,
  }
  const promise = request.get<T>(url, axiosConfig).then((response) => response.data)
  // 这里可以做一些异常处理
  return promise
}

// POST请求 - 直接返回Promise
export const rawPost = <T = any>(url: string, data?: any, extra?: Extra): Promise<T> => {
  const axiosConfig: AxiosRequestConfig = {
    timeout: extra?.timeout,
  }
  const promise = request.post<T>(url, data, axiosConfig).then((response) => response.data)
  // 这里可以做一些异常处理
  return promise
}

export interface ApiCommonReturnType<T> {
  raw: CommonReturn<T>
  data: T
  isSuccess: boolean
  errCode: BusinessErrorCode | null
  toastMessage: string
  isErrorMessageShown: boolean
}

// GET请求 - 直接返回Promise
export const get = <T = any>(
  url: string,
  params?: any,
  extra?: Extra,
): Promise<ApiCommonReturnType<T>> => {
  const promise = rawGet<CommonReturn<T>>(url, params, extra)
    .then((result) => {
      const toastMessage = result.message || '服务器内部错误，请稍后再试'
      let isErrorMessageShown: boolean = false
      if (!result.isSuccess && extra?.showErrorWhenFailed !== false /* undefined || true */) {
        ElMessage.error({
          message: toastMessage,
          grouping: true,
        })
        isErrorMessageShown = true

        // 若用户未登录
        if (result.errCode === BusinessErrorCode.USER_NOT_LOGIN) {
          const router = useRouter()
          userLogout(router, true)
        }
      }

      return {
        raw: result,
        data: result.data,
        isSuccess: result.isSuccess,
        errCode: result.errCode,
        toastMessage: result.isSuccess ? toastMessage : '',
        isErrorMessageShown,
      }
    })
    .catch((err) => {
      ElMessage.error({
        message: '请求失败，请检查网络连接',
        grouping: true,
      })
      throw err
    })
  if (extra?.showLoading) {
    return usePromiseLoading(promise)
  }
  return promise
}

// POST请求 - 直接返回Promise
export const post = <T = any>(
  url: string,
  data?: any,
  extra?: Extra,
): Promise<ApiCommonReturnType<T>> => {
  const promise = rawPost<CommonReturn<T>>(url, data, extra)
    .then((result) => {
      const toastMessage = result.message || '服务器内部错误，请稍后再试'
      let isErrorMessageShown: boolean = false
      if (!result.isSuccess && extra?.showErrorWhenFailed !== false /* undefined || true */) {
        ElMessage.error({
          message: toastMessage,
          grouping: true,
        })
        isErrorMessageShown = true

        // 若用户未登录
        if (result.errCode === BusinessErrorCode.USER_NOT_LOGIN) {
          const router = useRouter()
          userLogout(router, true)
        }
      }

      return {
        raw: result,
        data: result.data,
        isSuccess: result.isSuccess,
        errCode: result.errCode,
        toastMessage: result.isSuccess ? toastMessage : '',
        isErrorMessageShown,
      }
    })
    .catch((err) => {
      ElMessage.error({
        message: '请求失败，请检查网络连接',
        grouping: true,
      })
      throw err
    })
  if (extra?.showLoading) {
    return usePromiseLoading(promise)
  }
  return promise
}

// POST请求 - 直接返回Promise
export const downloadPost = (
  url: string,
  data?: any,
  extra?: Extra,
): Promise<void> => {
  const promise = request.post(url, data, { responseType: 'blob', ...extra })
    .then(async (result) => {
      if (result.headers['content-type'] === 'application/json') { // 导出失败
        const jsonStr = await result.data.text()
        const data = JSON.parse(jsonStr)

        const toastMessage = data.message || '服务器内部错误，请稍后再试'
        let isErrorMessageShown: boolean = false
        if (!data.isSuccess && extra?.showErrorWhenFailed !== false /* undefined || true */) {
          ElMessage.error({
            message: toastMessage,
            grouping: true,
          })
          isErrorMessageShown = true

          // 若用户未登录
          if (data.errCode === BusinessErrorCode.USER_NOT_LOGIN) {
            const router = useRouter()
            userLogout(router, true)
          }
        }

        if (isErrorMessageShown) {
          return
        }
      }

      const filename = decodeURIComponent(result.headers['data-filename'])
      const blobUrl = window.URL.createObjectURL(new Blob([result.data]))

      const link = document.createElement('a')
      link.href = blobUrl
      link.setAttribute('download', filename)
      // link.setAttribute('download', 'file.xlsx')
      document.body.appendChild(link)
      link.click()
      link.remove()
    })
    .catch((err) => {
      ElMessage.error({
        message: '请求失败，请检查网络连接',
        grouping: true,
      })
      throw err
    })
  if (extra?.showLoading) {
    return usePromiseLoading(promise)
  }
  return promise
}
