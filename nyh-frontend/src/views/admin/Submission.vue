<template>
  <div class="admin-submission">
    <el-card shadow="never">
      <template #header>
        <el-radio-group v-model="queryParams.manualAuditStatus" @change="handleSearch">
          <el-radio-button :label="null">全部</el-radio-button>
          <el-radio-button :label="0">待人工审核</el-radio-button>
          <el-radio-button :label="1">已通过</el-radio-button>
          <el-radio-button :label="2">未通过</el-radio-button>
        </el-radio-group>
      </template>

      <el-table :data="tableData" border stripe>
        <el-table-column prop="title" label="标题" width="200" />
        <el-table-column prop="content" label="内容" show-overflow-tooltip />
        <el-table-column label="AI 预审结果" width="180">
          <template #default="{ row }">
            <el-tooltip :content="row.aiAuditRemark" placement="top">
              <el-tag :type="row.aiAuditStatus === 1 ? 'success' : 'danger'">
                {{ row.aiAuditStatus === 1 ? 'AI通过' : 'AI拦截' }}
              </el-tag>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" align="center">
          <template #default="{ row }">
            <div v-if="row.manualAuditStatus === 0">
              <el-button type="success" size="small" @click="handleAudit(row.id, 1)">通过</el-button>
              <el-button type="danger" size="small" @click="handleAudit(row.id, 2)">驳回</el-button>
            </div>
            <el-tag v-else :type="row.manualAuditStatus === 1 ? 'success' : 'info'">
              {{ row.manualAuditStatus === 1 ? '审核通过' : '已驳回' }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminSubmissionPageAPI, auditSubmissionAPI } from '../../api/index'

const tableData = ref([])
const queryParams = reactive({ current: 1, size: 10, manualAuditStatus: 0 })

const fetchData = async () => {
  const res = await getAdminSubmissionPageAPI(queryParams)
  tableData.value = res.records
}
onMounted(fetchData)
const handleSearch = () => { queryParams.current = 1; fetchData() }

const handleAudit = async (id, status) => {
  await auditSubmissionAPI(id, status)
  ElMessage.success('处理成功')
  fetchData()
}
</script>