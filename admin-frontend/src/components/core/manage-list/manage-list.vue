<template>
  <div class="manage-list-wrapper " :class="[props.tableFillHeight ? 'fill-height' : '']">
    <!-- 顶部查询条件 -->
    <div class="top-container">
      1
    </div>

    <div class="table-container">
      <el-table height="100%">

      </el-table>
    </div>

    <!-- 底部元素组件 -->
    <div class="footer-container">
      2
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  tableFillHeight: {
    type: Boolean,
    required: false,
    default: true,
  },
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
}

.top-container {
  background-color: #ffffff;
  padding: 16px;
  margin-bottom: 16px;
  border-radius: 2px;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
}

.table-container {
  min-height: 150px;
  flex-grow: 1;
  background-color: #ffffff;
  border-radius: 2px;
  /* box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); */
  overflow: auto;
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
