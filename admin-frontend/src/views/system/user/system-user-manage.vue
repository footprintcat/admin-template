<template>
  <!-- class="no-scroll" -->
  <div style="display: flex; flex-direction: column; ">

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
    <manage-list :search-input-list="searchInputList" :fetch-data="fetchData" />
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
import { User, UserFilled } from '@element-plus/icons-vue'
import { get } from '@/utils/api'
import ManageList from '@/components/core/manage-list/manage-list.vue'
import type { SearchInputList } from '@/components/core/manage-list/types/search-input'

const searchInputList: SearchInputList = [
  {
    field: 'id',
    label: '用户ID',
    type: 'text',
  },
  {
    field: 'id2',
    label: 'datetime range',
    type: 'datetime-range',
    columnGap: 2,
  },
  {
    field: 'id3',
    label: '没有placeHolder',
    type: 'datetime',
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

function fetchData(): Promise<Array<unknown>> {
  return get('/v2/manage/user/list')
}
</script>
