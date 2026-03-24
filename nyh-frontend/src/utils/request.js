import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建 axios 实例
const request = axios.create({
  baseURL: 'http://localhost:8080', // 对应你的 Spring Boot 端口
  timeout: 60000 // 大模型请求可能较慢，超时时间设长一点，15秒
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 如果后续加入了 JWT Token，可以在这里统一带上 Header
    // const token = localStorage.getItem('token')
    // if (token) {
    //   config.headers['Authorization'] = 'Bearer ' + token
    // }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    // 这里的 res 就是后端返回的 Result 对象
    let res = response.data
    
    // 如果返回的 code 是 200，说明业务成功，直接返回 data 部分
    if (res.code === 200) {
      return res.data
    } else {
      // 如果 code 不是 200，说明业务报错（比如账号密码错误），弹出后端的 msg
      ElMessage.error(res.msg || '系统错误')
      return Promise.reject(new Error(res.msg || 'Error'))
    }
  },
  error => {
    // HTTP 状态码错误（比如 404, 500 断网等）
    console.error('err' + error)
    ElMessage.error(error.message || '网络请求错误')
    return Promise.reject(error)
  }
)

export default request