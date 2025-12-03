/* eslint-disable @typescript-eslint/no-explicit-any */
import axios, { type AxiosInstance } from 'axios'

const baseURL = 'http://localhost:8412'

// 创建axios实例
const request: AxiosInstance = axios.create({
  baseURL,
  timeout: 6000,
  headers: {
    'Content-Type': 'application/json',
  },
})

// GET请求 - 直接返回Promise
export const get = <T = any>(url: string, params?: any): Promise<T> => {
  const promise = request.get<T>(url, { params }).then(response => response.data)
  // 这里可以做一些异常处理
  return promise
}

// POST请求 - 直接返回Promise
export const post = <T = any>(url: string, data?: any): Promise<T> => {
  const promise = request.post<T>(url, data).then(response => response.data)
  // 这里可以做一些异常处理
  return promise
}
