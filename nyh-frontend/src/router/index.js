import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user' // 路径根据你的项目实际情况微调

const routes = [
  // ================= 用户端路由 =================
  {
    path: '/',
    name: 'FrontLayout',
    component: () => import('../views/front/Layout.vue'),
    redirect: '/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/front/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'ai-assistant',
        name: 'AiAssistant',
        component: () => import('../views/front/AiAssistant.vue'),
        meta: { title: 'AI 禁毒小助手',requiresAuth: true }
      },
      {
        path: 'knowledge',
        name: 'Knowledge',
        component: () => import('../views/front/Knowledge.vue'),
        meta: { title: '毒品分类科普' }
      },
      {
        path: 'submission',
        name: 'Submission',
        component: () => import('../views/front/Submission.vue'),
        meta: { title: '留言投稿' }
      },
      {
        path: 'test',
        name: 'Test',
        component: () => import('../views/front/Test.vue'),
        meta: { title: '在线测试', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'Profile',
        component: () => import('../views/front/Profile.vue'),
        meta: { title: '个人中心', requiresAuth: true } 
      },
      {
        path: 'announcement-list',
        name: 'AnnouncementList',
        component: () => import('../views/front/AnnouncementList.vue'),
        meta: { title: '系统公告' }
      }
    ]
  },

  // ================= 管理员端路由 =================
  {
    path: '/admin',
    name: 'AdminLayout',
    component: () => import('../views/admin/Layout.vue'),
    redirect: '/admin/dashboard',
    meta: { requiresAdmin: true }, 
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/admin/Dashboard.vue'),
        meta: { title: '后台数据看板' }
      },
      {
        path: 'user',
        name: 'AdminUser',
        component: () => import('../views/admin/User.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: 'announcement',
        name: 'AdminAnnouncement',
        component: () => import('../views/admin/Announcement.vue'),
        meta: { title: '公告管理' }
      },
      {
        path: 'news',
        name: 'AdminNews',
        component: () => import('../views/admin/News.vue'),
        meta: { title: '首页推荐管理' }
      },
      {
        path: 'knowledge',
        name: 'AdminKnowledge',
        component: () => import('../views/admin/Knowledge.vue'),
        meta: { title: '科普内容管理' }
      },
      {
        path: 'submission',
        name: 'AdminSubmission',
        component: () => import('../views/admin/Submission.vue'),
        meta: { title: '留言投稿审核' }
      },
      {
        path: 'question',
        name: 'AdminQuestion',
        component: () => import('../views/admin/Question.vue'),
        meta: { title: '在线试题管理' }
      }
    ]
  },
  
  {
    path: '/:pathMatch(.*)*',
    redirect: '/home'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

/**
 * 全局前置守卫
 */
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title + ' - 禁毒科普平台'
  }

  const userStore = useUserStore()
  const userInfo = userStore.userInfo

  // 管理端权限强制拦截
  if (to.path.startsWith('/admin') || to.meta.requiresAdmin) {
    if (!userInfo) {
      ElMessage.warning('后台重地，请先登录管理员账号')
      return next('/home')
    }
    // 角色校验：0为管理员，1为普通用户
    if (userInfo.role !== 0) {
      ElMessage.error('权限不足：您的账号非管理员，禁止访问后台！')
      return next('/home')
    }
  }

  // 个人中心等受限页面拦截
  if (to.meta.requiresAuth && !userInfo) {
    ElMessage.warning('请先登录账号后再进行操作')
    return next('/home')
  }

  next()
})

export default router