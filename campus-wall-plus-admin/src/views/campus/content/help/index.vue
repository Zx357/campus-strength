<template>
  <CrudPage title="校园互助" base-url="/api/admin/help" audit-type="help" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../../components/crud-page.vue';

const columns = [
  { prop: 'title', label: '标题' },
  { prop: 'type', label: '类型' },
  { prop: 'rewardAmount', label: '悬赏' }
];

const formFields = [
  { prop: 'title', label: '标题', required: true, type: 'text' },
  { prop: 'content', label: '内容', required: false, type: 'textarea' },
  { prop: 'rewardAmount', label: '悬赏', required: false, type: 'text' }
];
</script>
