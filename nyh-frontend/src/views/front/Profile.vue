<template>
    <div class="profile-container">
        <el-card class="glass-card profile-card" shadow="never">
            <el-tabs v-model="activeTab" class="profile-tabs">
                <el-tab-pane label="基本资料设置" name="info">
                    <el-form :model="form" ref="formRef" label-width="100px" class="profile-form">
                        <el-form-item label="用户头像">
                            <el-upload class="avatar-uploader" action="#" :show-file-list="false" :auto-upload="false"
                                :on-change="handleAvatarChange">
                                <img v-if="form.avatar" :src="form.avatar" class="avatar" />
                                <el-icon v-else class="avatar-uploader-icon">
                                    <Plus />
                                </el-icon>
                            </el-upload>
                        </el-form-item>
                        <el-form-item label="登录账号"><el-input v-model="form.username" disabled /></el-form-item>
                        <el-form-item label="用户昵称" prop="nickname"><el-input v-model="form.nickname" /></el-form-item>
                        <el-form-item label="兴趣偏好" prop="preferences">
                            <el-select v-model="form.preferences" multiple style="width: 100%">
                                <el-option label="传统毒品科普" value="传统毒品" />
                                <el-option label="新型毒品识别" value="新型毒品" />
                                <el-option label="禁毒法律法规" value="法律法规" />
                                <el-option label="心理健康疏导" value="心理健康" />
                            </el-select>
                        </el-form-item>
                        <el-form-item label="修改密码" prop="password"><el-input v-model="form.password" type="password"
                                show-password placeholder="不修改请留空" /></el-form-item>
                        <el-form-item><el-button type="primary" class="gradient-btn" @click="saveProfile"
                                :loading="saving">保存修改</el-button></el-form-item>
                    </el-form>
                </el-tab-pane>

                <el-tab-pane label="我的测试记录" name="records">
                    <el-table :data="recordList" border stripe style="width: 100%; margin-top: 10px;" height="400">
                        <el-table-column type="index" label="序号" width="60" align="center" />
                        <el-table-column prop="createTime" label="交卷时间" width="180">
                            <template #default="{ row }">{{ formatDate(row.createTime) }}</template>
                        </el-table-column>
                        <el-table-column prop="totalQuestions" label="总题数" width="100" align="center" />
                        <el-table-column prop="score" label="最终得分" align="center">
                            <template #default="{ row }">
                                <el-tag :type="row.score >= 90 ? 'success' : (row.score >= 60 ? 'warning' : 'danger')"
                                    effect="dark" round>
                                    {{ row.score }} 分
                                </el-tag>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
            </el-tabs>
        </el-card>
    </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../../store/user'
import { updateUserInfoAPI, getMyTestRecordsAPI } from '../../api/index'

const userStore = useUserStore()
const saving = ref(false)
const activeTab = ref('info')
const recordList = ref([])

const form = reactive({
    id: '',
    username: '',
    nickname: '',
    password: '',
    avatar: ''
})

onMounted(() => {
    if (userStore.userInfo) {
        form.id = userStore.userInfo.id
        form.username = userStore.userInfo.username
        form.nickname = userStore.userInfo.nickname || ''
        form.avatar = userStore.userInfo.avatar || ''
        form.preferences = userStore.userInfo.preferences ? userStore.userInfo.preferences.split(',') : []
        // 密码不回显
    }
})

// 将图片转为 Base64
const handleAvatarChange = (file) => {
    const reader = new FileReader()
    reader.readAsDataURL(file.raw)
    reader.onload = () => {
        form.avatar = reader.result
    }
}

const saveProfile = async () => {
    saving.value = true
    try {
        // 组装提交数据（如果密码为空，就不提交密码字段）
        const submitData = { ...form }
        if (Array.isArray(submitData.preferences)) {
            submitData.preferences = submitData.preferences.join(',')
        }
        if (!submitData.password) {
            delete submitData.password
        }

        await updateUserInfoAPI(submitData)
        ElMessage.success('个人信息更新成功！')

        // 更新本地 Pinia 状态
        userStore.setUserInfo({ ...userStore.userInfo, ...submitData })
    } catch (error) {
        // 错误处理交由 request.js
    } finally {
        saving.value = false
    }
}

// 拉取成绩单方法
const fetchRecords = async (userId) => {
    try {
        const res = await getMyTestRecordsAPI(userId)
        recordList.value = res || []
    } catch (error) {
        console.error('获取成绩失败', error)
    }
}

const formatDate = (dateStr) => {
    if (!dateStr) return ''
    return dateStr.substring(0, 16).replace('T', ' ')
}
</script>

<style scoped>
.profile-container {
    display: flex;
    justify-content: center;
    padding: 40px 0;
}

.profile-card {
    width: 600px;
    padding: 30px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.9);
}

.profile-header {
    text-align: center;
    margin-bottom: 30px;
    border-bottom: 1px solid #f3f4f6;
    padding-bottom: 20px;
}

.profile-header h2 {
    margin: 0 0 10px 0;
    color: #1f2937;
}

.profile-header p {
    margin: 0;
    color: #6b7280;
    font-size: 14px;
}

/* 头像上传组件样式 */
.avatar-uploader :deep(.el-upload) {
    border: 1px dashed #d9d9d9;
    border-radius: 50%;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: border-color 0.3s;
}

.avatar-uploader :deep(.el-upload:hover) {
    border-color: #3B82F6;
}

.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 100px;
    height: 100px;
    text-align: center;
    line-height: 100px;
}

.avatar {
    width: 100px;
    height: 100px;
    display: block;
    object-fit: cover;
}

.gradient-btn {
    background: linear-gradient(135deg, #10B981 0%, #3B82F6 100%);
    border: none;
    padding: 0 30px;
}

.profile-container {
    display: flex;
    justify-content: center;
    padding: 40px 0;
}

.profile-card {
    width: 700px;
    padding: 20px;
    border-radius: 16px;
    background: rgba(255, 255, 255, 0.9);
}

.profile-tabs :deep(.el-tabs__item) {
    font-size: 16px;
    font-weight: bold;
}
</style>