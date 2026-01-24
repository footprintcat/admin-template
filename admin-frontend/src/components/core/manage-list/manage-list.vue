<template>
  <div class="manage-list-wrapper" :class="[props.tableFillHeight ? 'fill-height' : '']">
    <!-- 顶部查询条件 -->
    <div class="top-container container-style">
      <manage-list-search-form ref="manageListSearchFormRef" :search-form-label-position="props.searchFormLabelPosition"
        :search-input-list="props.searchInputList" :extra-initial-params="props.extraInitialParams"
        @change="handleParamsUpdate" />
      <div>
        <el-button type="primary" :icon="Search" @click="handleFetchData({ gotoFirstPage: true })"
          :loading="props.allowParallelFetch ? false : isLoading">
          查询
        </el-button>
        <el-button type="danger" plain :icon="Delete" @click="handleResetParams">
          重置查询条件
        </el-button>
        <el-button type="danger" plain :icon="Delete" @click="handleResetFilters" v-if="hasFilterColumn">
          重置排序
        </el-button>
        <!-- type="primary" plain -->
        <el-button type="primary" plain :icon="Download" @click="handleExportFile">
          导出到文件
        </el-button>

        <el-tooltip placement="left" content="刷新">
          <el-button type="default" :icon="RefreshRight" circle style="float: right;"
            :loading="props.allowParallelFetch ? false : isLoading"
            @click="handleFetchData({ gotoFirstPage: false })" />
        </el-tooltip>
      </div>
    </div>

    <div class="table-container container-style" :class="[props.tableFillHeight ? 'fill-height' : '']"
      v-loading="isLoading" element-loading-text="请稍候...">
      <!-- el-table 设置 height="100%" 后 前后不能再添加其他元素 否则高度会被无限撑大 -->
      <!-- 如果要添加其他元素，可以设置 style="height: 100%;" -->
      <el-table ref="manageListTableRef" height="100%" :data="tableData" @sort-change="handleTableSortChange"
        :border="true">
        <slot name="customTableColumn">
          <!-- <el-table-column prop="id" label="用户id"></el-table-column> -->
          <!-- <el-table-column prop="username" label="用户姓名"></el-table-column> -->
          <template v-for="column in props.tableColumnList" :key="column.field">
            <el-table-column :prop="column.field" :label="column.label" :width="column.width"
              :min-width="column.minWidth" :align="column.align" :header-align="column.headerAlign"
              :sortable="column.sortable ? 'custom' : false" :default-sort="column.defaultSort"
              :sort-orders="['ascending', 'descending', null]" resizable
              :show-overflow-tooltip="column.showOverflowTooltip">
              <!-- 字段翻译 -->
              <template #default="scope" v-if="column.transformMap">
                <!-- {{ Object.keys(scope) }} -->
                {{ column.transformMap[scope.row[column.field]] || column.transformDefaultValue }}
              </template>
            </el-table-column>
          </template>
        </slot>
        <template #empty>
          暂无符合条件的数据
        </template>
      </el-table>
    </div>

    <!-- 底部元素组件 -->
    <div class="footer-container container-style">
      <!-- 排序信息显示 -->
      <div class="sort-info">
        <el-text>
          <template v-if="sortList.length === 0">
            暂无排序
          </template>
          <template v-else>
            按{{
              sortList
                .map(item => ` [${[fieldNameMap[item.field]]}] ${item.order === 'ascending' ? '升序' : '降序'}`)
                .join('、')
            }}排序
          </template>
        </el-text>
        <el-link v-if="!isDefaultFilters" type="primary" style="vertical-align: unset;" @click="handleResetFilters">
          重置排序
        </el-link>
      </div>
      <!-- 分页组件 -->
      <el-pagination v-model:current-page="pageQuery.pageIndex" v-model:page-size="pageQuery.pageSize"
        :page-sizes="props.pageSizeOptions" layout="total, sizes, prev, pager, next, jumper" :total="total"
        @size-change="handlePageSizeChange" @current-change="handleCurrentChange" />
      <!-- :disabled="isLoading" -->
    </div>

    <!-- 导出文件弹窗 -->
    <export-file-dialog v-model="showExportFileDialog" />
  </div>
