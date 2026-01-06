import { onMounted, onUnmounted, ref } from 'vue'

export function useWindowResize({
  callback,
}: {
  callback?: () => void
} = {}) {
  const width = ref(window.innerWidth)
  const height = ref(window.innerHeight)

  const update = () => {
    width.value = window.innerWidth
    height.value = window.innerHeight
    if (callback) {
      callback()
    }
  }

  onMounted(() => {
    window.addEventListener('resize', update)
  })

  onUnmounted(() => {
    window.removeEventListener('resize', update)
  })

  return { width, height }
}
