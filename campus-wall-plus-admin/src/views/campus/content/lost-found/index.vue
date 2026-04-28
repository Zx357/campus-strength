<template>
  <CrudPage title="失物招领" base-url="/api/admin/lost-found" audit-type="lost" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../../components/crud-page.vue';

const columns = [
  { prop: 'title', label: '标题' },
  { prop: 'lostLocation', label: '地点' },
  { prop: 'contactPhone', label: '联系电话' },
  { prop: 'viewCount', label: '浏览数' }
];

const formFields = [
  { prop: 'title', label: '标题', required: true, type: 'text' },
  { prop: 'description', label: '描述', required: false, type: 'textarea' },
  { prop: 'lostLocation', label: '地点', required: false, type: 'text' },
  { prop: 'contactPhone', label: '联系电话', required: false, type: 'text' }
];
</script>
