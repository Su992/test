<template>
  <div class="admin-announcement">
    <el-card shadow="never">
      <div class="table-header">
        <el-button type="primary" :icon="Plus" @click="handleAdd">发布新公告</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="margin-top: 20px">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="title" label="公告标题" />
        <el-table-column prop="content" label="内容预览" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">{{ row.status === 1 ? '已发布' : '已隐藏' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="发布时间" width="180" />
        <el-table-column label="操作" width="180" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定要删除这条公告吗？" @confirm="handleDelete(row.id)">
              <template #reference><el-button link type="danger">删除</el-button></template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
        <el-pagination background layout="prev, pager, next" :total="total" v-model:current-page="queryParams.current"
          @current-change="fetchData" />
      </div>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑公告' : '发布公告'" width="50%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" placeholder="请输入公告标题" /></el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">立即发布</el-radio>
            <el-radio :label="0">暂不发布(隐藏)</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容">
          <el-input v-model="form.content" type="textarea" :rows="6" placeholder="请输入公告详细内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAdminAnnouncePageAPI, addAdminAnnounceAPI, updateAdminAnnounceAPI, deleteAdminAnnounceAPI } from '../../api/index'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({ current: 1, size: 10 })

const fetchData = async () => {
  loading.value = true
  const res = await getAdminAnnouncePageAPI(queryParams)
  tableData.value = res.records
  total.value = res.total
  loading.value = false
}

onMounted(fetchData)

const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', content: '', status: 1 })

const handleAdd = () => { Object.assign(form, { id: null, title: '', content: '', status: 1 }); dialogVisible.value = true }
const handleEdit = (row) => { Object.assign(form, row); dialogVisible.value = true }

const submitForm = async () => {
  if (form.id) await updateAdminAnnounceAPI(form)
  else await addAdminAnnounceAPI(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  fetchData()
}

const handleDelete = async (id) => {
  await deleteAdminAnnounceAPI(id)
  ElMessage.success('已删除')
  fetchData()
}
</script>

<style scoped>
.table-header {
  display: flex;
  justify-content: flex-end;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>