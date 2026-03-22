<template>
    <div class="test-container">
        <div class="page-header">
            <h1 class="gradient-text">禁毒知识在线测试</h1>
            <p class="subtitle">检验学习成果，提升识毒防毒拒毒能力</p>
        </div>

        <el-card class="glass-card test-card" shadow="never">
            <div v-if="!isStarted && !isFinished" class="intro-section">
                <el-icon :size="60" color="#3B82F6">
                    <Trophy />
                </el-icon>
                <h2>模拟测试说明</h2>
                <p>系统将随机抽取 10 道禁毒知识单选题。答题结束后将自动评分并展示解析。</p>
                <el-button type="primary" size="large" class="gradient-btn start-btn" @click="startTest"
                    :loading="loading">
                    开 始 答 题
                </el-button>
            </div>

            <div v-else-if="isFinished" class="result-section">
                <el-progress type="dashboard" :percentage="score" :color="scoreColor" :width="150">
                    <template #default="{ percentage }">
                        <span class="percentage-value">{{ percentage }}分</span>
                    </template>
                </el-progress>
                <h2 :style="{ color: scoreColor }">{{ scoreText }}</h2>
                <el-button type="primary" plain @click="resetTest">再测一次</el-button>

                <el-divider>题目解析</el-divider>
                <div class="analysis-list">
                    <div v-for="(q, index) in questions" :key="q.id" class="analysis-item">
                        <p class="q-title">{{ index + 1 }}. {{ q.questionText }}</p>
                        <p class="q-answer">
                            您的答案：<span :class="answers[q.id] === q.correctAnswer ? 'text-success' : 'text-danger'">{{
                                answers[q.id] ||
                                '未作答' }}</span>
                            <span class="ml-3">正确答案：<strong>{{ q.correctAnswer }}</strong></span>
                        </p>
                        <p class="q-desc" v-if="q.analysis"><strong>解析：</strong>{{ q.analysis }}</p>
                    </div>
                </div>
            </div>

            <div v-else class="question-section">
                <div class="progress-bar">
                    <span>答题进度：{{ currentIndex + 1 }} / {{ questions.length }}</span>
                    <el-progress :percentage="((currentIndex + 1) / questions.length) * 100" :show-text="false" />
                </div>

                <div class="question-content" v-if="currentQuestion">
                    <h3 class="q-title">{{ currentIndex + 1 }}. {{ currentQuestion.questionText }}</h3>
                    <el-radio-group v-model="answers[currentQuestion.id]" class="options-group">
                        <el-radio label="A" size="large" border>A. {{ currentQuestion.optionA }}</el-radio>
                        <el-radio label="B" size="large" border>B. {{ currentQuestion.optionB }}</el-radio>
                        <el-radio label="C" size="large" border v-if="currentQuestion.optionC">C. {{
                            currentQuestion.optionC
                            }}</el-radio>
                        <el-radio label="D" size="large" border v-if="currentQuestion.optionD">D. {{
                            currentQuestion.optionD
                            }}</el-radio>
                    </el-radio-group>
                </div>

                <div class="action-bar">
                    <el-button @click="prevQuestion" :disabled="currentIndex === 0">上一题</el-button>
                    <el-button type="primary" @click="nextQuestion"
                        v-if="currentIndex < questions.length - 1">下一题</el-button>
                    <el-button type="success" @click="submitTest" v-else>交 卷</el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { Trophy } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRandomQuestionsAPI } from '../../api/index'

const loading = ref(false)
const isStarted = ref(false)
const isFinished = ref(false)
const questions = ref([])
const currentIndex = ref(0)
const answers = ref({}) // 结构: { questionId: 'A' }
const score = ref(0)

const currentQuestion = computed(() => questions.value[currentIndex.value])

const scoreColor = computed(() => {
    if (score.value >= 90) return '#67C23A' // 绿
    if (score.value >= 60) return '#E6A23C' // 黄
    return '#F56C6C' // 红
})

const scoreText = computed(() => {
    if (score.value >= 90) return '非常出色！您具备优秀的禁毒知识。'
    if (score.value >= 60) return '及格了，但仍需加强防范意识。'
    return '不太理想，建议多去科普区学习哦。'
})

// 开始测试
const startTest = async () => {
    loading.value = true
    try {
        const res = await getRandomQuestionsAPI(10)
        if (res && res.length > 0) {
            questions.value = res
            isStarted.value = true
        } else {
            ElMessage.warning('题库暂时没有题目，请联系管理员添加。')
        }
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false
    }
}

const prevQuestion = () => { currentIndex.value-- }
const nextQuestion = () => { currentIndex.value++ }

// 交卷
const submitTest = () => {
    ElMessageBox.confirm('确定要交卷吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '检查一下' })
        .then(() => {
            // 计算分数
            let correctCount = 0
            questions.value.forEach(q => {
                if (answers.value[q.id] === q.correctAnswer) {
                    correctCount++
                }
            })
            score.value = Math.round((correctCount / questions.value.length) * 100)
            isFinished.value = true
        })
        .catch(() => { })
}

// 重置
const resetTest = () => {
    isStarted.value = false
    isFinished.value = false
    currentIndex.value = 0
    answers.value = {}
    score.value = 0
}
</script>

<style scoped>
.page-header {
    text-align: center;
    margin-bottom: 30px;
}

.gradient-text {
    font-size: 32px;
    background: linear-gradient(135deg, #3B82F6, #10B981);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-weight: 800;
}

.subtitle {
    color: #6b7280;
    font-size: 16px;
}

.test-card {
    max-width: 800px;
    margin: 0 auto;
    min-height: 500px;
    border-radius: 16px;
    padding: 20px;
    background: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(10px);
}

.intro-section,
.result-section {
    text-align: center;
    padding: 40px 20px;
}

.intro-section p {
    color: #6b7280;
    margin-bottom: 30px;
}

.start-btn {
    width: 200px;
    font-weight: bold;
    font-size: 16px;
    border-radius: 30px;
}

.gradient-btn {
    background: linear-gradient(135deg, #10B981 0%, #3B82F6 100%);
    border: none;
}

.percentage-value {
    font-size: 28px;
    font-weight: bold;
}

.analysis-list {
    text-align: left;
    margin-top: 20px;
}

.analysis-item {
    background: #f8fafc;
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 15px;
}

.text-success {
    color: #67C23A;
    font-weight: bold;
}

.text-danger {
    color: #F56C6C;
    font-weight: bold;
}

.ml-3 {
    margin-left: 15px;
}

.progress-bar {
    margin-bottom: 30px;
    color: #6b7280;
    font-size: 14px;
}

.q-title {
    font-size: 18px;
    color: #1f2937;
    margin-bottom: 25px;
    line-height: 1.5;
}

.options-group {
    display: flex;
    flex-direction: column;
    gap: 15px;
    width: 100%;
}

.options-group :deep(.el-radio) {
    margin-right: 0;
    padding: 20px;
    height: auto;
    border-radius: 8px;
    background: #fff;
}

.options-group :deep(.el-radio.is-bordered.is-checked) {
    border-color: #3B82F6;
    background: #eff6ff;
}

.action-bar {
    display: flex;
    justify-content: space-between;
    margin-top: 40px;
}
</style>