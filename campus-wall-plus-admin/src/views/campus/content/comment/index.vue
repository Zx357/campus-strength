<template>
  <CrudPage title="评论管理" base-url="/api/admin/comment" audit-type="comment" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../../components/crud-page.vue';

const columns = [
  { prop: 'postId', label: '帖子ID' },
  { prop: 'userId', label: '用户ID' },
  { prop: 'content', label: '内容' },
  { prop: 'likeCount', label: '点赞数' }
];

const formFields = [
  { prop: 'postId', label: '帖子ID', required: true, type: 'text' },
  { prop: 'content', label: '内容', required: true, type: 'textarea' }
];
</script>
