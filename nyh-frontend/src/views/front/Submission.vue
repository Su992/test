<template>
    <div class="submission-container">
        <div class="page-header">
            <h1 class="gradient-text">防线发声 · 留言投稿</h1>
            <p class="subtitle">分享您的感悟、故事或建议，内容将由 AI 与人工双重审核</p>
        </div>

        <el-row :gutter="40">
            <el-col :span="10">
                <div class="glass-panel form-panel">
                    <h3 class="panel-title"><el-icon>
                            <EditPen />
                        </el-icon> 撰写内容</h3>

                    <el-form :model="form" :rules="rules" ref="formRef" layout="vertical" class="submit-form">
                        <el-form-item label="标题" prop="title">
                            <el-input v-model="form.title" placeholder="给你的内容起个响亮的标题吧" />
                        </el-form-item>
                        <el-form-item label="内容正文" prop="content">
                            <el-input v-model="form.content" type="textarea" :rows="8"
                                placeholder="请文明发言，共同维护网络环境健康。AI 预审系统将自动拦截违规内容。" resize="none" />
                        </el-form-item>
                        <el-button type="primary" class="submit-btn" size="large" @click="submit" :loading="submitting">
                            {{ submitting ? 'AI 正在进行安全预审...' : '提 交 发 布' }}
                        </el-button>
                    </el-form>
                </div>
            </el-col>

            <el-col :span="14">
                <div class="glass-panel wall-panel">
                    <h3 class="panel-title"><el-icon>
                            <ChatLineSquare />
                        </el-icon> 精选留言墙</h3>

                    <el-skeleton :loading="loading" animated>
                        <template #default>
                            <div v-if="publicList.length === 0" class="empty-state">
                                <el-empty description="暂无精选留言，快来抢沙发！" />
                            </div>

                            <div class="timeline-container" v-else>
                                <el-timeline>
                                    <el-timeline-item v-for="item in publicList" :key="item.id"
                                        :timestamp="formatDate(item.createTime)" placement="top" color="#10B981">
                                        <el-card shadow="never" class="timeline-card">
                                            <h4 class="msg-title">{{ item.title }}</h4>
                                            <p class="msg-content">{{ item.content }}</p>
                                        </el-card>
                                    </el-timeline-item>
                                </el-timeline>
                            </div>
                        </template>
                    </el-skeleton>
                </div>
            </el-col>
        </el-row>
    </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { EditPen, ChatLineSquare } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addSubmissionAPI, getPublicSubmissionsAPI } from '../../api/index'
import { useUserStore } from '../../store/user'

const userStore = useUserStore()
const formRef = ref(null)
const submitting = ref(false)
const loading = ref(true)
const publicList = ref([])

const form = reactive({
    title: '',
    content: ''
})

const rules = {
    title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
    content: [{ required: true, message: '请输入内容', trigger: 'blur' }, { min: 10, message: '内容不能少于10个字', trigger: 'blur' }]
}

onMounted(() => {
    fetchPublicList()
})

// 获取公开留言列表
const fetchPublicList = async () => {
    loading.value = true
    try {
        const res = await getPublicSubmissionsAPI(1, 20)
        publicList.value = res.records || []
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false
    }
}

// 提交表单
const submit = async () => {
    // 1. 登录拦截：必须登录才能投稿
    if (!userStore.userInfo) {
        ElMessage.warning('请先点击右上角登录账号后，再进行留言投稿哦！')
        return
    }

    if (!formRef.value) return
    await formRef.value.validate(async (valid) => {
        if (valid) {
            submitting.value = true
            try {
                // 构建提交数据，带上用户 ID
                const submitData = {
                    userId: userStore.userInfo.id,
                    title: form.title,
                    content: form.content
                }
                await addSubmissionAPI(submitData)

                // 成功提示 (后端在这一步已经调用了 AI 预审)
                ElMessageBox.alert(
                    '您的内容已提交成功！目前已进入 AI 安全预审与人工复核通道，通过后将在精选墙展示。',
                    '提交成功',
                    { type: 'success', confirmButtonText: '我知道了' }
                )
                formRef.value.resetFields() // 清空表单
            } catch (error) {
                // 报错由 request.js 拦截
            } finally {
                submitting.value = false
            }
        }
    })
}

const formatDate = (dateStr) => {
    if (!dateStr) return ''
    return dateStr.substring(0, 16).replace('T', ' ')
}
</script>

<style scoped>
.page-header {
    text-align: center;
    margin-bottom: 40px;
}

.gradient-text {
    font-size: 32px;
    background: linear-gradient(135deg, #8B5CF6, #EC4899);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-weight: 800;
    margin-bottom: 10px;
}

.subtitle {
    color: #6b7280;
    font-size: 16px;
}

.glass-panel {
    background: rgba(255, 255, 255, 0.7);
    backdrop-filter: blur(10px);
    border-radius: 16px;
    padding: 30px;
    border: 1px solid rgba(255, 255, 255, 0.4);
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
    min-height: 500px;
}

.panel-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 20px;
    color: #1f2937;
    margin-top: 0;
    margin-bottom: 25px;
    border-bottom: 2px solid #f3f4f6;
    padding-bottom: 15px;
}

.submit-btn {
    width: 100%;
    border-radius: 8px;
    background: linear-gradient(135deg, #8B5CF6, #EC4899);
    border: none;
    font-weight: bold;
}

.submit-btn:hover {
    opacity: 0.9;
    transform: translateY(-1px);
    box-shadow: 0 5px 15px rgba(236, 72, 153, 0.3);
}

.timeline-container {
    height: 400px;
    overflow-y: auto;
    padding-right: 15px;
}

/* 自定义滚动条 */
.timeline-container::-webkit-scrollbar {
    width: 6px;
}

.timeline-container::-webkit-scrollbar-thumb {
    background-color: #d1d5db;
    border-radius: 4px;
}

.timeline-card {
    border-radius: 12px;
    border: 1px solid #f3f4f6;
    background: rgba(255, 255, 255, 0.9);
}

.msg-title {
    margin: 0 0 8px 0;
    color: #1f2937;
    font-size: 16px;
}

.msg-content {
    margin: 0;
    color: #4b5563;
    font-size: 14px;
    line-height: 1.6;
}
</style>