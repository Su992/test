<template>
  <div class="admin-question">
    <el-card shadow="never">
      <div class="table-header">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增题目</el-button>
      </div>

      <el-table :data="tableData" border style="margin-top: 20px">
        <el-table-column prop="questionText" label="题目内容" />
        <el-table-column prop="correctAnswer" label="正确答案" width="100" align="center" />
        <el-table-column label="操作" width="150" align="center">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑题目' : '新增题目'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="题干"><el-input v-model="form.questionText" type="textarea" /></el-form-item>
        <el-form-item label="选项A"><el-input v-model="form.optionA" /></el-form-item>
        <el-form-item label="选项B"><el-input v-model="form.optionB" /></el-form-item>
        <el-form-item label="选项C"><el-input v-model="form.optionC" /></el-form-item>
        <el-form-item label="选项D"><el-input v-model="form.optionD" /></el-form-item>
        <el-form-item label="答案"><el-input v-model="form.correctAnswer" placeholder="如 A" /></el-form-item>
        <el-form-item label="解析"><el-input v-model="form.analysis" type="textarea" /></el-form-item>
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
import { getAdminQuestionPageAPI, addAdminQuestionAPI, updateAdminQuestionAPI, deleteAdminQuestionAPI } from '../../api/index'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const dialogVisible = ref(false)
const form = reactive({ id: null, questionText: '', optionA: '', optionB: '', optionC: '', optionD: '', correctAnswer: '', analysis: '' })

const fetchData = async () => {
  const res = await getAdminQuestionPageAPI({ current: 1, size: 100 })
  tableData.value = res.records
}
onMounted(fetchData)

const handleAdd = () => { Object.assign(form, { id: null, questionText: '' }); dialogVisible.value = true }
const handleEdit = (row) => { Object.assign(form, row); dialogVisible.value = true }

const submitForm = async () => {
  if (form.id) await updateAdminQuestionAPI(form)
  else await addAdminQuestionAPI(form)
  ElMessage.success('操作成功')
  dialogVisible.value = false
  fetchData()
}

const handleDelete = async (id) => {
  await deleteAdminQuestionAPI(id)
  fetchData()
}
</script>