<template>
    <div class="knowledge-container">
        <div class="page-header">
            <h1 class="gradient-text">毒品分类与危害科普</h1>
            <p class="subtitle">认清毒品真面目，构筑坚固心理防线</p>
        </div>

        <el-skeleton :loading="loading" animated :count="4">
            <template #template>
                <div style="padding: 14px; width: 23%; display: inline-block; margin: 1%;">
                    <el-skeleton-item variant="image" style="width: 100%; height: 180px;" />
                    <div style="padding: 14px;">
                        <el-skeleton-item variant="h3" style="width: 50%;" />
                        <el-skeleton-item variant="text" style="margin-top: 16px;" />
                        <el-skeleton-item variant="text" style="width: 30%;" />
                    </div>
                </div>
            </template>

            <template #default>
                <div v-if="knowledgeList.length === 0" class="empty-state">
                    <el-empty description="管理员暂未发布科普文章" />
                </div>

                <el-row :gutter="20" class="card-grid">
                    <el-col :span="6" v-for="item in knowledgeList" :key="item.id">
                        <el-card class="knowledge-card glass-card" shadow="hover" @click="openDetail(item)">
                            <div class="card-img-wrapper">
                                <img v-if="item.coverImage" :src="item.coverImage" class="card-img" />
                                <div v-else class="img-placeholder">{{ item.category || '科普' }}</div>
                            </div>
                            <div class="card-content">
                                <el-tag size="small" effect="dark" class="category-tag">{{ item.category || '综合知识'
                                    }}</el-tag>
                                <h3 class="card-title">{{ item.title }}</h3>
                                <p class="card-date"><el-icon>
                                        <Calendar />
                                    </el-icon> {{ formatDate(item.createTime) }}</p>
                            </div>
                        </el-card>
                    </el-col>
                </el-row>

                <div class="pagination-wrapper" v-if="total > 0">
                    <el-pagination background layout="prev, pager, next" :total="total" :page-size="pageSize"
                        v-model:current-page="currentPage" @current-change="fetchData" />
                </div>
            </template>
        </el-skeleton>

        <el-drawer v-model="drawerVisible" :title="currentArticle.title" size="40%">
            <div class="article-meta">
                <el-tag>{{ currentArticle.category }}</el-tag>
                <span class="meta-date">发布于：{{ formatDate(currentArticle.createTime) }}</span>
            </div>
            <el-divider />
            <div class="article-body">
                {{ currentArticle.content }}
            </div>
        </el-drawer>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Calendar } from '@element-plus/icons-vue'
import { getKnowledgePageAPI } from '../../api/index'

const knowledgeList = ref([])
const loading = ref(true)
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(8)

const drawerVisible = ref(false)
const currentArticle = ref({})

onMounted(() => {
    fetchData()
})

const fetchData = async () => {
    loading.value = true
    try {
        const res = await getKnowledgePageAPI(currentPage.value, pageSize.value)
        knowledgeList.value = res.records || []
        total.value = res.total || 0
    } catch (error) {
        console.error(error)
    } finally {
        loading.value = false
    }
}

const openDetail = (item) => {
    currentArticle.value = item
    drawerVisible.value = true
}

const formatDate = (dateStr) => {
    if (!dateStr) return ''
    return dateStr.substring(0, 10)
}
</script>

<style scoped>
.page-header {
    text-align: center;
    margin-bottom: 40px;
}

.gradient-text {
    font-size: 32px;
    background: linear-gradient(135deg, #10B981, #3B82F6);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    font-weight: 800;
    margin-bottom: 10px;
}

.subtitle {
    color: #6b7280;
    font-size: 16px;
}

.card-grid {
    margin-bottom: 30px;
}

.knowledge-card {
    border-radius: 12px;
    cursor: pointer;
    transition: transform 0.3s;
    border: none;
    overflow: hidden;
}

.knowledge-card:hover {
    transform: translateY(-5px);
}

.card-img-wrapper {
    height: 160px;
    overflow: hidden;
    position: relative;
}

.card-img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.img-placeholder {
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #a1c4fd 0%, #c2e9fb 100%);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
    color: #fff;
    font-weight: bold;
}

.card-content {
    padding: 15px;
}

.category-tag {
    margin-bottom: 10px;
    background: #10B981;
    border: none;
}

.card-title {
    font-size: 16px;
    font-weight: 600;
    color: #1f2937;
    margin: 0 0 10px 0;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.card-date {
    font-size: 13px;
    color: #9ca3af;
    margin: 0;
    display: flex;
    align-items: center;
    gap: 5px;
}

.pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 30px;
}

.article-meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.meta-date {
    color: #9ca3af;
    font-size: 14px;
}

.article-body {
    font-size: 16px;
    line-height: 1.8;
    color: #374151;
    white-space: pre-wrap;
}
</style>