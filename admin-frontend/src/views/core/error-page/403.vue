<template>
  <div class="error-page">
    <div class="error-code">403</div>
    <div class="error-desc">你无权访问当前页面</div>
    <el-space class="error-handle" :size="36">
      <el-button type="primary" size="large" :icon="Back" @click="goBack">
        返回上一页
      </el-button>
      <router-link to="/">
        <el-button type="primary" size="large" :icon="HomeFilled">
          返回首页
        </el-button>
      </router-link>
      <el-button type="danger" size="large" :icon="MaterialSymbolsLogoutRounded" @click="handleLogout">
        退出登录
      </el-button>
    </el-space>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Back, HomeFilled } from '@element-plus/icons-vue'
import { userLogout } from '@/utils/user_utils'
import MaterialSymbolsLogoutRounded from '@/assets/icons/MaterialSymbolsLogoutRounded.vue'

defineOptions({
  // eslint-disable-next-line vue/multi-word-component-names
  name: '403',
})

const router = useRouter()
const goBack = () => {
  router.go(-1)
}

const handleLogout = () => {
  userLogout(router, true)
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.error-page {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
  background: #f3f3f3;
  box-sizing: border-box;
}

.error-code {
  line-height: 1;
  font-size: 250px;
  font-weight: bolder;
  color: #f34d4d;
}

.error-desc {
  font-size: 30px;
  color: #777;
}

.error-handle {
  margin-top: 30px;
  padding-bottom: 200px;
}
</style>
