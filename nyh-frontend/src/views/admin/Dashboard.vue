<template>
    <div class="dashboard-container">
        <el-row :gutter="20">
            <el-col :span="8">
                <el-card shadow="hover" class="data-card user-card">
                    <div class="card-icon"><el-icon>
                            <UserFilled />
                        </el-icon></div>
                    <div class="card-info">
                        <div class="title">平台注册用户</div>
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

        <el-row :gutter="20" class="mt-4">
            <el-col :span="12">
                <el-card shadow="never" class="chart-card">
                    <template #header>
                        <div class="card-header">平台核心数据概览</div>
                    </template>
                    <div class="chart-content" ref="barChartRef" style="height: 350px;"></div>
                </el-card>
            </el-col>
            <el-col :span="12">
                <el-card shadow="never" class="chart-card">
                    <template #header>
                        <div class="card-header">用户兴趣偏好分布 (数据源)</div>
                    </template>
                    <div class="chart-content" ref="pieChartRef" style="height: 350px;"></div>
                </el-card>
            </el-col>
        </el-row>

        <el-card shadow="never" class="ai-card mt-4">
            <template #header>
                <div class="ai-header">
                    <div class="ai-title">
                        <el-icon class="magic-icon">
                            <MagicStick />
                        </el-icon>
                        <span>DeepSeek-V3 智能运营分析报告</span>
                    </div>
                    <el-button type="primary" class="glow-btn" :loading="isAnalyzing" @click="generateAiReport">
                        <el-icon class="mr-1">
                            <DataAnalysis />
                        </el-icon>
                        {{ isAnalyzing ? 'AI 正在分析数据...' : '一键生成分析报告与建议' }}
                    </el-button>
                </div>
            </template>

            <div class="ai-content-box" v-loading="isAnalyzing" element-loading-text="AI 正在深度思考中，这可能需要几十秒...">
                <div v-if="!aiReport" class="empty-ai">
                    <el-empty description="点击右上角按钮，让 AI 基于上方的环状图数据为您提供平台运营建议" :image-size="100" />
                </div>
                <div v-else class="report-content">
                    <div class="markdown-body">{{ aiReport }}</div>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { UserFilled, Comment, Reading, MagicStick, DataAnalysis } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getAdminDashboardAPI, getPreferencesStatsAPI, chatWithAiAPI } from '../../api/index'

const statData = ref({})
const prefData = ref([]) // 偏好图表数据

const barChartRef = ref(null)
const pieChartRef = ref(null)

const isAnalyzing = ref(false)
const aiReport = ref('')

onMounted(async () => {
    try {
        // 1. 获取基本统计卡片数据
        const res = await getAdminDashboardAPI()
        statData.value = res || {}

        // 2. 获取用户兴趣偏好统计数据
        const prefRes = await getPreferencesStatsAPI()
        prefData.value = prefRes || []

        // 3. 渲染图表
        await nextTick()
        initBarChart()
        initPieChart()
    } catch (error) {
        console.error('获取数据失败', error)
    }
})

// 初始化柱状图
const initBarChart = () => {
    if (!barChartRef.value) return
    const myChart = echarts.init(barChartRef.value)
    const option = {
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: [{ type: 'category', data: ['注册用户', '留言数量', '题库数量'], axisTick: { alignWithLabel: true } }],
        yAxis: [{ type: 'value' }],
        series: [{
            name: '数量', type: 'bar', barWidth: '40%',
            data: [
                { value: statData.value.totalUsers || 0, itemStyle: { color: '#409EFF' } },
                { value: statData.value.totalSubmissions || 0, itemStyle: { color: '#67C23A' } },
                { value: statData.value.totalQuestions || 0, itemStyle: { color: '#E6A23C' } }
            ],
            label: { show: true, position: 'top' }
        }]
    }
    myChart.setOption(option)
    window.addEventListener('resize', () => myChart.resize())
}

// 初始化环形图 (用户兴趣偏好)
const initPieChart = () => {
    if (!pieChartRef.value) return
    const myChart = echarts.init(pieChartRef.value)
    const option = {
        tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c}人 ({d}%)' },
        legend: { orient: 'vertical', left: 'left' },
        series: [
            {
                name: '兴趣偏好',
                type: 'pie',
                radius: ['40%', '70%'], // 环形图
                avoidLabelOverlap: false,
                itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
                label: { show: false, position: 'center' },
                emphasis: { label: { show: true, fontSize: 20, fontWeight: 'bold' } },
                labelLine: { show: false },
                data: prefData.value.length > 0 ? prefData.value : [{ name: '暂无数据', value: 0 }]
            }
        ]
    }
    myChart.setOption(option)
    window.addEventListener('resize', () => myChart.resize())
}

// 核心功能：调用 AI 基于图表数据生成报告
const generateAiReport = async () => {
    if (!prefData.value || prefData.value.length === 0) {
        ElMessage.warning('目前还没有用户填写兴趣偏好，缺少分析数据。')
        return
    }

    // 1. 将数据拼接成字符串格式
    const dataString = prefData.value.map(item => `${item.name}: ${item.value}人选择`).join('，')

    // 2. 构造强大的专属 Prompt（提示词）
    const prompt = `
你现在是“阳光防线”禁毒科普平台的首席数据分析师。
以下是我从数据库中提取出的最新【用户兴趣偏好统计分布】：
【${dataString}】

请你基于以上真实数据，为平台管理员出具一份专业的《平台数据可视化分析与运营建议报告》。
要求包含：
1. 数据解读：分析当前用户群体的核心关注点是什么。
2. 运营指导：建议接下来应该多发布哪种类型的科普文章或公告？
3. 交互建议：基于这些偏好，如何设计在线测试或互动活动以提高用户活跃度。

请直接输出排版精美的报告正文，语气专业、客观，无需寒暄。
  `

    isAnalyzing.value = true
    try {
        // 3. 复用我们之前写的 chatWithAiAPI，直接对话大模型
        const res = await chatWithAiAPI(prompt)
        aiReport.value = res || '抱歉，分析报告生成失败，请重试。'
        ElMessage.success('报告生成完毕！')
    } catch (error) {
        ElMessage.error('AI 服务超时或异常，请稍后再试。')
    } finally {
        isAnalyzing.value = false
    }
}
</script>

<style scoped>
/* 基本卡片样式 */
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

.mr-1 {
    margin-right: 5px;
}

.chart-card {
    border-radius: 8px;
}

.card-header {
    font-weight: bold;
}

/* AI 报告区专属样式 */
.ai-card {
    border-radius: 12px;
    border: 1px solid #e0e7ff;
    background: #fafafa;
}

.ai-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.ai-title {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
    color: #3730a3;
    gap: 8px;
}

.magic-icon {
    font-size: 22px;
    color: #6366f1;
}

.glow-btn {
    background: linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%);
    border: none;
    font-weight: bold;
}

.glow-btn:hover {
    box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
    transform: translateY(-1px);
}

.ai-content-box {
    min-height: 200px;
    padding: 10px;
}

.empty-ai {
    margin-top: 20px;
}

.report-content {
    background: #fff;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.02);
}

/* Markdown 文本渲染样式 */
.markdown-body {
    font-size: 15px;
    line-height: 1.8;
    color: #374151;
    white-space: pre-wrap;
    /* 完美保留 AI 换行 */
}
</style>