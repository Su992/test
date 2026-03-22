<template>
  <div class="front-layout">
    <div class="header glass-effect">
      <div class="logo">
        <el-icon :size="32" color="#10B981">
          <Lock />
        </el-icon>
        <span class="title">阳光防线</span>
      </div>

      <div class="nav">
        <el-menu :default-active="$route.path" class="custom-menu" mode="horizontal" router>
          <el-menu-item index="/home">首页探索</el-menu-item>
          <el-menu-item index="/knowledge">毒品科普</el-menu-item>
          <el-menu-item index="/test">在线测试</el-menu-item>
          <el-menu-item index="/ai-assistant">AI 小助手</el-menu-item>
          <el-menu-item index="/submission">留言投稿</el-menu-item>
        </el-menu>
      </div>

      <div class="user-action">
        <el-dropdown v-if="userStore.userInfo" @command="handleCommand" trigger="click">
          <div class="user-profile">
            <el-avatar :size="36"
              :src="userStore.userInfo.avatar ? userStore.userInfo.avatar : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
            <span class="nickname">{{ userStore.userInfo.nickname || userStore.userInfo.username }}</span>
            <el-icon>
              <CaretBottom />
            </el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-if="userStore.userInfo.role === 0" command="goAdmin">
                <el-icon>
                  <Monitor />
                </el-icon>管理后台
              </el-dropdown-item>

              <el-dropdown-item command="profile">
                <el-icon>
                  <User />
                </el-icon>个人中心
              </el-dropdown-item>

              <el-dropdown-item command="logout" divided class="text-danger">
                <el-icon>
                  <SwitchButton />
                </el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>

        <el-button v-else type="primary" class="gradient-btn" round @click="showAuthDialog = true">
          开启探索 / 登录
        </el-button>
      </div>
    </div>

    <div class="main-content">
      <router-view />
    </div>

    <el-dialog v-model="showAuthDialog" :width="400" destroy-on-close :show-close="false" class="premium-dialog">
      <template #header>
        <div class="dialog-header">
          <h3>{{ isLoginMode ? '欢迎回来' : '加入防线' }}</h3>
          <p class="subtitle">{{ isLoginMode ? '登录以体验完整智能服务' : '注册账号，共建无毒社会' }}</p>
        </div>
      </template>

      <el-form :model="authForm" :rules="authRules" ref="authFormRef" size="large">
        <el-form-item prop="username">
          <el-input v-model="authForm.username" placeholder="请输入用户名" :prefix-icon="UserIcon" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="authForm.password" type="password" placeholder="请输入密码" show-password :prefix-icon="Lock" />
        </el-form-item>
        <el-form-item prop="nickname" v-if="!isLoginMode">
          <el-input v-model="authForm.nickname" placeholder="请输入昵称 (选填)" :prefix-icon="Edit" />
        </el-form-item>

        <el-button type="primary" class="full-width-btn gradient-btn" @click="submitAuth" :loading="loading">
          {{ isLoginMode ? '登 录' : '注 册' }}
        </el-button>
      </el-form>

      <div class="dialog-footer-text">
        {{ isLoginMode ? '还没有账号？' : '已有账号？' }}
        <span class="toggle-link" @click="toggleMode">{{ isLoginMode ? '立即注册' : '返回登录' }}</span>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ref, reactive } from 'vue'
import { User as UserIcon, Lock, Edit, CaretBottom, Monitor, SwitchButton, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store/user'
import { loginAPI, registerAPI } from '../../api/index'

const router = useRouter()
const userStore = useUserStore()

const showAuthDialog = ref(false)
const isLoginMode = ref(true)
const loading = ref(false)
const authFormRef = ref(null)

const authForm = reactive({
  username: '',
  password: '',
  nickname: ''
})

const authRules = {
  username: [{ required: true, message: '用户名不能为空', trigger: 'blur' }],
  password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
}

const toggleMode = () => {
  isLoginMode.value = !isLoginMode.value
  authFormRef.value?.resetFields()
}

// 提交表单：增加了角色重定向逻辑
const submitAuth = async () => {
  if (!authFormRef.value) return
  await authFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        if (isLoginMode.value) {
          const res = await loginAPI(authForm)
          userStore.setUserInfo(res)

          // ✨ 角色分流跳转
          if (res.role === 0) {
            ElMessage.success('管理员欢迎回来！正在进入系统后台...')
            router.push('/admin/dashboard')
          } else {
            ElMessage.success('登录成功，欢迎回来！')
            showAuthDialog.value = false
          }
        } else {
          await registerAPI(authForm)
          ElMessage.success('注册成功，请登录')
          isLoginMode.value = true
        }
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}

const handleCommand = (command) => {
  if (command === 'logout') {
    userStore.logout()
    ElMessage.success('已安全退出')
    router.push('/home')
  } else if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'goAdmin') {
    // ✨ 管理员一键传送至后台
    router.push('/admin/dashboard')
  }
}
</script>

<style scoped>
/* 样式部分保持你之前的玻璃拟态设计，无需改动 */
.glass-effect {
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.05);
}

.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
  padding: 0 50px;
  position: sticky;
  top: 0;
  z-index: 1000;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo .title {
  font-size: 22px;
  font-weight: 800;
  background: linear-gradient(135deg, #10B981, #3B82F6);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav {
  flex: 1;
  margin: 0 50px;
}

.custom-menu {
  background: transparent !important;
  border-bottom: none !important;
}

:deep(.el-menu-item) {
  font-size: 16px;
  color: #4B5563 !important;
  border-bottom: 2px solid transparent !important;
}

:deep(.el-menu-item.is-active) {
  color: #10B981 !important;
  border-bottom: 2px solid #10B981 !important;
}

.gradient-btn {
  background: linear-gradient(135deg, #10B981 0%, #3B82F6 100%);
  border: none;
  font-weight: bold;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 5px 15px;
  border-radius: 30px;
}

.nickname {
  font-weight: 600;
  color: #374151;
}

.dialog-header {
  text-align: center;
}

.full-width-btn {
  width: 100%;
  margin-top: 10px;
}

.dialog-footer-text {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.toggle-link {
  color: #3B82F6;
  cursor: pointer;
  font-weight: 600;
}

.main-content {
  width: 1200px;
  margin: 30px auto;
  min-height: calc(100vh - 130px);
}
</style>