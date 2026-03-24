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

        <el-card shadow="never" class="chart-card mt-4">
            <template #header>
                <div class="card-header">
                    <span>平台核心数据可视化概览</span>
                </div>
            </template>
            <div class="chart-content" ref="chartRef" style="width: 100%; height: 400px;"></div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { UserFilled, Comment, Reading } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getAdminDashboardAPI } from '../../api/index'

const statData = ref({})
const chartRef = ref(null)

onMounted(async () => {
    try {
        const res = await getAdminDashboardAPI()
        statData.value = res || {}

        // 等待 DOM 渲染完成后初始化图表
        await nextTick()
        initChart()
    } catch (error) {
        console.error('获取统计数据失败', error)
    }
})

const initChart = () => {
    if (!chartRef.value) return
    const myChart = echarts.init(chartRef.value)

    const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: [
            {
                type: 'category',
                data: ['平台注册用户', '留言投稿数量', '在线测试题库'],
                axisTick: { alignWithLabel: true }
            }
        ],
        yAxis: [{ type: 'value' }],
        series: [
            {
                name: '数量',
                type: 'bar',
                barWidth: '40%',
                data: [
                    { value: statData.value.totalUsers || 0, itemStyle: { color: '#409EFF' } },
                    { value: statData.value.totalSubmissions || 0, itemStyle: { color: '#67C23A' } },
                    { value: statData.value.totalQuestions || 0, itemStyle: { color: '#E6A23C' } }
                ],
                label: { show: true, position: 'top' }
            }
        ]
    }
    myChart.setOption(option)

    // 监听窗口缩放，自动重绘图表
    window.addEventListener('resize', () => {
        myChart.resize()
    })
}
</script>

<style scoped>
/* 原有的 data-card 样式保持不变 */
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

.chart-card {
    border-radius: 8px;
}

.card-header {
    font-weight: bold;
}
</style>