<template>
  <CrudPage title="操作日志" base-url="/api/admin/operation-log" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../components/crud-page.vue';

const columns = [
  { prop: 'username', label: '用户' },
  { prop: 'moduleName', label: '模块' },
  { prop: 'operationType', label: '操作' },
  { prop: 'result', label: '结果' }
];

const formFields = [
  { prop: 'moduleName', label: '模块', required: false, type: 'text' },
  { prop: 'operationType', label: '操作', required: false, type: 'text' }
];
</script>
