/* eslint-disable import/order */
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import 'element-plus/dist/index.css'
import 'dayjs/locale/zh-cn' // 添加这行以设置 element-plus 日期选择器的 周一 为一周的开始

import './assets/css/global-vars.css'
import './assets/css/main.css'
import './assets/css/global-style.css'

import App from './App.vue'
import router from './router'

import { usePermissionStore } from './stores/permission'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)

app.use(pinia)
app.use(router)

// 自定义权限指令
const permission = usePermissionStore()
permission.registerDirective(app)

app.mount('#app')

console.log('BUILD_TIME', new Date(__APP_BUILD_TIME__).toLocaleString())
