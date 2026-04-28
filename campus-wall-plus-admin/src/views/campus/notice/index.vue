<template>
  <CrudPage title="公告管理" base-url="/api/admin/notice" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../components/crud-page.vue';

const columns = [
  { prop: 'title', label: '标题' },
  { prop: 'type', label: '类型' },
  { prop: 'topFlag', label: '置顶' }
];

const formFields = [
  { prop: 'title', label: '标题', required: true, type: 'text' },
  { prop: 'content', label: '内容', required: true, type: 'textarea' },
  { prop: 'type', label: '类型', required: false, type: 'text' },
  { prop: 'topFlag', label: '是否置顶', required: false, type: 'text' }
];
</script>
