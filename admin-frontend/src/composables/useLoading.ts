import { getCurrentInstance, ref } from 'vue'
import { ElLoading } from 'element-plus'
import type { LoadingInstance } from 'element-plus/es/components/loading/src/loading.mjs'

/**
 * useLoading, usePromiseLoading 的实现
 *
 * @since 2025.08.05
 */

const count = ref<number>(0)

let loading: LoadingInstance

/**
 * show 和 hide 必须成对调用
 * @returns
 */
function show() {
  count.value++
  if (count.value > 1) {
    return
  }
  const currentInstance = getCurrentInstance()

  loading = ElLoading.service(
    {
      lock: true,
      text: '请稍候...',
      background: 'rgba(0, 0, 0, 0.2)',
    },
    currentInstance !== null ? currentInstance.appContext : undefined,
  )
}

function hide() {
  count.value--
  if (count.value > 0) {
    return
  }
  loading.close()
}

export function useLoading() {
  return {
    show,
    hide,
  }
}

export function usePromiseLoading<T>(promise: Promise<T>): Promise<T> {
  show()
  promise.finally(hide)
  return promise
}
