import { computed, defineAsyncComponent } from 'vue'
import type { Component } from 'vue'
import type { RouteLocationNormalizedGeneric } from 'vue-router'

export type ErrorCode =
  | '403'
  | '404'

const errorCode = ref<ErrorCode | null>(null)
const errorCodeReadonly = readonly(errorCode)
const showErrorPage = computed<boolean>(() => errorCode.value !== null)

/**
 * @param code ErrorCode
 * @param to   跳转到的路由, 仅用于 console.log 打印
 */
function setErrorCode(code: ErrorCode | null, to: RouteLocationNormalizedGeneric) {
  console.log('setErrorCode', code, 'to', to)
  errorCode.value = code
}

// { [key in ErrorCode | null]: Component | undefined }
const errorPage: Record<ErrorCode, Component | undefined> = {
  '403': defineAsyncComponent(() => import('@/views/core/error-page/403.vue')),
  '404': defineAsyncComponent(() => import('@/views/core/error-page/404.vue')),
}

const currentErrorPage = computed<Component | undefined>(() => errorCode.value === null ? undefined : errorPage[errorCode.value])

export function useErrorPage() {
  return {
    currentErrorPage,
    errorCode: errorCodeReadonly,
    showErrorPage: showErrorPage,
    setErrorCode,
  }
}
