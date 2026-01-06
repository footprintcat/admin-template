<template>
  <!-- class="no-scroll" -->
  <div style="display: flex; flex-direction: column;">

    <page-title title="用户管理" type="single-line">
      <UserFilled />
    </page-title>

    <!-- <page-title title="用户管理" /> -->

    <!--
    <page-title title="用户管理">
      <User />
    </page-title>
    -->

    <!-- 用户管理 -->
    <!-- <el-config-provider size="small"> -->
    <manage-list :search-input-list="searchInputList" :table-column-list="tableColumnList" :fetch-data="fetchData"
      :extra-initial-params="extraInitialParams" :debug="true" />
    <!-- </el-config-provider> -->

    <!--
    <manage-list :search-input-list="searchInputList" :fetch-data="fetchData" >
      <template #customTableColumn>
          <el-table-column prop="id" label="用户id"></el-table-column>
          <el-table-column prop="username" label="用户姓名"></el-table-column>
          <el-table-column prop="username2" label="用户姓名"></el-table-column>
      </ template>
    </manage-list>
    -->

  </div>
</template>

<script setup lang="ts">
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { User, UserFilled } from '@element-plus/icons-vue'
import { systemUserPage } from '@/api/system/user'
import ManageList from '@/components/core/manage-list/manage-list.vue'
import type { RequestParam } from '@/components/core/manage-list/types/request-param'
import type { SearchInputList } from '@/components/core/manage-list/types/search-input'
import type { TableColumnList } from '@/components/core/manage-list/types/table-column'

const extraInitialParams = {
  foobar: '1',
  extraParam: 'extraParamValue',
}

const searchInputList: SearchInputList = [
  {
    field: 'id',
    label: '用户ID',
    type: 'text',
    // initialValue: '2',
  },
  {
    field: 'id2',
    label: 'datetime range',
    type: 'datetime-range',
    columnGap: 2,
  },
  {
    field: 'datetime',
    label: 'datetime 没有placeHolder',
    type: 'datetime',
    // valueFormat: 'timestamp',
    // valueFormat: 'date',
    valueFormat: 'ISOString',
    showTimePart: () => true,
    // initialValue: () => 'ISOString',
    // initialValue: Date.now(),
    // initialValue: () => Date.now(),
    initialValue: new Date().toISOString(),
  },
  {
    field: 'datetime1',
    label: '日期时间',
    type: 'datetime',
    valueFormat: 'dateObject',
    showTimePart: true,
    initialValue: () => new Date(),
  },
  {
    field: 'datetime2',
    label: '日期',
    type: 'datetime',
    valueFormat: 'timestamp',
    showTimePart: false,
    initialValue: () => Date.now(),
  },
  {
    field: 'id4',
    label: '用户类型',
    type: 'dropdown',
  },
  {
    field: 'id5',
    label: '多选',
    type: 'dropdown',
    multipleSelection: true,
  },
]

const tableColumnList: TableColumnList = [
  {
    field: 'id',
    label: '用户ID',
    type: 'text',
    sortable: true,
    defaultSort: 'descending',
  },
  {
    field: 'username',
    label: '用户姓名',
    type: 'text',
    sortable: true,
  },
  {
    field: 'nickname',
    label: '用户昵称',
    type: 'text',
    sortable: true,
  },
  {
    field: 'status',
    label: '用户状态',
    type: 'text',
    sortable: true,
    transformMap: {
      'NORMAL': '正常',
    },
    transformDefaultValue: '⚠ 没有找到字典!!!',
  },
  {
    field: 'telephone',
    label: '电话',
    type: 'text',
    sortable: false,
  },
  {
    field: 'test1',
    label: '电话',
    type: 'datetime',
    valueFormat: 'timestamp',
    sortable: false,
  },
]

// 模拟没有筛选条件的情况
// tableColumnList.forEach(i => i.sortable = false)

function fetchData(requestParam: RequestParam<Record<string, unknown>>) {
  return systemUserPage(requestParam)
}
</script>
