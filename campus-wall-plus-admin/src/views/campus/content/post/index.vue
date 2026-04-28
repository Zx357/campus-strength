<template>
  <CrudPage title="校园墙帖子" base-url="/api/admin/post" audit-type="post" :columns="columns" :form-fields="formFields" />
</template>

<script setup lang="ts">
import CrudPage from '../../components/crud-page.vue';

const columns = [
  { prop: 'title', label: '标题' },
  { prop: 'content', label: '内容' },
  { prop: 'likeCount', label: '点赞数' },
  { prop: 'commentCount', label: '评论数' },
  { prop: 'hotScore', label: '热度' }
];

const formFields = [
  { prop: 'title', label: '标题', required: false, type: 'text' },
  { prop: 'content', label: '内容', required: true, type: 'textarea' },
  { prop: 'location', label: '位置', required: false, type: 'text' }
];
</script>
