/// <reference types="vite/client" />

// 声明全局变量
declare const __APP_BUILD_TIME__: number

interface ImportMetaEnv {
  /** 网站名称 */
  readonly VITE_APP_TITLE: string
  /** 网站全称 */
  readonly VITE_APP_FULL_TITLE: string
  /** 后端 API 接口地址前缀 */
  readonly VITE_API_BASE_URL: string
  // 更多环境变量...
}
