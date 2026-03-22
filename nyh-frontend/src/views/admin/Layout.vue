<template>
    <el-container class="admin-layout">
        <el-aside :width="isCollapse ? '64px' : '220px'" class="aside-menu">
            <div class="logo-box">
                <el-icon :size="24" color="#fff">
                    <Monitor />
                </el-icon>
                <span class="logo-text" v-show="!isCollapse">防线后台管理</span>
            </div>

            <el-menu active-text-color="#409EFF" background-color="#304156" class="el-menu-vertical"
                :default-active="$route.path" text-color="#bfcbd9" :collapse="isCollapse" router>
                <el-menu-item index="/admin/dashboard">
                    <el-icon>
                        <DataBoard />
                    </el-icon>
                    <template #title>数据看板</template>
                </el-menu-item>

                <el-menu-item index="/admin/user">
                    <el-icon>
                        <User />
                    </el-icon>
                    <template #title>用户管理</template>
                </el-menu-item>

                <el-sub-menu index="content">
                    <template #title>
                        <el-icon>
                            <Document />
                        </el-icon>
                        <span>内容管理</span>
                    </template>
                    <el-menu-item index="/admin/announcement">平台公告</el-menu-item>
                    <el-menu-item index="/admin/news">推荐消息</el-menu-item>
                    <el-menu-item index="/admin/knowledge">毒品科普</el-menu-item>
                </el-sub-menu>

                <el-menu-item index="/admin/submission">
                    <el-icon>
                        <ChatDotSquare />
                    </el-icon>
                    <template #title>留言审核</template>
                </el-menu-item>

                <el-menu-item index="/admin/question">
                    <el-icon>
                        <Tickets />
                    </el-icon>
                    <template #title>题库管理</template>
                </el-menu-item>
            </el-menu>
        </el-aside>

        <el-container>
            <el-header class="admin-header">
                <div class="header-left">
                    <el-icon class="collapse-btn" @click="isCollapse = !isCollapse">
                        <Fold v-if="!isCollapse" />
                        <Expand v-else />
                    </el-icon>
                    <span class="breadcrumb">欢迎来到阳光防线后台管理系统</span>
                </div>

                <div class="header-right">
                    <el-button link @click="$router.push('/home')" class="mr-3">返回前台首页</el-button>
                    <el-dropdown @command="handleCommand">
                        <span class="user-info">
                            <el-avatar :size="32"
                                :src="userStore.userInfo?.avatar || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" />
                            <span class="nickname">{{ userStore.userInfo?.nickname || '管理员' }}</span>
                            <el-icon>
                                <CaretBottom />
                            </el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item command="logout" class="text-danger">退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </el-header>

            <el-main class="admin-main">
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/user'
import { Monitor, Fold, Expand, DataBoard, User, Document, ChatDotSquare, Tickets, CaretBottom } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

const isCollapse = ref(false)
const router = useRouter()
const userStore = useUserStore()

const handleCommand = (command) => {
    if (command === 'logout') {
        userStore.logout()
        ElMessage.success('已退出后台')
        router.push('/home')
    }
}
</script>

<style scoped>
.admin-layout {
    height: 100vh;
}

.aside-menu {
    background-color: #304156;
    transition: width 0.3s;
    overflow-x: hidden;
}

.logo-box {
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #fff;
    background-color: #2b3649;
}

.logo-text {
    margin-left: 10px;
    font-weight: bold;
    font-size: 16px;
    white-space: nowrap;
}

.el-menu-vertical {
    border-right: none;
}

.admin-header {
    height: 60px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #fff;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
    padding: 0 20px;
}

.header-left {
    display: flex;
    align-items: center;
    gap: 15px;
}

.collapse-btn {
    font-size: 20px;
    cursor: pointer;
    color: #606266;
}

.collapse-btn:hover {
    color: #409EFF;
}

.breadcrumb {
    color: #606266;
    font-size: 14px;
}

.header-right {
    display: flex;
    align-items: center;
}

.user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    color: #606266;
    outline: none;
}

.mr-3 {
    margin-right: 15px;
}

.admin-main {
    background-color: #f0f2f5;
    padding: 20px;
}
</style>