<template>
  <el-form :inline="true" :label-position="props.searchFormLabelPosition">
    <el-form-item v-for="(searchInput, index) in props.searchInputList" :key="index" :label="searchInput.label"
      :style="{ width: `${calcItemWidth(searchInput.columnGap)}px` }">
      <template v-if="searchInput.type === 'text'">
        <el-input v-model="tempVModel" clearable :placeholder="searchInput.placeHolder || `请输入${searchInput.label}`" />
      </template>
      <template v-else-if="searchInput.type === 'dropdown'">
        <el-select v-model="tempVModel" clearable filterable :multiple="searchInput.multipleSelection" collapse-tags
          collapse-tags-tooltip :placeholder="searchInput.placeHolder || `请输入${searchInput.label}`">
          <el-option v-for="item in tempOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </template>
      <template v-else-if="searchInput.type === 'datetime'">
        <el-date-picker v-model="tempVModel" clearable
          :placeholder="searchInput.placeHolder || `请选择${searchInput.label}`" />
      </template>
      <template v-else-if="searchInput.type === 'datetime-range'">
        <el-date-picker v-model="tempVModel" clearable type="datetimerange"
          :start-placeholder="searchInput.placeHolderStart || `请选择开始时间`"
          :end-placeholder="searchInput.placeHolderEnd || `请选择结束时间`" />
      </template>
      <template v-else>
        <el-input placeholder="未知的搜索条件类型" disabled />
      </template>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import type { SearchInputList } from '../types/search-input'

interface Props {
  /** 搜索输入框label */
  searchFormLabelPosition?: 'top' | 'left'
  searchInputList?: SearchInputList
}

const props = withDefaults(defineProps<Props>(), {
  searchFormLabelPosition: 'top',
  searchInputList: () => [] satisfies SearchInputList,
})

// 搜索条件中，一个单位宽度是多宽
const elFormItemWidth = 200 // px
// element plus 两个 form item 之间的间距
const elFormItemGapSpace = 32 // px

function calcItemWidth(columnGap?: number) {
  return columnGap
    ? (elFormItemWidth * columnGap) + (elFormItemGapSpace * (columnGap - 1))
    : elFormItemWidth
}

const tempVModel = ref()
const tempOptions = [
  {
    value: 'Option1',
    label: 'Option1',
  },
  {
    value: 'Option2',
    label: 'Option2',
  },
  {
    value: 'Option3',
    label: 'Option3',
  },
  {
    value: 'Option4',
    label: 'Option4',
  },
  {
    value: 'Option5',
    label: 'Option5',
  },
]
</script>