</template>

<script setup lang="ts">
import { ElMessage, type ElTable, type TableColumnCtx } from 'element-plus'
import { Delete, Download, RefreshRight, Search } from '@element-plus/icons-vue'
import type { ApiCommonReturnType } from '@/utils/api'
import type { ManageListResponse } from '@/types/backend/common/manage-list-response'
import ManageListSearchForm from './components/manage-list-search-form.vue'
import ExportFileDialog from './export-file/export-file-dialog.vue'
import type { RequestParam, SortItemWithLabel } from './types/request-param'
import type { SearchInputList } from './types/search-input'
import type { TableColumnList } from './types/table-column'

interface Props {
  /**
   * 表格是否撑满容器
   */
  tableFillHeight?: boolean
  /**
   * 是否展示导出按钮
   */
  showExportButton?: boolean
  /**
   * 搜索输入框label
   */
  searchFormLabelPosition?: 'top' | 'left'
  /**
   * 查询条件
   */
  searchInputList: SearchInputList
  /**
   * 表格列
   */
  tableColumnList: TableColumnList
  /**
   * 非查询条件的额外默认值
   */
  extraInitialParams?: Record<string, unknown>
  fetchData: (requestParams: RequestParam) => Promise<ApiCommonReturnType<ManageListResponse<unknown>>>
  /**
   * 组件挂载时是否拉取数据
   */
  fetchDataOnMounted?: boolean
  /**
   * 是否允许并行请求
   * - true: 点击查询按钮时，查询按钮不会显示 loading 状态，请求中允许再次点击
   * - false: 点击查询按钮时，查询按钮呈现 loading 状态，此时再次点击不触发 fetchData
   */
  allowParallelFetch?: boolean
  /**
   * 每页条数选项
   */
  pageSizeOptions?: number[]
  /**
   * 调试模式
   * 启用后将会打印更多日志信息
   */
  debug?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  tableFillHeight: true,
  showExportButton: true,
  searchFormLabelPosition: 'top',
  searchInputList: () => [] satisfies SearchInputList,
  fetchDataOnMounted: true,
  allowParallelFetch: false,
  pageSizeOptions: () => [5, 10, 15, 20, 25, 30, 50, 100, 150, 200, 250, 500],
  debug: false,
})

// 组件 ref
const manageListTableRef = ref<InstanceType<typeof ElTable>>()
const manageListSearchFormRef = ref<InstanceType<typeof ManageListSearchForm>>()

// 排序信息
const sortList = ref<Array<SortItemWithLabel>>([])
// 初始排序状态
const initialSortList = ref<Array<SortItemWithLabel>>([])

// 查询条件
function handleParamsUpdate(params: Record<string, unknown>) {
  // console.log('paramsUpdate', params)
}

function handleResetParams() {
  manageListSearchFormRef.value?.resetParams()
  ElMessage.info({
    message: '已重置查询条件',
    grouping: true,
  })

  // 重新查询数据
  handleFetchData({ gotoFirstPage: true })
}

// 表格排序
const hasFilterColumn = computed<boolean>(() => {
  return props.tableColumnList.some(column => column.sortable)
})

// 筛选条件 field -> label 映射
const fieldNameMap = computed<Record<string, string>>(() => {
  const map: Record<string, string> = {}
  props.tableColumnList.forEach(column => {
    map[column.field] = column.label
  })
  return map
})

// 当前排序条件是否是默认排序
const isDefaultFilters = computed(() => {
  return JSON.stringify(sortList.value) === JSON.stringify(initialSortList.value)
})

function handleResetFilters() {
  // 重置排序信息到初始状态
  sortList.value = [...initialSortList.value]

  // 更新表格排序状态
  updateTableSortState(initialSortList.value)

  ElMessage.info({
    message: '已重置排序到初始状态',
    grouping: true,
  })

  // 重新查询数据
  handleFetchData({ gotoFirstPage: true })
}

