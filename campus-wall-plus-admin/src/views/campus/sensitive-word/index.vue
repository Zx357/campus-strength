<template>
  <CrudPage title="敏感词管理" base-url="/api/admin/sensitive-word" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../components/crud-page.vue';

const columns = [
  { prop: 'word', label: '敏感词' },
  { prop: 'level', label: '级别' },
  { prop: 'status', label: '状态' }
];

const formFields = [
  { prop: 'word', label: '敏感词', required: true, type: 'text' },
  { prop: 'level', label: '级别', required: true, type: 'text' }
];
</script>
