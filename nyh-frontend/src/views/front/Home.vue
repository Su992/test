<template>
  <div class="home-container">
    <div class="premium-banner">
      <div class="banner-content">
        <h1 class="animate-title">珍爱生命，远离毒品</h1>
        <p class="animate-desc">
          探索全面的毒品科普知识，体验前沿的 AI 智能防毒助手。<br />
          共建无毒社会，从了解开始。
        </p>
        <el-button size="large" type="primary" class="start-btn" round @click="$router.push('/ai-assistant')">
          立即体验 AI 助手 <el-icon class="ml-2">
            <Right />
          </el-icon>
        </el-button>
      </div>
      <div class="glow-circle shape-1"></div>
      <div class="glow-circle shape-2"></div>
    </div>

    <el-row :gutter="30" class="content-row">
      <el-col :span="16">
        <el-card shadow="never" class="premium-card" v-loading="loading">
          <template #header>
            <div class="card-header">
              <div class="header-left">
                <div class="icon-box warning-bg"><el-icon>
                    <Notification />
                  </el-icon></div>
                <span class="card-title">最新平台公告</span>
              </div>
              <el-button link type="primary" @click="$router.push('/knowledge')">更多动态 <el-icon>
                  <ArrowRight />
                </el-icon></el-button>
            </div>
          </template>

          <div v-if="announcements.length === 0" class="empty-state">
            <el-empty description="暂无公告发布" :image-size="100" />
          </div>

          <div v-else class="announcement-list">
            <div class="list-item" v-for="item in announcements" :key="item.id">
              <div class="item-main">
                <span class="dot"></span>
                <span class="title">{{ item.title }}</span>
              </div>
              <span class="date">{{ formatDate(item.createTime) }}</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card shadow="never" class="premium-card ai-recommend-card">
          <template #header>
            <div class="card-header">
              <div class="header-left">
                <div class="icon-box primary-bg"><el-icon>
                    <MuteNotification />
                  </el-icon></div>
                <span class="card-title">防毒知识每日推</span>
              </div>
            </div>
          </template>
          <div class="recommend-content">
            <div v-for="item in recommendNews" :key="item.id" class="recommend-item">
              <div class="r-icon"><el-icon>
                  <StarFilled />
                </el-icon></div>
              <div class="r-text">
                <h4>{{ item.title }}</h4>
                <p>{{ item.content.substring(0, 30) }}...</p>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Notification, ArrowRight, Right, MuteNotification, StarFilled } from '@element-plus/icons-vue'
import { getAnnouncementsAPI, getRecommendNewsAPI } from '../../api/index'

const announcements = ref([])
const recommendNews = ref([])
const loading = ref(false)

// 挂载时获取数据
onMounted(async () => {
  fetchAnnouncements()
  fetchRecommendNews()
})

const fetchAnnouncements = async () => {
  loading.value = true
  try {
    const res = await getAnnouncementsAPI(1, 6) // 获取第一页，6条
    announcements.value = res.records || [] // MyBatis Plus 分页返回的数据在 records 里
  } catch (error) {
    console.error('获取公告失败', error)
  } finally {
    loading.value = false
  }
}
const fetchRecommendNews = async () => {
  try {
    const res = await getRecommendNewsAPI()
    recommendNews.value = res || []
  } catch (error) {
    console.error('推荐获取失败', error)
  }
}

// 简单格式化时间
const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return dateStr.substring(0, 10) // 截取 yyyy-mm-dd
}
</script>

<style scoped>
/* 极光渐变 Banner */
.premium-banner {
  position: relative;
  background: #0f172a;
  /* 深邃夜空蓝 */
  border-radius: 20px;
  padding: 80px 60px;
  overflow: hidden;
  margin-bottom: 40px;
  box-shadow: 0 20px 40px -10px rgba(0, 0, 0, 0.15);
}

.banner-content {
  position: relative;
  z-index: 10;
  text-align: center;
  color: #fff;
}

.animate-title {
  font-size: 42px;
  font-weight: 800;
  margin-bottom: 20px;
  background: linear-gradient(to right, #60A5FA, #34D399);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.animate-desc {
  font-size: 18px;
  color: #94A3B8;
  line-height: 1.6;
  margin-bottom: 30px;
}

.start-btn {
  background: #fff;
  color: #0f172a;
  border: none;
  padding: 0 30px;
  font-weight: bold;
  transition: transform 0.3s;
}

.start-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 20px rgba(255, 255, 255, 0.2);
}

.glow-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.5;
}

.shape-1 {
  width: 300px;
  height: 300px;
  background: #3B82F6;
  top: -100px;
  left: -50px;
}

.shape-2 {
  width: 400px;
  height: 400px;
  background: #10B981;
  bottom: -150px;
  right: -100px;
}

/* 高级卡片样式 */
.premium-card {
  border: none;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03) !important;
  height: 420px;
  transition: box-shadow 0.3s ease;
}

.premium-card:hover {
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.06) !important;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.icon-box {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.warning-bg {
  background: linear-gradient(135deg, #F59E0B, #EF4444);
}

.primary-bg {
  background: linear-gradient(135deg, #3B82F6, #8B5CF6);
}

.card-title {
  font-size: 18px;
  font-weight: bold;
  color: #1f2937;
}

/* 列表样式 */
.announcement-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.list-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 15px;
  border-radius: 8px;
  transition: background 0.2s;
  cursor: pointer;
}

.list-item:hover {
  background: #f8fafc;
}

.item-main {
  display: flex;
  align-items: center;
  gap: 12px;
  flex: 1;
  overflow: hidden;
}

.item-main .dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background-color: #3B82F6;
}

.item-main .title {
  font-size: 15px;
  color: #374151;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  font-weight: 500;
}

.list-item:hover .title {
  color: #3B82F6;
}

.list-item .date {
  font-size: 13px;
  color: #9CA3AF;
  min-width: 80px;
  text-align: right;
}

/* 推荐内容区 */
.recommend-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.recommend-item {
  display: flex;
  gap: 15px;
  padding: 10px;
  border-radius: 12px;
  cursor: pointer;
  transition: background 0.3s;
}

.recommend-item:hover {
  background: #F3F4F6;
}

.r-icon {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  background: #E0E7FF;
  color: #4F46E5;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.r-text h4 {
  margin: 0 0 5px 0;
  font-size: 15px;
  color: #1F2937;
}

.r-text p {
  margin: 0;
  font-size: 13px;
  color: #6B7280;
}
</style>