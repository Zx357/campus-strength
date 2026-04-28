<template>
  <div class="p-16px">
    <div class="grid grid-cols-1 gap-16px md:grid-cols-4">
      <ElCard v-for="item in cards" :key="item.label">
        <div class="text-14px text-gray">{{ item.label }}</div>
        <div class="mt-8px text-28px font-bold">{{ item.value }}</div>
      </ElCard>
    </div>

    <ElCard class="mt-16px">
      <template #header>近 7 日发帖趋势</template>
      <ElEmpty description="接口已预留，接入数据后展示图表" />
    </ElCard>

    <ElCard class="mt-16px">
      <template #header>内容分类占比</template>
      <ElEmpty description="接口已预留，接入数据后展示图表" />
    </ElCard>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue';
import { campusApi } from '@/service/api/campus';

const cards = ref([
  { label: '今日新增用户', value: 0 },
  { label: '今日发帖数', value: 0 },
  { label: '待处理举报数', value: 0 },
  { label: '租户数量', value: 0 }
]);

onMounted(async () => {
  const summary: any = await campusApi.dashboard.summary().catch(() => ({}));
  cards.value = [
    { label: '用户数量', value: summary.userCount || 0 },
    { label: '帖子数量', value: summary.postCount || 0 },
    { label: '待处理举报数', value: summary.pendingReportCount || 0 },
    { label: '租户数量', value: summary.tenantCount || 0 }
  ];
});
</script>
