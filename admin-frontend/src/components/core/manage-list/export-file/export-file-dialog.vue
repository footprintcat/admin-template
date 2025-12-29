<template>
  <!-- 导出 Excel 公共组件 -->
  <el-dialog v-model="dialogVisible" title="导出选项" width="680px">
    <!-- {{ exportConfig }} -->
    <el-form :model="form" label-width="80px">
      <el-form-item label="数据范围" v-if="!props.hideWithFilterRadioBox">
        <el-radio-group v-model="exportConfig.withFilter">
          <el-radio :value="false">全部数据</el-radio>
          <el-radio :value="true">当前查询条件下全部数据</el-radio>
          <!-- TODO -->
          <el-radio :value="2">当前页数据 (TODO)</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="文件格式">
        <el-radio-group v-model="exportConfig.ext">
          <el-radio v-for="ext in supportExportFormatList" :key="ext" :value="ext">{{ ext }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-alert v-if="exportInfo" :title="exportInfo.info" :type="exportInfo.type" :closable="false" show-icon />
    </el-form>
    <p>
      TODO:
      将导出 x 条数据
    </p>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleExport">导出</el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref, toRaw } from 'vue'
import { ElMessage } from 'element-plus'
import * as xlsx from 'xlsx'
import { downloadFile } from '@/utils/file_download_utils'
import type { ExportConfig, ExportResult } from './types'

const emit = defineEmits(['update:modelValue', 'handleExport'])

const props = defineProps({
  modelValue: {
    type: Boolean,
    required: true,
  },
  hideWithFilterRadioBox: {
    type: Boolean,
    default: false,
  },
})

const dialogVisible = computed({
  get() {
    return props.modelValue
  },
  set(value) {
    emit('update:modelValue', value)
  },
})

const form = reactive({}) // 弹窗字段的值

// 导出 Excel 弹窗
const exportFormVisible = ref<boolean>(false)
const supportExportFormatList = ref(['xlsx', 'xls', 'csv', 'html', 'txt', 'json', 'rtf'] as Array<string>) // 支持的导出格式
const exportConfig = ref<ExportConfig>({
  withFilter: true, // 导出时是否携带查询条件
  ext: 'xlsx', // 所选导出格式
})
const exportInfo = computed<null | { info: string; type: 'info' | 'warning' }>(() => {
  if (['xlsx', 'xls'].includes(exportConfig.value.ext)) {
    return null
  }
  let info: string = '建议选择 xlsx 或 xls 格式'
  let type: 'info' | 'warning' = 'info'
  if (['rtf'].includes(exportConfig.value.ext)) {
    // 不推荐的导出格式
    info = '该格式易出现编码问题，' + info
    type = 'warning'
  } else if (['json'].includes(exportConfig.value.ext)) {
    info = '该格式方便程序读取，若您不了解该格式，建议选择其他格式进行导出'
    type = 'warning'
  }
  return { info, type }
})


onMounted(() => {

})

// 点击确认后，将参数返回给负组件，获取数据
function handleExport() {
  const config: ExportConfig = toRaw(exportConfig.value)
  emit('handleExport', config)
}

defineExpose({
  // 将获取到的数据进行导出
  exportFile(result: ExportResult): void {
    console.log('导出文件 -> exportFile', result)

    const excelList: string[][] = [
      result.head.showHead ? result.head.columns.map(column => column.fieldName) : [],
      ...result.data.map(row => result.head.columns.map(column => row[column.field])),
    ]

    // 生成文件
    const _fileName: string = `${result.fileName || '数据导出'}.${exportConfig.value.ext || 'xlsx'}`
    if (exportConfig.value.ext === 'json') {
      const text = JSON.stringify(excelList)
      downloadFile(text, _fileName, 'application/json;charset=utf-8')
    } else if (exportConfig.value.ext === 'txt') {
      const text = excelList.map(row => row.join('\t')).join('\n')
      downloadFile(text, _fileName, 'text/plain;charset=utf-8')
    } else {
      const worksheet = xlsx.utils.aoa_to_sheet(excelList)
      const workbook = xlsx.utils.book_new()
      worksheet['!cols'] = result.head.columns.map(column => ({ wch: column.columnWidth })) // 指定列宽
      xlsx.utils.book_append_sheet(workbook, worksheet, result.fileName || 'Sheet1')
      xlsx.writeFile(workbook, _fileName)
    }

    ElMessage.success({ message: '导出成功' })
    exportFormVisible.value = false
  },
})
</script>
