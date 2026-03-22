import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 从本地存储初始化用户信息
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || 'null'))

  // 设置用户信息
  const setUserInfo = (data) => {
    userInfo.value = data
    localStorage.setItem('userInfo', JSON.stringify(data))
  }

  // 退出登录
  const logout = () => {
    userInfo.value = null
    localStorage.removeItem('userInfo')
    // 清理所有缓存，防止权限残留
    localStorage.clear() 
    sessionStorage.clear()
  }

  return { userInfo, setUserInfo, logout }
})