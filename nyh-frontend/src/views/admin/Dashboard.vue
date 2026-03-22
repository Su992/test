<template>
    <div class="dashboard-container">
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="data-card user-card">
                    <div class="card-icon"><el-icon>
                            <UserFilled />
                        </el-icon></div>
                    <div class="card-info">
                        <div class="title">总用户数</div>
                        <div class="value">{{ statData.totalUsers || 0 }}</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="data-card submission-card">
                    <div class="card-icon"><el-icon>
                            <Comment />
                        </el-icon></div>
                    <div class="card-info">
                        <div class="title">留言投稿总数</div>
                        <div class="value">{{ statData.totalSubmissions || 0 }}</div>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="hover" class="data-card question-card">
                    <div class="card-icon"><el-icon>
                            <Reading />
                        </el-icon></div>
                    <div class="card-info">
                        <div class="title">在线题库总数</div>
                        <div class="value">{{ statData.totalQuestions || 0 }}</div>
                    </div>
                </el-card>
            </el-col>
        </el-row>

        <el-card shadow="never" class="welcome-card mt-4">
            <template #header>
                <div class="card-header">
                    <span>快速操作与系统状态</span>
                </div>
            </template>
            <div class="welcome-content">
                <el-empty description="数据可视化图表开发中..." :image-size="120" />
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { UserFilled, Comment, Reading } from '@element-plus/icons-vue'
import { getAdminDashboardAPI } from '../../api/index'

const statData = ref({})

onMounted(async () => {
    try {
        const res = await getAdminDashboardAPI()
        statData.value = res || {}
    } catch (error) {
        console.error('获取统计数据失败', error)
    }
})
</script>

<style scoped>
.data-card {
    display: flex;
    align-items: center;
    border-radius: 8px;
    border: none;
}

.data-card :deep(.el-card__body) {
    display: flex;
    align-items: center;
    width: 100%;
    padding: 20px;
}

.card-icon {
    width: 60px;
    height: 60px;
    border-radius: 8px;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 30px;
    color: #fff;
    margin-right: 20px;
}

.user-card .card-icon {
    background: linear-gradient(135deg, #409EFF, #64b5f6);
}

.submission-card .card-icon {
    background: linear-gradient(135deg, #67C23A, #81c784);
}

.question-card .card-icon {
    background: linear-gradient(135deg, #E6A23C, #ffb74d);
}

.card-info .title {
    font-size: 14px;
    color: #909399;
    margin-bottom: 5px;
}

.card-info .value {
    font-size: 28px;
    font-weight: bold;
    color: #303133;
}

.mt-4 {
    margin-top: 20px;
}

.welcome-card {
    border-radius: 8px;
}

.card-header {
    font-weight: bold;
}
</style>