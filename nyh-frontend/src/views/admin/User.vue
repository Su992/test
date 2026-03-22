<template>
  <div class="admin-user">
    <el-card shadow="never">
      <div class="table-header">
        <el-input v-model="queryParams.username" placeholder="输入用户名搜索" style="width: 250px" clearable @clear="handleSearch" />
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="width: 100%; margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="nickname" label="昵称" width="150" />
        <el-table-column prop="role" label="身份" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.role === 0 ? 'danger' : 'success'">{{ row.role === 0 ? '管理员' : '普通用户' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="注册时间" width="180" />
        <el-table-column label="操作" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">修改角色</el-button>
            <el-popconfirm title="确定要删除该用户吗？" @confirm="handleDelete(row.id)">
              <template #reference><el-button link type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination background layout="prev, pager, next" :total="total" v-model:current-page="queryParams.current" @current-change="fetchData" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" title="修改用户角色" width="300px">
      <el-radio-group v-model="editForm.role">
        <el-radio :label="0">管理员</el-radio>
        <el-radio :label="1">普通用户</el-radio>
      </el-radio-group>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAdminUserPageAPI, updateAdminUserAPI, deleteAdminUserAPI } from '../../api/index'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({ current: 1, size: 10, username: '' })

const fetchData = async () => {
  loading.value = true
  const res = await getAdminUserPageAPI(queryParams)
  tableData.value = res.records
  total.value = res.total
  loading.value = false
}

onMounted(fetchData)
const handleSearch = () => { queryParams.current = 1; fetchData() }

// 修改逻辑
const dialogVisible = ref(false)
const editForm = reactive({ id: '', role: 1 })
const handleEdit = (row) => { 
  editForm.id = row.id; 
  editForm.role = row.role; 
  dialogVisible.value = true 
}
const saveEdit = async () => {
  await updateAdminUserAPI(editForm)
  ElMessage.success('更新成功')
  dialogVisible.value = false
  fetchData()
}

const handleDelete = async (id) => {
  await deleteAdminUserAPI(id)
  ElMessage.success('已删除')
  fetchData()
}
</script>