<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getMyReportPage } from '@/api/report';
import type { ReportItem } from '@/types';

const reports = ref<ReportItem[]>([]);

async function loadData() {
  const result = await getMyReportPage({ current: 1, size: 20 });
  reports.value = result.records;
}

onShow(loadData);

function statusText(status?: number) {
  if (status === 1) return '已处理';
  if (status === 2) return '已驳回';
  return '待处理';
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="我的举报" back @back="uni.navigateBack()" />

    <view v-if="reports.length" class="list">
      <view v-for="item in reports" :key="item.id" class="card report-card">
        <view class="top">
          <text class="type">{{ item.targetType || '内容' }}</text>
          <cw-tag>{{ statusText(item.status) }}</cw-tag>
        </view>
        <text class="reason">{{ item.reasonType || '其他原因' }}</text>
        <text class="content">{{ item.reasonContent || '未填写补充说明' }}</text>
        <text v-if="item.handleResult" class="result">处理结果：{{ item.handleResult }}</text>
        <text class="time">{{ item.createdAt || '刚刚' }}</text>
      </view>
    </view>
    <cw-empty v-else text="还没有举报记录" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page {
  padding: 0 24rpx 40rpx;
}

.list {
  padding-top: 22rpx;
}

.report-card {
  margin-bottom: 18rpx;
  padding: 22rpx;
}

.top {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.type,
.reason {
  color: $text-main;
  font-size: 28rpx;
  font-weight: 800;
}

.reason {
  display: block;
  margin-top: 10rpx;
  font-size: 26rpx;
}

.content,
.result,
.time {
  display: block;
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.7;
  color: $text-sub;
}

.time {
  color: $text-light;
}
</style>
