<template>
  <div class="admin-knowledge">
    <el-card shadow="never">
      <div class="table-header">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增科普文章</el-button>
      </div>

      <el-table :data="tableData" v-loading="loading" border stripe style="margin-top: 20px">
        <el-table-column prop="title" label="科普标题" width="200" />
        <el-table-column prop="category" label="分类" width="120">
          <template #default="{ row }">
            <el-tag>{{ row.category || '未分类' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="正文预览" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="150" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-popconfirm title="确定要删除这篇科普吗？" @confirm="handleDelete(row.id)">
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑科普' : '新增科普'" width="60%">
      <el-form :model="form" label-width="80px">
        <el-form-item label="文章标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="所属分类">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option label="传统毒品" value="传统毒品" />
            <el-option label="新型合成毒品" value="新型合成毒品" />
            <el-option label="法律法规" value="法律法规" />
            <el-option label="综合知识" value="综合知识" />
          </el-select>
        </el-form-item>
        <el-form-item label="封面图上传">
          <el-upload class="avatar-uploader" action="#" :show-file-list="false" :auto-upload="false"
            :on-change="handleImageChange">
            <img v-if="form.coverImage" :src="form.coverImage" class="avatar"
              style="width: 150px; height: 100px; object-fit: cover;" />
            <el-icon v-else class="avatar-uploader-icon"
              style="font-size: 28px; width: 150px; height: 100px; border: 1px dashed #d9d9d9; line-height: 100px; text-align: center;">
              <Plus />
            </el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="正文内容"><el-input v-model="form.content" type="textarea" :rows="10"
            placeholder="支持长文本输入" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">保存发布</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAdminKnowledgePageAPI, addAdminKnowledgeAPI, updateAdminKnowledgeAPI, deleteAdminKnowledgeAPI } from '../../api/index'

const loading = ref(false)
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({ current: 1, size: 10 })

const fetchData = async () => {
  loading.value = true
  const res = await getAdminKnowledgePageAPI(queryParams)
  tableData.value = res.records
  total.value = res.total
  loading.value = false
}

const handleImageChange = (file) => {
  const reader = new FileReader()
  reader.readAsDataURL(file.raw)
  reader.onload = () => {
    form.coverImage = reader.result
  }
}

onMounted(fetchData)

const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', category: '', coverImage: '', content: '' })

const handleAdd = () => { Object.assign(form, { id: null, title: '', category: '', coverImage: '', content: '' }); dialogVisible.value = true }
const handleEdit = (row) => { Object.assign(form, row); dialogVisible.value = true }

const submitForm = async () => {
  if (form.id) await updateAdminKnowledgeAPI(form)
  else await addAdminKnowledgeAPI(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  fetchData()
}

const handleDelete = async (id) => {
  await deleteAdminKnowledgeAPI(id)
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