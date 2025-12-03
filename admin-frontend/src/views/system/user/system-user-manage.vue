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
    <manage-list :search-input-list="searchInputList" />

  </div>
</template>

<script setup lang="ts">
import { User, UserFilled } from '@element-plus/icons-vue'
import ManageList from '@/components/core/manage-list/manage-list.vue'
import type { SearchInputList } from '@/components/core/manage-list/types/search-input'

const searchInputList: SearchInputList = [
  {
    label: '用户ID',
    type: 'text',
  },
  {
    label: 'datetime range',
    type: 'datetime-range',
    columnGap: 2,
  },
  {
    label: '没有placeHolder',
    type: 'datetime',
  },
  {
    label: '用户类型',
    type: 'dropdown',
  },
  {
    label: '多选',
    type: 'dropdown',
    multipleSelection: true,
  },
]
</script>
