<template>
  <div class="admin-news">
    <el-card shadow="never">
      <div class="table-header">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增推荐消息</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="margin-top: 20px">
        <el-table-column prop="title" label="消息标题" />
        <el-table-column prop="tags" label="关联标签" width="150">
          <template #default="{ row }">
            <el-tag type="warning" v-if="row.tags">{{ row.tags }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="内容预览" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定要删除吗？" @confirm="handleDelete(row.id)">
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑推荐消息' : '新增推荐消息'" width="50%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="标签"><el-input v-model="form.tags" placeholder="如：新型毒品,心理健康 (用逗号分隔)" /></el-form-item>
        <el-form-item label="内容"><el-input v-model="form.content" type="textarea" :rows="4" /></el-form-item>
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
import { getAdminNewsPageAPI, addAdminNewsAPI, updateAdminNewsAPI, deleteAdminNewsAPI } from '../../api/index'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({ current: 1, size: 10 })

const fetchData = async () => {
  loading.value = true
  const res = await getAdminNewsPageAPI(queryParams)
  tableData.value = res.records
  total.value = res.total
  loading.value = false
}

onMounted(fetchData)

const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', content: '', tags: '' })

const handleAdd = () => { Object.assign(form, { id: null, title: '', content: '', tags: '' }); dialogVisible.value = true }
const handleEdit = (row) => { Object.assign(form, row); dialogVisible.value = true }

const submitForm = async () => {
  if (form.id) await updateAdminNewsAPI(form)
  else await addAdminNewsAPI(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  fetchData()
}

const handleDelete = async (id) => {
  await deleteAdminNewsAPI(id)
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