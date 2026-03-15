<template>
  <div class="login-wrap">
    <div class="login-container">
      <div class="login-wrapper">
        <!-- 左侧登录表单区域 -->
        <!--
          v-loading 是 element-plus 自带自定义指令
          docs: https://element-plus.org/zh-CN/component/loading#%E5%8C%BA%E5%9F%9F%E5%8A%A0%E8%BD%BD
        -->
        <div class="login-form" v-loading="loading">
          <div class="login-header">
            <div class="logo">
              <img :src="baseUrl + '/favicon.svg'" alt="Logo" />
            </div>
            <h1 class="title">{{ siteFullTitle }}</h1>
            <p>请登录以继续</p>
          </div>

          <el-form :model="param" :rules="rules" ref="loginFormRef" label-width="0px" class="form-content"
            @keyup.enter="submitForm(loginFormRef)">
            <!-- 用户名输入框 -->
            <el-form-item prop="username">
              <el-input v-model="param.username" placeholder="请输入用户名" size="large">
                <template #prepend>
                  <el-icon>
                    <User />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <!-- 密码输入框 -->
            <el-form-item prop="password">
              <el-input v-model="param.password" :type="showPassword ? 'text' : 'password'" placeholder="请输入密码"
                size="large" @keyup.enter="submitForm(loginFormRef)">
                <template #prepend>
                  <el-icon>
                    <Lock />
                  </el-icon>
                </template>
                <template #suffix>
                  <el-icon style="cursor: pointer;" @click="togglePassword">
                    <View v-if="showPassword" />
                    <Hide v-else />
                  </el-icon>
                </template>
              </el-input>
            </el-form-item>

            <!-- 验证码输入框 -->
            <el-form-item v-if="captchaEnabled" prop="captcha" ref="captchaInputElFormItemRef">
              <CaptchaInput ref="captchaInputRef" v-model="param.captcha" @enter="submitForm(loginFormRef)"
                @clear-validate="clearCaptchaInputValidate" />
            </el-form-item>

            <!-- 记住我和忘记密码 -->
            <div class="form-options">
              <!-- <el-checkbox v-model="rememberMe" size="small">
                记住我
              </el-checkbox>
              <a href="#" class="forgot-link">忘记密码？</a> -->
            </div>

            <!-- 登录按钮 -->
            <el-form-item>
              <el-button type="primary" size="large" class="login-btn-full" :loading="loading"
                @click="submitForm(loginFormRef)">
                <span v-if="!loading">登 录</span>
                <span v-else>登录中...</span>
              </el-button>
            </el-form-item>

          </el-form>

          <!-- 页脚信息 -->
          <div class="login-footer">
            <p>
              <!-- {{ `© ${new Date().getFullYear()} ${settings.companyName}. All rights reserved.` }} -->
            </p>
          </div>
        </div>

        <!-- 右侧图片区域 -->
        <div class="login-image">
          <div class="image-content">
            <h2>智能管理系统</h2>
            <p>高效协同 · 数据洞察 · 智能决策</p>
            <div class="feature-list">
              <div class="feature-item">
                <span class="feature-icon">📈</span>
                <span>数据驾驶舱</span>
              </div>
              <div class="feature-item">
                <span class="feature-icon">🔐</span>
                <span>权限管理</span>
              </div>
              <div class="feature-item">
                <span class="feature-icon">⚙️</span>
                <span>工作流引擎</span>
              </div>
              <div class="feature-item">
                <span class="feature-icon">🤖</span>
                <span>智能分析</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, useTemplateRef } from 'vue'
import { Hide, Lock, User, View } from '@element-plus/icons-vue'
import { useLoginLogic } from './composables/useLoginLogic'
import CaptchaInput from './components/CaptchaInput.vue'

const baseUrl = import.meta.env.BASE_URL
const siteFullTitle = import.meta.env.VITE_APP_FULL_TITLE

const captchaInputRef = useTemplateRef('captchaInputRef')
const captchaInputElFormItemRef = useTemplateRef('captchaInputElFormItemRef')
const loginFormRef = useTemplateRef('loginFormRef')

