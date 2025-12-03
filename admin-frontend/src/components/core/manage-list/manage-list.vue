<template>
  <div class="manage-list-wrapper" :class="[props.tableFillHeight ? 'fill-height' : '']">
    <!-- 顶部查询条件 -->
    <div class="top-container container-style">
      <manage-list-search-form ref="manageListSearchFormRef" :search-form-label-position="props.searchFormLabelPosition"
        :search-input-list="props.searchInputList" :extra-initial-params="props.extraInitialParams"
        @change="handleParamsUpdate" />
      <div>
        <el-button type="primary" :icon="Search" @click="handleFetchData(true)"
          :loading="props.allowParallelFetch ? false : isLoading">
          查询
        </el-button>
        <el-button type="danger" plain :icon="Delete" @click="handleResetParams">
          重置查询条件
        </el-button>
        <!-- type="primary" plain -->
        <el-button type="primary" plain :icon="Download" @click="handleExportFile">
          导出到文件
        </el-button>

        TODO: 排序

        <el-tooltip placement="left" content="刷新">
          <el-button type="default" :icon="RefreshRight" circle style="float: right;" @click="handleFetchData(false)" />
        </el-tooltip>
      </div>
    </div>

    <div class="table-container container-style" :class="[props.tableFillHeight ? 'fill-height' : '']"
      v-loading="isLoading" element-loading-text="请稍候...">
      <!-- el-table 设置 height="100%" 后 前后不能再添加其他元素 否则高度会被无限撑大 -->
      <!-- 如果要添加其他元素，可以设置 style="height: 100%;" -->
      <el-table ref="manageListTableRef" height="100%" :data="tableData">
        <slot name="customTableColumn">
          <el-table-column prop="id" label="用户id"></el-table-column>
          <el-table-column prop="username" label="用户姓名"></el-table-column>
        </slot>
      </el-table>
    </div>

    <!-- 底部元素组件 -->
    <div class="footer-container container-style">
      2
    </div>

    <!-- 导出文件弹窗 -->
    <export-file-dialog v-model="showExportFileDialog" />
  </div>
</template>

<script setup lang="ts">
import { ElMessage, type ElTable } from 'element-plus'
import { Delete, Download, RefreshRight, Search } from '@element-plus/icons-vue'
import ManageListSearchForm from './components/manage-list-search-form.vue'
import ExportFileDialog from './export-file/export-file-dialog.vue'
import type { SearchInputList } from './types/search-input'

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
  searchInputList?: SearchInputList
  /**
   * 非查询条件的额外默认值
   */
  extraInitialParams?: Record<string, unknown>
  fetchData: (params: unknown) => Promise<Array<unknown>>
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
  debug: false,
})

// 组件 ref
const manageListTableRef = ref<InstanceType<typeof ElTable>>()
const manageListSearchFormRef = ref<InstanceType<typeof ManageListSearchForm>>()

// 查询条件
function handleParamsUpdate(params: Record<string, unknown>) {
  if (props.debug) {
    console.log('[debug] paramsUpdate', params)
  }
}

function handleResetParams() {
  manageListSearchFormRef.value?.resetParams()
  ElMessage.info({
    message: '已重置查询条件',
    grouping: true,
  })
}

// 请求状态
const fetchingCount = ref<number>(0)
const isLoading = computed<boolean>(() => fetchingCount.value > 0)

// 表格数据
const tableData = ref<Array<unknown>>([])

async function handleFetchData(gotoFirstPage: boolean) {
  if (gotoFirstPage) {
    // TODO 回到第1页
  }
  fetchingCount.value++

  if (props.debug) {
    console.log('')
  }
  console.log('==========', 'fetchData start', '==========')
  const param = manageListSearchFormRef.value?.getParams()
  // if (props.debug) {
  //   console.log('[debug] param', param)
  // }
  const requestParam = {
    ...param,
  }
  console.log('request param', param)
  props.fetchData(requestParam)
    .then(result => {
      console.log('result', result)
      tableData.value = result.data.list
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

  // nextTick 是非必要调用，需要
  nextTick(() => {
    handleFetchData(false)
  })
})

// 文件导出
const showExportFileDialog = ref<boolean>(false)
function handleExportFile() {
  showExportFileDialog.value = true
}
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
}

/* 隐藏 table 下的分隔线 */
:deep(.el-table--fit .el-table__inner-wrapper:before) {
  display: none;
}
</style>
