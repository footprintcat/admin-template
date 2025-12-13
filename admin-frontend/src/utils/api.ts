/* eslint-disable @typescript-eslint/no-explicit-any */
import { ElMessage } from 'element-plus'
import axios, { type AxiosInstance, type AxiosRequestConfig } from 'axios'
import type { CommonReturn } from '@/types/backend/common-return'

interface Extra {
  timeout?: number
  /**
   * 请求失败后，是否弹出失败信息
   * 默认为 true, 除非显式指定 false
   */
  showErrorWhenFailed?: boolean
}

const baseURL = 'http://localhost:8412'

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
  const promise = request.get<T>(url, axiosConfig).then(response => response.data)
  // 这里可以做一些异常处理
  return promise
}

// POST请求 - 直接返回Promise
export const rawPost = <T = any>(url: string, data?: any, extra?: Extra): Promise<T> => {
  const axiosConfig: AxiosRequestConfig = {
    timeout: extra?.timeout,
  }
  const promise = request.post<T>(url, data, axiosConfig).then(response => response.data)
  // 这里可以做一些异常处理
  return promise
}

// GET请求 - 直接返回Promise
export const get = <T = any>(url: string, params?: any, extra?: Extra): Promise<{
  raw: CommonReturn<T>
  data: T
  isSuccess: boolean
  toastMessage: string
  isErrorMessageShown: boolean
}> => {
  const promise = rawGet<CommonReturn<T>>(url, params, extra)
    .then(result => {
      const toastMessage = result.message || '服务器内部错误，请稍后再试'
      let isErrorMessageShown: boolean = false
      if (!result.isSuccess && extra?.showErrorWhenFailed !== false /* undefined || true */) {
        ElMessage.error({
          message: toastMessage,
          grouping: true,
        })
        isErrorMessageShown = true
      }

      return {
        raw: result,
        data: result.data,
        isSuccess: result.isSuccess,
        toastMessage,
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
  return promise
}

// POST请求 - 直接返回Promise
export const post = <T = any>(url: string, data?: any, extra?: Extra): Promise<{
  raw: CommonReturn<T>
  data: T
  isSuccess: boolean
  toastMessage: string
  isErrorMessageShown: boolean
}> => {
  const promise = rawPost<CommonReturn<T>>(url, data, extra)
    .then(result => {
      const toastMessage = result.message || '服务器内部错误，请稍后再试'
      let isErrorMessageShown: boolean = false
      if (!result.isSuccess && extra?.showErrorWhenFailed !== false /* undefined || true */) {
        ElMessage.error({
          message: toastMessage,
          grouping: true,
        })
        isErrorMessageShown = true
      }

      return {
        raw: result,
        data: result.data,
        isSuccess: result.isSuccess,
        toastMessage,
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
  return promise
}
