import request from '../utils/request'

// ================= 用户端通用接口 =================
export const loginAPI = (data) => request({ url: '/user/login', method: 'post', data })
export const registerAPI = (data) => request({ url: '/user/register', method: 'post', data })
export const updateUserInfoAPI = (data) => request({ url: '/user/update', method: 'put', data })
export const getAnnouncementsAPI = (current, size) => request({ url: '/sys-announcement/announcements', method: 'get', params: { current, size } })
export const getRecommendNewsAPI = () => request({ url: '/sys-news/recommend', method: 'get' })
export const getKnowledgePageAPI = (current, size) => request({ url: '/drug-knowledge/page', method: 'get', params: { current, size } })
export const addSubmissionAPI = (data) => request({ url: '/submission/add', method: 'post', data })
export const getPublicSubmissionsAPI = (current, size) => request({ url: '/submission/public-list', method: 'get', params: { current, size } })
export const getRandomQuestionsAPI = (limit) => request({ url: '/test/random', method: 'get', params: { limit } })
export const chatWithAiAPI = (message) => request({ url: '/ai-assistant/chat', method: 'post', data: { message } })

// ================= 管理端专属接口 =================

// 1. 数据看板
export const getAdminDashboardAPI = () => request({ url: '/admin/data/dashboard', method: 'get' })

// 2. 用户管理
export const getAdminUserPageAPI = (params) => request({ url: '/admin/user/page', method: 'get', params })
export const updateAdminUserAPI = (data) => request({ url: '/admin/user', method: 'put', data })
export const deleteAdminUserAPI = (id) => request({ url: `/admin/user/${id}`, method: 'delete' })

// 3. 公告管理
export const getAdminAnnouncePageAPI = (params) => request({ url: '/admin/announcement/page', method: 'get', params })
export const addAdminAnnounceAPI = (data) => request({ url: '/admin/announcement', method: 'post', data })
export const updateAdminAnnounceAPI = (data) => request({ url: '/admin/announcement', method: 'put', data })
export const deleteAdminAnnounceAPI = (id) => request({ url: `/admin/announcement/${id}`, method: 'delete' })

// 4. 科普管理
export const addAdminKnowledgeAPI = (data) => request({ url: '/drug-knowledge', method: 'post', data })
export const updateAdminKnowledgeAPI = (data) => request({ url: '/drug-knowledge', method: 'put', data })
export const deleteAdminKnowledgeAPI = (id) => request({ url: `/drug-knowledge/${id}`, method: 'delete' })

// 5. 留言审核 (核心：人工审核接口)
export const getAdminSubmissionPageAPI = (params) => request({ url: '/admin/submission/page', method: 'get', params })
export const auditSubmissionAPI = (id, status) => request({ url: `/admin/submission/audit/${id}`, method: 'put', params: { status } })
export const deleteSubmissionAPI = (id) => request({ url: `/admin/submission/${id}`, method: 'delete' })

// 6. 题库管理
export const getAdminQuestionPageAPI = (params) => request({ url: '/admin/question/page', method: 'get', params })
export const addAdminQuestionAPI = (data) => request({ url: '/admin/question', method: 'post', data })
export const updateAdminQuestionAPI = (data) => request({ url: '/admin/question', method: 'put', data })
export const deleteAdminQuestionAPI = (id) => request({ url: `/admin/question/${id}`, method: 'delete' })
// 7. 首页推荐消息管理
export const getAdminNewsPageAPI = (params) => request({ url: '/admin/news/page', method: 'get', params })
export const addAdminNewsAPI = (data) => request({ url: '/admin/news', method: 'post', data })
export const updateAdminNewsAPI = (data) => request({ url: '/admin/news', method: 'put', data })
export const deleteAdminNewsAPI = (id) => request({ url: `/admin/news/${id}`, method: 'delete' })
// 8. 毒品科普管理 (复用了前面的 /drug-knowledge 接口路径)
export const getAdminKnowledgePageAPI = (params) => request({ url: '/drug-knowledge/page', method: 'get', params })
// ===== 测试记录接口 =====
export const addTestRecordAPI = (data) => request({ url: '/test-record/add', method: 'post', data })
export const getMyTestRecordsAPI = (userId) => request({ url: '/test-record/my-records', method: 'get', params: { userId } })
// ===== 数据看板统计接口 =====
export const getPreferencesStatsAPI = () => request({ url: '/admin/data/preferences', method: 'get' })