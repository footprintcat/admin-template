<template>
  <!-- 导出 Excel 公共组件 -->
  <el-dialog v-model="dialogVisible" title="导出选项" :width="dialogWidth"
    v-if="props.exportDataFrontend !== undefined || props.exportDataBackend !== undefined">
    <!-- {{ exportConfig }} -->
    <p>前端导出: {{ props.exportDataFrontend !== undefined ? '已配置' : '未配置' }}</p>
    <p>后端导出: {{ props.exportDataBackend !== undefined ? '已配置' : '未配置' }}</p>
    <p style="padding-top: 5px; padding-bottom: 15px;">
      您正在导出数据
    </p>
    <el-form :model="form" label-width="80px">
      <el-form-item label="数据范围" v-if="!props.hideDataSortRadioBox">
        <el-radio-group v-model="exportConfig.dataRange">
          <el-radio :value="'all'">
            全部数据
            <el-text size="small">（约 {{ props.totalCount }} 条）</el-text>
          </el-radio>
          <el-radio :value="'current-page'">
            当前页数据
            <el-text size="small">（约 {{ props.pageQuery.pageSize }} 条）</el-text>

          </el-radio>
          <el-radio :value="'top-n'">
            前
            <!-- exportConfig.dataRangeTopN -->
            <el-input-number v-model="dataRangeTopNInput" type="number" :clearable="true" :min="0"
              :max="props.totalCount" :step="1000" placeholder="N"
              style="width: 150px; text-align: center; margin: auto 5px;" v-if="exportConfig.dataRange === 'top-n'">
            </el-input-number>
            <span v-else>N</span>
            条数据
            <el-text size="small" v-if="exportConfig.dataRange !== 'top-n'">
              （选中以设置）
            </el-text>
            <el-text size="small" v-else>
              （范围 0 ~ {{ props.totalCount }} 条）
            </el-text>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="过滤条件" v-if="!props.hideDataSortRadioBox">
        <el-radio-group v-model="exportConfig.filterType">
          <el-radio :value="'none'">不过滤</el-radio>
          <el-radio :value="'current'">使用当前查询条件</el-radio>
        </el-radio-group>
        TODO
      </el-form-item>
      <el-form-item label="排序" v-if="!props.hideDataSortRadioBox">
        <el-radio-group v-model="exportConfig.sortType">
          <el-radio :value="'none'">不排序</el-radio>
          <el-radio :value="'current'">使用当前列表排序</el-radio>
        </el-radio-group>
        TODO
      </el-form-item>
      <el-form-item label="文件格式">
        <el-radio-group v-model="exportConfig.ext">
          <el-radio v-for="ext in supportExportFormatList" :key="ext" :value="ext">{{ ext }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="导出方式">
        <!-- {{ exportConfig.exportType }} -->
        <el-radio-group v-model="exportConfig.exportType"
          :disabled="!frontendSupportExportFormatList.includes(exportConfig.ext as any) || !backendSupportExportFormatList.includes(exportConfig.ext as any)">
          <el-radio :value="'backend'"
            v-if="props.exportDataBackend !== undefined && backendSupportExportFormatList.includes(exportConfig.ext as any)">
            服务端导出（推荐）
            <el-text size="small">（仅支持 xlsx 格式，建议数据量较大时选择）</el-text>
          </el-radio>
          <el-radio :value="'frontend'"
            v-if="props.exportDataFrontend !== undefined && frontendSupportExportFormatList.includes(exportConfig.ext as any)">
            浏览器直出
            <el-text size="small">（支持多种格式，数据量大时可能有性能问题）</el-text>
          </el-radio>
        </el-radio-group>
      </el-form-item>
      <el-alert v-if="exportInfo" :title="exportInfo.info" :type="exportInfo.type" :closable="false" show-icon />
    </el-form>
    <template #footer>
      <el-text style="float: left;">
        共 {{ props.totalCount }} 条数据，
        <span v-if="exportCount">将导出 {{ exportCount }} 条数据</span>
        <span v-else>请输入需要导出的数据条数</span>
      </el-text>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleExport" :disabled="!exportCount || isExporting" :loading="isExporting">
          导出
        </el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-else v-model="dialogVisible" title="导出选项" width="700px">
    暂不支持导出
  </el-dialog>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import * as xlsx from 'xlsx'
import { downloadFile } from '@/utils/file_download_utils'
import type { ExportConfig, ExportConfig_ExportType, ExportResult } from './types'
import type { RequestParam, SortItemWithLabel } from '../types/request-param'
import type { ApiCommonReturnType } from '@/utils/api'
import { backendSupportExportFormatList, frontendSupportExportFormatList, possibleSupportExportFormatList, type SupportExportFormats } from './support-export-formats'

// docs: https://cn.vuejs.org/guide/components/v-model
const dialogVisible = defineModel<boolean>({ default: false })

interface Props {
  totalCount: number
  pageQuery: {
    pageIndex: number
    pageSize: number
  }
  sortList: Array<SortItemWithLabel>
  getSearchFormParams: () => Record<string, any> | undefined
  hideDataRangeRadioBox?: boolean
  hideDataSortRadioBox?: boolean

  // 上级组件限制 以下两个参数至少传入1项
  exportDataFrontend?: (requestParams: RequestParam) => Promise<ApiCommonReturnType<ExportResult>>
  exportDataBackend?: (requestParams: RequestParam) => Promise<void>
}

const props = withDefaults(defineProps<Props>(), {
  hideDataRangeRadioBox: false,
  hideDataSortRadioBox: false,
})

const form = reactive({}) // 弹窗字段的值

// 导出 Excel 弹窗
const exportFormVisible = ref<boolean>(false)

// 支持的导出格式
const supportExportFormatList = computed<Array<SupportExportFormats>>(() => {
  if (!props.exportDataFrontend) {
    return backendSupportExportFormatList
  } else if (!props.exportDataBackend) {
    return frontendSupportExportFormatList
  } else {
    return possibleSupportExportFormatList
  }
})

const exportTypeInput = ref<ExportConfig_ExportType>(props.exportDataBackend ? 'backend' : 'frontend')
const exportConfig = ref<ExportConfig>({
  dataRange: 'all', // 导出时是否携带查询条件
  dataRangeTopN: null,
  sortType: 'none',
  filterType: 'none',
  ext: 'xlsx', // 所选导出格式
  exportType: computed<ExportConfig_ExportType>({
    get: (): ExportConfig_ExportType => {
      // 假定: 前后端一定至少有一个支持导出
      const canFrontendExport: boolean = props.exportDataFrontend !== undefined && frontendSupportExportFormatList.includes(exportConfig.value.ext as any)
      const canBackendExport: boolean = props.exportDataBackend !== undefined && backendSupportExportFormatList.includes(exportConfig.value.ext as any)
      // console.log('canExport', 'frontend:', canFrontendExport, 'backend:', canBackendExport)

      if (!canFrontendExport) {
        if (exportTypeInput.value === 'frontend') {
          return 'backend'
        }
      } else if (!canBackendExport) {
        if (exportTypeInput.value === 'backend') {
          return 'frontend'
        }
      }
      return exportTypeInput.value
    },
    set: (val: ExportConfig_ExportType) => {
      exportTypeInput.value = val
    },
  }),
})

let _topN: number | null = 2000 // null
watch(
  () => exportConfig.value.dataRange,
  (range, old) => {
    if (old === range) {
      return
    }
    if (range === 'top-n') {
      exportConfig.value.dataRangeTopN = _topN
    } else if (old === 'top-n') {
      _topN = exportConfig.value.dataRangeTopN
      exportConfig.value.dataRangeTopN = null
    }
  },
  { immediate: true },
)

const dataRangeTopNInput = computed<ExportConfig['dataRangeTopN']>({
  get: () => {
    return exportConfig.value.dataRange === 'top-n' ? exportConfig.value.dataRangeTopN : null
  },
  set: (val) => {
    exportConfig.value.dataRangeTopN = val
  },
})

const exportCount = computed<number | null>(() => {
  switch (exportConfig.value.dataRange) {
    case 'all':
      return props.totalCount
    case 'current-page':
      return props.pageQuery.pageSize
    case 'top-n':
      return dataRangeTopNInput.value
    default:
      return null
  }
})

const exportInfo = computed<null | { info: string; type: 'info' | 'warning' }>(() => {
  if (['xlsx', 'xls'].includes(exportConfig.value.ext)) {
    return null
  }
  let info: string = '建议选择 xlsx 或 xls 格式'
  let type: 'info' | 'warning' = 'info'
  if (['csv'].includes(exportConfig.value.ext)) {
    // 不推荐的导出格式
    info = '该格式数据列易错位，且易出现编码问题，' + info
    type = 'warning'
  } else if (['rtf'].includes(exportConfig.value.ext)) {
    // 不推荐的导出格式
    info = '该格式易出现编码问题，' + info
    type = 'warning'
  } else if (['json'].includes(exportConfig.value.ext)) {
    info = '该格式方便程序读取，若您不了解该格式，建议选择其他格式进行导出'
    type = 'warning'
  }
  return { info, type }
})

const isExporting = ref<boolean>(false)
// 点击确认后，处理导出数据
async function handleExport() {
  const params = props.getSearchFormParams()
  const requestParam: RequestParam = {
    params: exportConfig.value.dataRange ? { ...params } : {},
    sort: props.sortList.map(item => ({
      field: item.field,
      order: item.order,
    })),
    pageQuery: {
      pageIndex: 1,
      pageSize: -1, // -1 表示导出全部数据
    },
  }

  console.log('导出请求参数', requestParam)

  isExporting.value = true
  try {
    if (props.exportDataBackend && exportConfig.value.exportType === 'backend') {
      // 后端导出（仅支持 xlsx）
      console.log('后端导出')
      props.exportDataBackend(requestParam)
      dialogVisible.value = false
    } else if (props.exportDataFrontend) {
      // 前端导出
      console.log('前端导出')
      const result = await props.exportDataFrontend(requestParam)
      console.log('导出结果', result)
      if (!result.isSuccess) {
        ElMessage.error({ message: '导出失败', grouping: true })
        return
      }
      // 调用 dialog 组件的 exportFile 方法进行文件下载
      frontendExportFile(result.data)
      dialogVisible.value = false
    } else {
      ElMessage.warning({ message: '导出功能未配置', grouping: true })
    }
  } catch (error) {
    console.error('导出失败', error)
    ElMessage.error({ message: '导出失败，网络连接异常', grouping: true })
  } finally {
    isExporting.value = false
  }
}

// 将获取到的数据进行导出
function frontendExportFile(result: ExportResult): void {
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
    xlsx.utils.book_append_sheet(workbook, worksheet, result.sheetName || 'Sheet1')
    xlsx.writeFile(workbook, _fileName)
  }

  ElMessage.success({ message: '导出成功' })
  exportFormVisible.value = false
}

const dialogWidth = computed<string>(() => {
  if (exportConfig.value.dataRange === 'top-n') {
    return `900px` // 选中 [前N条数据] 选项时，弹窗需要宽一点
  }
  return `700px`
})
</script>
