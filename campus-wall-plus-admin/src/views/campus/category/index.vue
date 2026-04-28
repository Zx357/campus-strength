<template>
  <CrudPage title="分类管理" base-url="/api/admin/category" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../components/crud-page.vue';

const columns = [
  { prop: 'moduleCode', label: '模块' },
  { prop: 'categoryName', label: '分类名称' },
  { prop: 'sort', label: '排序' }
];

const formFields = [
  { prop: 'moduleCode', label: '模块', required: true, type: 'text' },
  { prop: 'categoryName', label: '分类名称', required: true, type: 'text' },
  { prop: 'icon', label: '图标', required: false, type: 'text' },
  { prop: 'sort', label: '排序', required: false, type: 'text' }
];
</script>