// 使用抽离的登录逻辑
const { param, rules, submitForm, loading, captchaEnabled } = useLoginLogic({ onLoginFailedCallback })

// 本地状态
const showPassword = ref(false)
// const rememberMe = ref(false)

/**
 * 切换密码显示/隐藏
 */
const togglePassword = () => {
  showPassword.value = !showPassword.value
}

const clearCaptchaInputValidate = () => {
  captchaInputElFormItemRef.value?.clearValidate()
}

function onLoginFailedCallback() {
  // 刷新验证码
  captchaInputRef.value?.refreshCaptcha()
}
</script>

<style scoped>
.login-wrap {
  width: 100%;
  height: 100vh;
  /* background: #d3dbf5; */
  /* background: linear-gradient(-135deg, #eedefd 0%, #d3dbf5 70%); */
  background: linear-gradient(-135deg, #d8b9f4 0%, #a7b9f3 70%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.login-container {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-wrapper {
  display: flex;
  width: 100%;
  max-width: 1000px;
  min-height: 600px;
  background: white;
  border-radius: 20px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  margin: 2rem;
}

/* 左侧登录表单区域 */
.login-form {
  flex: 1.2;
  padding: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  background: white;
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.logo {
  margin-bottom: 1rem;
}

.logo img {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  user-select: none;
}

.login-header .title {
  font-size: 1.6rem;
  color: #333;
  margin: 0.5rem 0;
  font-weight: 600;

  /* 超过指定行数显示省略号 */
  --max-line: 2;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: var(--max-line);
  line-clamp: var(--max-line);
  overflow: hidden;
  text-overflow: ellipsis;
  /* 加一点 padding-right 避免省略号被遮挡 */
  padding-right: 1px;
  /* padding-left 也同步加一下保证标题居中 */
  padding-left: 1px;
}

.login-header p {
  color: #666;
  margin: 0;
  font-size: 1rem;
}

.form-content {
  max-width: 320px;
  margin: 0 auto;
  width: 100%;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  font-size: 0.9rem;
}

.forgot-link {
  color: #667eea;
  text-decoration: none;
}

.forgot-link:hover {
  text-decoration: underline;
}

.login-btn-full {
  width: 100%;
  height: 44px;
  font-size: 1rem;
  font-weight: 600;
}

.login-footer {
  text-align: center;
  margin-top: 2rem;
  color: #999;
  font-size: 0.8rem;
}

/* 右侧图片区域 */
.login-image {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 2rem;
  color: white;
}

.image-content {
  text-align: center;
  max-width: 300px;
}

.image-content h2 {
  font-size: 2rem;
  margin-bottom: 1rem;
  font-weight: 600;
}

.image-content p {
  font-size: 1.2rem;
  margin-bottom: 2rem;
  opacity: 0.9;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  font-size: 1rem;
  opacity: 0.9;
}

.feature-icon {
  font-size: 1.5rem;
}

.company-info {
  color: #7589b6;
  text-align: center;
  position: absolute;
  left: 0;
  right: 0;
  bottom: 10px;
  font-size: 13px;
  letter-spacing: 1px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
    margin: 1rem;
    min-height: auto;
  }

  .login-form {
    padding: 2rem 1.5rem;
  }

  .login-image {
    display: none;
  }

  .form-content {
    max-width: 100%;
  }
}

@media (max-width: 480px) {
  .login-wrapper {
    margin: 0;
    border-radius: 0;
    box-shadow: none;
  }
}

/* 深色模式样式 */
html.dark .login-wrap {
  background: linear-gradient(-135deg, #2d1b47 0%, #1a1c3a 70%);
}

html.dark .login-wrapper {
  background: #1a1a2e;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3);
}

html.dark .login-form {
  background: #1a1a2e;
}

html.dark .login-header .title {
  color: #e0e0e0;
}

html.dark .login-header p {
  color: #b0b0b0;
}

html.dark .logo {
  filter: invert(100%);
}

html.dark .login-footer {
  color: #888;
}

html.dark .login-image {
  background: linear-gradient(135deg, #4a5568 0%, #2d3748 100%);
}

html.dark .company-info {
  color: #9ca3af;
}
</style>
