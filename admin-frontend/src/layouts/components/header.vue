<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChange">
      <el-icon v-if="sidebarStore.collapse">
        <Expand />
      </el-icon>
      <el-icon v-else>
        <Fold />
      </el-icon>
    </div>

    <div class="logo" @click="router.push('/')" style="cursor: pointer;">
      <el-tooltip effect="dark" content="回到首页">
        <span>{{ settings.siteFullTitle }}</span>
      </el-tooltip>
    </div>

    <div class="header-center"></div>

    <div class="header-right">
      <div class="header-user-con">
        <!-- v-permission="'navigation'" -->
        <div class="btn-icon hide-on-narrow-screen" @click="router.push('/navigation')">
          <el-tooltip effect="dark" content="系统导航" placement="bottom">
            <el-icon>
              <Guide />
            </el-icon>
          </el-tooltip>
        </div>
        <!-- 消息中心 -->
        <div class="btn-icon hide-on-narrow-screen" @click="router.push('/tabs')">
          <el-tooltip effect="dark" :content="message ? `有${message}条未读消息` : `消息中心`" placement="bottom">
            <el-icon>
              <Message />
            </el-icon>
          </el-tooltip>
          <span class="btn-bell-badge" v-if="message"></span>
        </div>
        <div class="btn-icon hide-on-narrow-screen" @click="setFullScreen">
          <el-tooltip effect="dark" content="全屏" placement="bottom">
            <el-icon>
              <FullScreen />
            </el-icon>
          </el-tooltip>
        </div>
        <!-- 切换身份 -->
        <div class="btn-icon hide-on-narrow-screen" @click="handleSwitchIdentity">
          <el-tooltip effect="dark" content="切换身份" placement="bottom">
            <el-icon>
              <Switch />
            </el-icon>
          </el-tooltip>
        </div>
        <div class="btn-icon hide-on-narrow-screen" @click="() => themeSettingDrawerVisible = true">
          <el-tooltip effect="dark" content="自定义主题" placement="bottom">
            <el-icon>
              <Brush />
            </el-icon>
          </el-tooltip>
        </div>

        <!-- 用户头像 -->
        <el-avatar class="user-avatar" :size="30" :src="defaultAvatarUrl"
          :style="themeStore.presentationMode ? { filter: 'blur(8px)' } : undefined" />
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link" :style="themeStore.presentationMode ? { filter: 'blur(3px)' } : undefined">
            {{ userStore.user?.nickname }}
            <el-icon class="el-icon--right">
              <ArrowDown />
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :icon="User" command="user">个人中心</el-dropdown-item>
              <el-dropdown-item divided :icon="Switch" command="switch-identity">切换身份</el-dropdown-item>
              <el-dropdown-item divided :icon="Right" command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>

    <!-- 自定义主题 drawer -->
    <!-- <ThemeSettingDrawer v-model:visible="themeSettingDrawerVisible" /> -->

  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowDown, Brush, Expand, Fold, FullScreen, Guide, Message, Right, Switch, User } from '@element-plus/icons-vue'
import settings from '@/utils/settings'
import { userLogout } from '@/utils/user_utils'
import defaultAvatarUrl from '@/assets/img/default-avatar.jpg'
import { redirectToChooseIdentity } from '@/router/guards/scripts/redirect_to'
import { useSidebarStore } from '@/stores/sidebar'
import { useThemeStore } from '@/stores/theme'
import { useUserStore } from '@/stores/user'
// import ThemeSettingDrawer from './theme-setting-drawer.vue'

const message: number = 2

const sidebarStore = useSidebarStore()
const themeStore = useThemeStore()
const userStore = useUserStore()

// 侧边栏折叠
const collapseChange = () => {
  sidebarStore.handleCollapse()
}

onMounted(() => {
  // 网页打开时不折叠
  // if (document.body.clientWidth < 1500) {
  // 	collapseChange()
  // }
})

// 用户名下拉菜单选择事件
const router = useRouter()
const handleCommand = (command: string) => {
  if (command == 'logout') {
    userLogout(router, true)
    ElMessage.success('已退出登录')
  } else if (command == 'switch-identity') {
    handleSwitchIdentity()
  } else if (command == 'user') {
    router.push('/user')
  }
}

const setFullScreen = () => {
  if (document.fullscreenElement) {
    document.exitFullscreen()
  } else {
    document.body.requestFullscreen.call(document.body)
  }
}

const handleSwitchIdentity = () => {
  redirectToChooseIdentity(router, true)
}

const themeSettingDrawerVisible = ref<boolean>(false)
</script>

<style scoped>
.header {
  display: flex;
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: var(--header-height);
  color: #fff;
  background-color: #242f42;
  background-color: #1b2639;
  overflow-y: hidden;
  overflow-x: auto;
}

.collapse-btn {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  float: left;
  padding: 0 21px;
  cursor: pointer;
  font-size: 22px;
}

.collapse-btn:hover {
  background: rgb(40, 52, 70);
}

.header .logo {
  float: left;
  display: grid;
  place-items: center;
  /* 系统名称不换行 */
  white-space: nowrap;
  font-size: 20px;
}

.header-center {
  flex-grow: 1;
  display: grid;
  place-items: center;
  /* 最小宽度动态计算 */
  /* min-width: 360px; */
  /* 菜单名称不换行 */
  white-space: nowrap;
}

.header-center-group-item {
  user-select: none;
  transition: all 0.15s;
}

/* .header-center-group-item.active {} */

.header-center-group-item.inactive {
  color: white;
}

.header-center-group-item.inactive:hover {
  color: rgb(159.5, 206.5, 255);
}

.header-right {
  float: right;
  padding-right: 50px;

  display: grid;
  place-items: center;
}

.header-user-con {
  display: flex;
  /* align-items: center; */
}

.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}

.btn-icon {
  position: relative;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 15px;
  cursor: pointer;
  display: flex;
  align-items: center;
  margin: 0 3px;
  font-size: 20px;
}

.btn-icon .btn-bell-badge {
  position: absolute;
  right: 4px;
  top: 0px;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
}

.user-name {
  margin-left: 10px;
}

.user-avatar {
  margin-left: 20px;
}

.el-dropdown-link {
  color: #fff;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.el-dropdown-menu__item {
  text-align: center;
}

@media screen and (max-width: 1000px) {
  .header .logo {
    font-size: 16px;
  }

  .hide-on-narrow-screen {
    display: none;
  }
}
</style>
