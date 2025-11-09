<template>
  <div class="no-scroll">
    <page-title title="用户管理" type="single-line">
      <UserFilled />
    </page-title>

    <page-title title="用户管理" />

    <page-title title="用户管理">
      <User />
    </page-title>

    <!-- 用户管理 -->
    <manage-list />
  </div>
</template>

<script setup lang="ts">
import { User, UserFilled } from '@element-plus/icons-vue'
import ManageList from '@/components/core/manage-list/manage-list.vue'

</script>
