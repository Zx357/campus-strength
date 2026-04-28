<template>
  <CrudPage title="租户管理" base-url="/api/platform/tenant" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../components/crud-page.vue';

const columns = [
  { prop: 'tenantCode', label: '学校编码' },
  { prop: 'tenantName', label: '学校名称' },
  { prop: 'themeColor', label: '主题色' }
];

const formFields = [
  { prop: 'tenantCode', label: '学校编码', required: true, type: 'text' },
  { prop: 'tenantName', label: '学校名称', required: true, type: 'text' },
  { prop: 'themeColor', label: '主题色', required: false, type: 'text' },
  { prop: 'contactName', label: '联系人', required: false, type: 'text' },
  { prop: 'contactPhone', label: '联系电话', required: false, type: 'text' }
];
</script>
