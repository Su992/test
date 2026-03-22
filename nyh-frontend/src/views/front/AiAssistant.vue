<template>
  <div class="chat-container">
    <el-card class="chat-card glass-card" shadow="never">
      <div class="chat-header">
        <div class="ai-info">
          <el-avatar :size="46" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" class="ai-avatar" />
          <div class="ai-text">
            <h2>阳光智导 · AI 小助手</h2>
            <p>基于 DeepSeek-V3 引擎，为您解答禁毒疑惑或进行情感交流</p>
          </div>
        </div>
        <el-button plain round type="danger" size="small" @click="clearChat" :icon="Delete">清空对话</el-button>
      </div>

      <div class="chat-body" ref="chatBodyRef">
        <div v-for="(msg, index) in messageList" :key="index" :class="['message-wrapper', msg.role]">
          
          <el-avatar v-if="msg.role === 'ai'" :size="40" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
          
          <div class="message-bubble">
            <div v-if="msg.loading" class="typing-indicator">
              <span></span><span></span><span></span>
            </div>
            <div v-else class="text-content">{{ msg.content }}</div>
          </div>

          <el-avatar v-if="msg.role === 'user'" :size="40" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
        </div>
      </div>

      <div class="chat-footer">
        <el-input
          v-model="inputMessage"
          type="textarea"
          :rows="3"
          placeholder="请输入您的问题或想倾诉的话题，按 Enter 键发送..."
          resize="none"
          @keydown.enter.prevent="sendMessage"
        />
        <div class="action-bar">
          <span class="tip">内容由 AI 生成，仅供参考</span>
          <el-button type="primary" class="send-btn" @click="sendMessage" :loading="isSending">
            <el-icon class="mr-1"><Position /></el-icon> 发 送
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { Position, Delete } from '@element-plus/icons-vue'
import { chatWithAiAPI } from '../../api/index'
import { ElMessage } from 'element-plus'

const chatBodyRef = ref(null)
const inputMessage = ref('')
const isSending = ref(false)

// 初始消息列表，打个招呼
const messageList = ref([
  {
    role: 'ai',
    content: '你好！我是阳光防线的禁毒科普小助手。你可以问我关于毒品种类、危害、禁毒法律的问题，或者有什么烦心事也可以和我说说哦。',
    loading: false
  }
])

// 滚动到底部的方法
const scrollToBottom = async () => {
  await nextTick()
  if (chatBodyRef.value) {
    chatBodyRef.value.scrollTop = chatBodyRef.value.scrollHeight
  }
}

// 发送消息
const sendMessage = async () => {
  const text = inputMessage.value.trim()
  if (!text) {
    ElMessage.warning('不能发送空消息哦')
    return
  }

  // 1. 把用户的话推入列表
  messageList.value.push({ role: 'user', content: text, loading: false })
  inputMessage.value = '' // 清空输入框
  scrollToBottom()

  // 2. 推入一个 AI 的等待状态气泡
  const aiWaitIndex = messageList.value.push({ role: 'ai', content: '', loading: true }) - 1
  scrollToBottom()

  isSending.value = true
  try {
    // 3. 调用后端接口
    const res = await chatWithAiAPI(text)
    // 4. 替换等待气泡的内容
    messageList.value[aiWaitIndex].content = res || '抱歉，我好像没懂你的意思。'
    messageList.value[aiWaitIndex].loading = false
  } catch (error) {
    messageList.value[aiWaitIndex].content = '网络或服务异常，请稍后再试。'
    messageList.value[aiWaitIndex].loading = false
  } finally {
    isSending.value = false
    scrollToBottom()
  }
}

// 清空对话
const clearChat = () => {
  messageList.value = [
    { role: 'ai', content: '对话已清空。有什么我可以继续帮您的吗？', loading: false }
  ]
}
</script>

<style scoped>
.chat-container {
  max-width: 1000px;
  margin: 0 auto;
  height: calc(100vh - 150px);
  display: flex;
}

.glass-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  border: none;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  box-shadow: 0 10px 30px rgba(0,0,0,0.08);
  overflow: hidden;
}

.chat-header {
  padding: 20px 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid rgba(0,0,0,0.05);
  background: #fff;
}
.ai-info { display: flex; align-items: center; gap: 15px; }
.ai-text h2 { margin: 0 0 5px 0; font-size: 18px; color: #1f2937; }
.ai-text p { margin: 0; font-size: 13px; color: #6b7280; }

.chat-body {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
  background: #f8fafc;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.message-wrapper {
  display: flex;
  align-items: flex-start;
  gap: 15px;
  max-width: 85%;
}
.message-wrapper.user {
  align-self: flex-end;
  flex-direction: row-reverse;
}

.message-bubble {
  padding: 12px 18px;
  border-radius: 12px;
  font-size: 15px;
  line-height: 1.6;
  box-shadow: 0 2px 10px rgba(0,0,0,0.02);
}

.ai .message-bubble {
  background: #ffffff;
  color: #374151;
  border-top-left-radius: 2px;
  border: 1px solid #e5e7eb;
}

.user .message-bubble {
  background: linear-gradient(135deg, #10B981 0%, #3B82F6 100%);
  color: #ffffff;
  border-top-right-radius: 2px;
}

.text-content {
  white-space: pre-wrap; /* 允许 AI 返回的内容自动换行 */
}

.chat-footer {
  padding: 20px 30px;
  background: #fff;
  border-top: 1px solid rgba(0,0,0,0.05);
}

:deep(.el-textarea__inner) {
  background: #f3f4f6;
  border: none;
  border-radius: 8px;
  box-shadow: none !important;
  font-size: 15px;
}
:deep(.el-textarea__inner:focus) { background: #fff; border: 1px solid #10B981; }

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 15px;
}
.tip { font-size: 12px; color: #9ca3af; }
.send-btn { border-radius: 20px; padding: 0 25px; }
.mr-1 { margin-right: 5px; }

/* AI Loading 打字动画 */
.typing-indicator { display: flex; gap: 4px; padding: 5px 0; }
.typing-indicator span {
  width: 6px; height: 6px; background-color: #9ca3af; border-radius: 50%;
  animation: typing 1.4s infinite ease-in-out both;
}
.typing-indicator span:nth-child(1) { animation-delay: -0.32s; }
.typing-indicator span:nth-child(2) { animation-delay: -0.16s; }
@keyframes typing {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}
</style>