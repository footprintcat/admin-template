<template>
  <div class="manage-list-wrapper" :class="[props.tableFillHeight ? 'fill-height' : '']">
    <!-- 顶部查询条件 -->
    <div class="top-container">
      <manage-list-search-form :search-form-label-position="props.searchFormLabelPosition"
        :search-input-list="props.searchInputList" />
      <div>
        <el-button type="primary" :icon="Search">
          查询
        </el-button>
        <el-button type="primary" plain :icon="Download">
          导出到文件
        </el-button>
        <el-button type="default" :icon="RefreshRight" circle style="float: right;"></el-button>
      </div>
    </div>

    <div class="table-container" :class="[props.tableFillHeight ? 'fill-height' : '']">
      <!-- el-table 设置 height="100%" 后 前后不能再添加其他元素 否则高度会被无限撑大 -->
      <!-- 如果要添加其他元素，可以设置 style="height: 100%;" -->
      <el-table ref="manageListTableRef" height="100%">

      </el-table>
    </div>

    <!-- 底部元素组件 -->
    <div class="footer-container">
      2
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ElTable } from 'element-plus'
import { Download, RefreshRight, Search } from '@element-plus/icons-vue'
import ManageListSearchForm from './components/manage-list-search-form.vue'
import type { SearchInputList } from './types/search-input'

interface Props {
  tableFillHeight?: boolean
  showExportButton?: boolean
  /** 搜索输入框label */
  searchFormLabelPosition?: 'top' | 'left'
  searchInputList?: SearchInputList
}

const props = withDefaults(defineProps<Props>(), {
  tableFillHeight: true,
  showExportButton: true,
  searchFormLabelPosition: 'top',
  searchInputList: () => [] satisfies SearchInputList,
})

const manageListTableRef = ref<InstanceType<typeof ElTable>>()
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

.top-container {
  background-color: #ffffff;
  padding: 16px 20px;
  margin-bottom: 16px;
  border-radius: 2px;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
}

.table-container {
  min-height: 150px;
  background-color: #ffffff;
  border-radius: 2px;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
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
