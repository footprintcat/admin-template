import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import 'element-plus/dist/index.css'
import 'dayjs/locale/zh-cn' // 添加这行以设置 element-plus 日期选择器的 周一 为一周的开始

import App from './App.vue'
import router from './router'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)

app.use(pinia)
app.use(router)

app.mount('#app')

console.log('BUILD_TIME', new Date(__APP_BUILD_TIME__).toLocaleString())
