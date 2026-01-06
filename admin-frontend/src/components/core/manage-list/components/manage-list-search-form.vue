<template>
  <el-form :inline="true" :label-position="props.searchFormLabelPosition">
    <el-form-item v-for="(searchInput, index) in props.searchInputList" :key="index" :label="searchInput.label"
      :style="{ width: `${calcItemWidth(searchInput.columnGap)}px` }">
      <template v-if="searchInput.type === 'text'">
        <el-input v-model="params[searchInput.field]" clearable
          :placeholder="searchInput.placeHolder || `请输入${searchInput.label}`" />
      </template>
      <template v-else-if="searchInput.type === 'dropdown'">
        <el-select v-model="params[searchInput.field]" clearable filterable :multiple="searchInput.multipleSelection"
          collapse-tags collapse-tags-tooltip :placeholder="searchInput.placeHolder || `请输入${searchInput.label}`">
          <el-option v-for="item in tempOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </template>
      <template v-else-if="searchInput.type === 'datetime'">
        <el-date-picker v-model="params[searchInput.field]" clearable
          :placeholder="searchInput.placeHolder || `请选择${searchInput.label}`"
          :format="getElementPlusDateTimePickerFormat(searchInput.showTimePart)"
          :value-format="getElementPlusDateTimePickerValueFormat(searchInput.valueFormat)" />
      </template>
      <template v-else-if="searchInput.type === 'datetime-range'">
        <el-date-picker v-model="params[searchInput.field]" clearable type="datetimerange"
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
import { getElementPlusDateTimePickerFormat, getElementPlusDateTimePickerValueFormat } from '../scripts/datetime-search'
import type { SearchInputList } from '../types/search-input'

interface Props {
  /** 搜索输入框label */
  searchFormLabelPosition?: 'top' | 'left'
  searchInputList?: SearchInputList
  /** 初始参数值 */
  extraInitialParams?: Record<string, unknown>
}

const props = withDefaults(defineProps<Props>(), {
  searchFormLabelPosition: 'top',
  searchInputList: () => [] satisfies SearchInputList,
  extraInitialParams: () => ({}),
})

const emit = defineEmits<{
  'update:params': [params: Record<string, unknown>]
  'change': [params: Record<string, unknown>]
}>()

// eslint-disable-next-line @typescript-eslint/no-explicit-any
const params = ref<Record<string, any>>({})

// 初始化参数结构
watch(() => props.searchInputList, (newList) => {
  resetParams()
}, { immediate: true })

function resetParams() {
  // 与查询条件 无关的 查询参数初值
  const newParams = JSON.parse(JSON.stringify(props.extraInitialParams))
  for (const searchInput of props.searchInputList) {
    if (searchInput.field in newParams) {
      // 已经有默认值了
      continue
    }

    // 与查询条件 有关的 查询参数初值
    if (searchInput.initialValue !== undefined) {
      newParams[searchInput.field] = typeof searchInput.initialValue === 'function'
        ? searchInput.initialValue()
        : searchInput.initialValue
      continue
    }

    // 根据类型设置初始值
    if (searchInput.type === 'dropdown' && searchInput.multipleSelection) {
      newParams[searchInput.field] = []
    } else if (searchInput.type === 'datetime-range') {
      newParams[searchInput.field] = []
    } else {
      newParams[searchInput.field] = null
    }
  }
  params.value = newParams
  console.log('params', newParams)
}

// ref 变化时，emit 通知父组件
watch(params, (newParams) => {
  const paramsRaw = toRaw(newParams)
  emit('update:params', paramsRaw)
  emit('change', paramsRaw)
}, {
  deep: true,
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

// TODO
/** @deprecated */
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

defineExpose({
  params,
  getParams: () => toRaw(params.value),
  getParamsRef: () => params,
  resetParams: resetParams,
})
</script>
