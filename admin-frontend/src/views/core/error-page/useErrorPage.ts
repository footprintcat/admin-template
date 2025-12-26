import { type Component, defineAsyncComponent } from 'vue'

export type ErrorCode =
  | '403'
  | '404'

const errorCode = ref<ErrorCode | null>(null)
const errorCodeReadonly = readonly(errorCode)
const showErrorPage = computed<boolean>(() => errorCode.value !== null)

function setErrorCode(code: ErrorCode | null) {
  console.log('setErrorCode', code)
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
