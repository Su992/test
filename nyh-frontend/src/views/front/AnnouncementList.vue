<template>
    <div class="cyber-container">
        <div class="cyber-header-box">
            <h1 class="cyber-title">SYSTEM_BROADCAST_<span>公告中心</span></h1>
            <p class="cyber-subtitle">实时同步平台最新动态与核心指令</p>
        </div>

        <div class="cyber-content" v-loading="loading">
            <el-empty v-if="announcements.length === 0" description="暂无信号接入..." :image-size="100" />

            <div class="cyber-timeline" v-else>
                <div v-for="(item, index) in announcements" :key="item.id" class="cyber-card">
                    <div class="card-glow-line"></div>
                    <div class="card-header">
                        <span class="index-badge">0{{ index + 1 }}</span>
                        <span class="date"><el-icon>
                                <Clock />
                            </el-icon> {{ formatDate(item.createTime) }}</span>
                    </div>
                    <h3 class="title">{{ item.title }}</h3>
                    <p class="content">{{ item.content }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Clock } from '@element-plus/icons-vue'
import { getAnnouncementsAPI } from '../../api/index'

const announcements = ref([])
const loading = ref(false)

onMounted(async () => {
    loading.value = true
    try {
        // 获取已发布的公告（假设后台接口正常工作）
        const res = await getAnnouncementsAPI(1, 20)
        announcements.value = res.records || []
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false
    }
})

const formatDate = (dateStr) => {
    if (!dateStr) return ''
    return dateStr.substring(0, 10)
}
</script>

<style scoped>
/* 科技感深色背景 */
.cyber-container {
    max-width: 900px;
    margin: 0 auto;
    padding: 40px 20px;
    background: #0a0f16;
    /* 深邃黑蓝 */
    border-radius: 12px;
    box-shadow: 0 0 40px rgba(0, 255, 255, 0.05);
    min-height: 80vh;
}

.cyber-header-box {
    text-align: center;
    margin-bottom: 50px;
    border-bottom: 1px solid rgba(0, 255, 255, 0.2);
    padding-bottom: 20px;
}

.cyber-title {
    color: #fff;
    font-family: 'Courier New', Courier, monospace;
    font-size: 28px;
    letter-spacing: 2px;
    text-shadow: 0 0 10px rgba(0, 255, 255, 0.5);
}

.cyber-title span {
    color: #00ffff;
}

.cyber-subtitle {
    color: #64748b;
    font-size: 14px;
    margin-top: 10px;
    text-transform: uppercase;
    letter-spacing: 1px;
}

.cyber-timeline {
    display: flex;
    flex-direction: column;
    gap: 25px;
}

/* 赛博卡片设计 */
.cyber-card {
    position: relative;
    background: rgba(16, 24, 39, 0.8);
    border: 1px solid #1e293b;
    padding: 25px 30px;
    border-radius: 4px;
    transition: all 0.3s ease;
    overflow: hidden;
}

.cyber-card:hover {
    border-color: #00ffff;
    box-shadow: 0 0 20px rgba(0, 255, 255, 0.15);
    transform: translateX(5px);
}

/* 侧边发光扫描线 */
.card-glow-line {
    position: absolute;
    left: 0;
    top: 0;
    bottom: 0;
    width: 3px;
    background: #00ffff;
    box-shadow: 0 0 10px #00ffff;
    opacity: 0.5;
}

.cyber-card:hover .card-glow-line {
    opacity: 1;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

.index-badge {
    background: rgba(0, 255, 255, 0.1);
    color: #00ffff;
    padding: 2px 8px;
    font-size: 12px;
    font-family: monospace;
    border: 1px solid rgba(0, 255, 255, 0.3);
}

.date {
    color: #475569;
    font-size: 13px;
    display: flex;
    align-items: center;
    gap: 5px;
}

.title {
    color: #e2e8f0;
    font-size: 18px;
    margin: 0 0 15px 0;
    font-weight: 600;
}

.content {
    color: #94a3b8;
    font-size: 14px;
    line-height: 1.8;
    margin: 0;
    white-space: pre-wrap;
}

/* 覆盖加载动画颜色 */
:deep(.el-loading-mask) {
    background-color: rgba(10, 15, 22, 0.8) !important;
}

:deep(.el-loading-spinner .path) {
    stroke: #00ffff;
}
</style>