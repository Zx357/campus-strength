<template>
  <CrudPage title="二手闲置" base-url="/api/admin/goods" audit-type="goods" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../../components/crud-page.vue';

const columns = [
  { prop: 'title', label: '标题' },
  { prop: 'price', label: '价格' },
  { prop: 'conditionLevel', label: '成色' },
  { prop: 'tradeMethod', label: '交易方式' }
];

const formFields = [
  { prop: 'title', label: '标题', required: true, type: 'text' },
  { prop: 'description', label: '描述', required: false, type: 'textarea' },
  { prop: 'price', label: '价格', required: true, type: 'text' },
  { prop: 'conditionLevel', label: '成色', required: false, type: 'text' }
];
</script>