// 手动设置表格列的排序状态
function updateTableSortState(sortItems: SortItemWithLabel[]) {
  const columns = manageListTableRef.value?.columns || []
  columns.forEach(col => {
    if (col.property) {
      const sortItem = sortItems.find(item => item.field === col.property)
      col.order = sortItem ? sortItem.order : null
    }
  })
}

function handleTableSortChange(data: { column: TableColumnCtx, prop: string, order: 'ascending' | 'descending' | null }) {
  // console.log('table sort change', data)

  // 多列排序逻辑
  const { column, prop, order } = data
  const sortField: string = prop
  const sortOrder: 'ascending' | 'descending' | null = order
  const label: string = column.label

  // 查找是否已存在该字段的排序
  const existingIndex = sortList.value.findIndex(item => item.field === sortField)

  // 更新 sortList
  if (existingIndex !== -1) {
    // 更新现有排序
    if (sortOrder === null) {
      // 移除排序
      sortList.value.splice(existingIndex, 1)
    } else {
      // 更新排序方向
      sortList.value[existingIndex]!.order = sortOrder
    }
  } else if (sortOrder !== null) {
    // 添加新的排序
    sortList.value.push({ field: sortField, order: sortOrder, label: label })
  }

  // 更新表格排序状态
  updateTableSortState(sortList.value)

  // console.log('sortList', toRaw(sortList.value))
  console.log('sortList', JSON.stringify(sortList.value))

  handleFetchData({ gotoFirstPage: true })
}



// 请求状态
const fetchingCount = ref<number>(0)
const isLoading = computed<boolean>(() => fetchingCount.value > 0)

// 分页信息
const pageQuery = ref<{ pageIndex: number; pageSize: number }>({
  pageIndex: 1, // 从 1 开始
  pageSize: 10,
})

// 总条数
const total = ref<number>(0)

// 表格数据
const tableData = ref<Array<unknown>>([])

async function handleFetchData({
  gotoFirstPage,
  pageIndexReEvaluateCount,
}: {
  gotoFirstPage: boolean
  pageIndexReEvaluateCount?: number
}) {
  if (gotoFirstPage) {
    pageQuery.value.pageIndex = 1
  }
  fetchingCount.value++

  if (props.debug) {
    // console.log('')
    console.log(new Date().toLocaleString())
  }
  console.log('==========', 'fetchData start', '==========')
  const params = manageListSearchFormRef.value?.getParams()
  // console.log('param', param)
  const requestParam: RequestParam = {
    params: {
      ...params,
    },
    sort: sortList.value.map(item => ({
      field: item.field,
      order: item.order,
    })),
    pageQuery: {
      pageIndex: pageQuery.value.pageIndex,
      pageSize: pageQuery.value.pageSize,
    },
  }
  console.log('request params', requestParam)
  if (props.debug) {
    console.log('\t', 'params', requestParam.params, '\n\t', 'sort', JSON.stringify(requestParam.sort), '\n\t', 'pageQuery', requestParam.pageQuery)
  }
  props.fetchData(requestParam)
    .then(result => {
      try {
        console.log('result', result)
        if (!result.isSuccess) {
          return // 已经弹出全局提示了
        }
        tableData.value = result.data.list
        total.value = result.data.total || 0

        // 如果超出最后一页, 则跳转到最后一页
        const totalPageCount = Math.ceil(total.value / pageQuery.value.pageSize)
        if (pageQuery.value.pageIndex > totalPageCount) {
          console.log('当前页', pageQuery.value.pageIndex, '总页数', totalPageCount, '当前页 > 总页数，将跳转到最后一页')
          pageQuery.value.pageIndex = totalPageCount
          // 重新请求数据
          if (pageIndexReEvaluateCount && pageIndexReEvaluateCount >= 3) {
            console.error('manage-list fetchData 超出最后一页 重试已达上限，停止重试')
            return
          }
          handleFetchData({ gotoFirstPage: false, pageIndexReEvaluateCount: (pageIndexReEvaluateCount || 0) + 1 })
        }
      } catch (error) {
        console.error('查询失败，前端处理逻辑错误', error)
        ElMessage.error({
          message: '查询失败，前端处理逻辑错误',
          grouping: true,
        })
      }
    })
    .catch((error) => {
      console.error('manage-list fetchData 查询失败', error)
      ElMessage.error({
        message: '查询失败，网络连接异常',
        grouping: true,
      })
    })
    .finally(() => {
      console.log('==========', 'fetchData finish', '==========')
      if (props.debug) {
        console.log('')
      }
      fetchingCount.value--
    })
}

