import axios from 'axios'
import type { AxiosError, AxiosInstance, AxiosResponse, InternalAxiosRequestConfig } from 'axios'

const service: AxiosInstance = axios.create({
  timeout: 6000,
})

service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    return config
  },
  (error: AxiosError) => {
    console.log(error)
    return Promise.reject(error)
  },
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    if (response.status === 200) {
      return response
    } else {
      return Promise.reject('response.status != 200')
    }
  },
  (error: AxiosError) => {
    console.log(error)
    return Promise.reject(error)
  },
)

export default service
