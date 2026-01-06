<template>
  <div class="choose-identity-wrap">
    <div class="choose-identity-container">
      <div class="header">
        <div class="header-top">
          <el-button v-if="showBackBtn" class="back-btn" @click="handleBack">
            <el-icon>
              <ArrowLeft />
            </el-icon>
            返回
          </el-button>
          <div v-else></div>
          <el-button class="logout-btn" type="danger" @click="handleLogout" :icon="SwitchButton">
            <template v-if="identityList.length === 0">
              退出
            </template>
            <template v-else>
              {{ currentIdentity ? '退出登录' : '取消登录' }}
            </template>
          </el-button>
        </div>
        <h1>
          {{ currentIdentity ? '切换登录身份' : '选择登录身份' }}
        </h1>
        <p>
          {{ currentIdentity ? '您当前拥有以下身份，请选择要切换的身份' : '您当前拥有以下身份，请选择要登录的身份' }}
        </p>
        <el-text style="opacity: 0.5;" v-if="userStore.isLogin && userStore.user">
          当前登录用户：<b>{{ userStore.user.nickname }}</b>({{ userStore.user.username }})
        </el-text>
      </div>

      <el-scrollbar max-height="61.8vh" class="identity-list" v-loading="loading" element-loading-background="#0000001a" element-loading-text="请稍候" wrap-style="padding: 1.2rem;">
        <el-card v-for="identity in identityList" :key="identity.id" class="identity-card"
          :class="{ 'is-selected': selectedId === identity.id, 'is-current': currentIdentity?.id === identity.id }"
          @click="handleSelectIdentity(identity)">
          <div class="card-content">
            <el-badge class="item" :offset="[-12, 0]" :hidden="currentIdentity?.id !== identity.id" :value="'当前'">
              <div class="identity-icon">
                <el-icon :size="40">
                  <User />
                </el-icon>
              </div>
            </el-badge>
            <div class="identity-info">
              <h3>{{ identity.name }}</h3>
              <p>{{ identity.intro }}</p>
              <p>
                <el-text style="font-size: 0.8em; opacity: 0.618;">
                  身份 ID: {{ identity.id }}，部门 ID: {{ identity.departmentId }}
                </el-text>
              </p>
            </div>
            <div class="select-icon" v-if="selectedId === identity.id">
              <el-icon :size="24" color="#FFFFFF">
                <Check />
              </el-icon>
            </div>
          </div>
        </el-card>
      </el-scrollbar>

      <div class="footer" v-if="identityList.length === 0">
        <el-empty description="暂无可用身份，请联系系统管理员为您分配身份后再尝试登录"
          style="--el-text-color-secondary: var(--el-text-color-primary);" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Check, SwitchButton, User } from '@element-plus/icons-vue'
import { userLogout } from '@/utils/user_utils'
import { useUserStore } from '@/stores/user'
import { useChooseIdentityLogic } from './composables/useChooseIdentityLogic'

const router = useRouter()

const userStore = useUserStore()

const {
  selectedId,
  currentIdentity,
  identityList,
  handleSelectIdentity,
  loading,
  showBackBtn,
  handleBack,
} = useChooseIdentityLogic()

const handleLogout = async () => {
  // 切换身份页点击退出登录，不保留 redirectTo 参数
  await userLogout(router, false)
  ElMessage.success('已退出登录')
}
</script>

<style scoped>
.choose-identity-wrap {
  width: 100%;
  height: 100vh;
  background: linear-gradient(-135deg, #d8b9f4 0%, #a7b9f3 70%);
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
}

.choose-identity-container {
  width: 100%;
  max-width: 600px;
  padding: 2rem;
}

.header-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1rem;
}

.back-btn {
  align-self: flex-start;
}

.logout-btn {
  align-self: flex-end;
}

.header {
  text-align: center;
  margin-bottom: 2rem;
  color: #333;
  user-select: none;
}

.header h1 {
  font-size: 2rem;
  margin-bottom: 0.5rem;
  font-weight: 600;
}

.header p {
  color: #666;
  font-size: 1rem;
}

.identity-list {}

.identity-card+.identity-card {
  margin-top: 1.2rem;
}

.identity-card {
  cursor: pointer;
  transition: all 0.3s ease;
  border: 2px solid transparent;
}

.identity-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
}

.identity-card.is-selected {
  /* border-color: #0f50c0; */
  border-color: #67c23a;
  background: #f0f9eb;
}

.card-content {
  display: flex;
  align-items: center;
  gap: 1.55rem;
  padding: 0.5rem 0.618rem;
}

.identity-icon {
  --identity-icon-width: 72px;
  width: var(--identity-icon-width);
  height: var(--identity-icon-width);
  border-radius: 12px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.identity-info {
  flex: 1;
}

.identity-info h3 {
  /* margin: 0 0 0.5rem 0; */
  margin: 0 0 0.2rem 0;
  font-size: 1.1rem;
  color: #333;
}

.identity-info p {
  /* margin: 0; */
  margin: 0.05rem 0;
  color: #666;
  font-size: 0.9rem;
}

.select-icon {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #67c23a;
  display: flex;
  align-items: center;
  justify-content: center;
}

.footer {
  margin-top: 2rem;
  text-align: center;
}

@media (max-width: 480px) {
  .choose-identity-container {
    padding: 1rem;
  }

  .header h1 {
    font-size: 1.5rem;
  }

  .card-content {
    flex-direction: column;
    text-align: center;
  }
}
</style>
