<template>
  <div class="captcha-container">
    <el-input ref="inputRef" v-model="inputCaptcha" :placeholder="props.placeholder" size="large" style="flex: 1"
      @keyup.enter="emit('enter')">
      <template #prepend>
        <el-icon>
          <Key />
        </el-icon>
      </template>
    </el-input>
    <div class="captcha-image-wrapper" @click="refreshCaptcha">
      <img v-if="captchaImageUrl" :src="captchaImageUrl" alt="验证码" class="captcha-image" @error="handleImageError" />
      <div v-else class="captcha-loading">
        <el-icon class="is-loading">
          <Loading />
        </el-icon>
      </div>
      <div class="captcha-refresh-tip">
        <el-icon>
          <Refresh />
        </el-icon>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Key, Loading, Refresh } from '@element-plus/icons-vue'
import { getCaptchaImageUrl } from '@/api/system/captcha'

interface Props {
  placeholder?: string
}

const props = withDefaults(defineProps<Props>(), {
  placeholder: '请输入验证码',
})

const emit = defineEmits<{
  enter: []
  clearValidate: []
}>()

const inputRef = useTemplateRef('inputRef')

const inputCaptcha = defineModel<string>()
const captchaImageUrl = ref<string>('')
const loading = ref(false)

// 获取验证码图片
const fetchCaptcha = async () => {
  loading.value = true
  captchaImageUrl.value = ''
  try {
    captchaImageUrl.value = getCaptchaImageUrl()
    inputCaptcha.value = ''
    emit('clearValidate')
  } catch (error) {
    console.error('获取验证码失败:', error)
    ElMessage.error('获取验证码失败，请刷新页面重试')
  } finally {
    loading.value = false
  }
}

// 刷新验证码
const refreshCaptcha = () => {
  fetchCaptcha()
  inputRef.value?.focus()
}

// 处理图片加载错误
const handleImageError = () => {
  ElMessage.error('验证码加载失败，请刷新重试')
  refreshCaptcha()
}

onMounted(() => {
  fetchCaptcha()
})

// 暴露刷新方法
defineExpose({
  refreshCaptcha,
})
</script>

<style scoped>
.captcha-container {
  display: flex;
  gap: 12px;
  align-items: center;
}

.captcha-image-wrapper {
  position: relative;
  cursor: pointer;
  border-radius: 4px;
  overflow: hidden;
  transition: all 0.3s;
  width: 120px;
  height: 40px;
  background: #f5f5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  user-select: none;
}

.captcha-image-wrapper:hover {
  box-shadow: 0 0 8px rgba(0, 0, 0, 0.15);
}

.captcha-image {
  display: block;
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.captcha-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.captcha-refresh-tip {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0);
  color: white;
  opacity: 0;
  transition: all 0.3s;
  font-size: 20px;
}

.captcha-image-wrapper:hover .captcha-refresh-tip {
  opacity: 1;
  background: rgba(0, 0, 0, 0.2);
}
</style>