onMounted(() => {
  if (props.debug) {
    // 获取当前组件实例
    const instance = getCurrentInstance()
    if (instance && instance.parent) {
      // 获取当前组件名称
      const componentName: string = instance.type.__name || ''
      // 获取父组件名称
      const parent = instance.parent
      const parentName: string = parent.type.__name || ''

      const consoleLogStyle = 'background-color: #ffcf77; padding: 2px 5px;'
      console.log('%c[debug]', consoleLogStyle, '[', parentName, '] 组件中的 [', componentName, '] 组件已开启 debug 模式')
    }
  }

  // 初始化初始排序状态
  initialSortList.value = props.tableColumnList
    .filter(column => column.defaultSort)
    .map(column => ({
      field: column.field,
      order: column.defaultSort || null,
      label: column.label,
    }))

  // 应用初始排序
  sortList.value = [...initialSortList.value]

  // 手动设置表格列的初始排序状态
  nextTick(() => {
    updateTableSortState(initialSortList.value)
    handleFetchData({ gotoFirstPage: false })
  })
})

// 分页事件处理
function handlePageSizeChange(newSize: number) {
  pageQuery.value.pageSize = newSize
  pageQuery.value.pageIndex = 1
  handleFetchData({ gotoFirstPage: true })
}

function handleCurrentChange(newPage: number) {
  pageQuery.value.pageIndex = newPage
  handleFetchData({ gotoFirstPage: false })
}

// 文件导出
const showExportFileDialog = ref<boolean>(false)
function handleExportFile() {
  showExportFileDialog.value = true
}


// 校验
onMounted(() => {
  // 校验 searchInputList 中的 field 是否重复
  const fieldSet = new Set<string>()
  for (const input of props.searchInputList) {
    if (fieldSet.has(input.field)) {
      console.error(`manage-list 组件 searchInputList 中存在重复的 field: ${input.field}`)
    } else {
      fieldSet.add(input.field)
    }
  }

  /*
  // 校验 tableColumnList 中的 field 是否重复
  fieldSet.clear()
  for (const column of props.tableColumnList) {
    if (fieldSet.has(column.field)) {
      console.error(`manage-list 组件 tableColumnList 中存在重复的 field: ${column.field}`)
    } else {
      fieldSet.add(column.field)
    }
  }
  */
})
</script>

<style scoped>
.manage-list-wrapper {
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  position: relative;

  --footer-vertical-padding: 16px;
  --footer-horizontal-padding: 32px;
}

.manage-list-wrapper.fill-height {
  height: 100%;
  /* 重要：避免 flex 项目无限扩展 */
  /* 不添加 min-height 会导致 网页高度缩小时，表格高度不会缩小 */
  min-height: 0;
}

.container-style {
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
  box-shadow: 0px 0px 10px 0px hsla(0, 0%, 0%, 0.05);
  border-radius: 3px;
}

.top-container {
  background-color: #ffffff;
  padding: 16px 20px;
  margin-bottom: 16px;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
}

.table-container {
  min-height: 150px;
  background-color: #ffffff;
  overflow: auto;
}

.table-container.fill-height {
  flex-grow: 1;
}

.footer-container {
  margin-top: 16px;
  background-color: #ffffff;
  border-top: 1px solid #e8e8e8;
  padding: var(--footer-vertical-padding) var(--footer-horizontal-padding);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 隐藏 table 下的分隔线 */
:deep(.el-table--fit .el-table__inner-wrapper:before) {
  display: none;
}
</style>
