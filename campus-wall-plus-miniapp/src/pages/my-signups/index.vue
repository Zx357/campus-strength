<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { cancelActivitySignup, getMySignupActivityPage } from '@/api/activity';
import type { ActivityItem } from '@/types';

const items = ref<ActivityItem[]>([]);

async function loadData() {
  const result = await getMySignupActivityPage({ current: 1, size: 20 });
  items.value = result.records;
}

onShow(loadData);

function openActivity(item: ActivityItem) {
  uni.navigateTo({ url: `/pages/activity-detail/index?id=${item.id}` });
}

function cancelSignup(item: ActivityItem) {
  uni.showModal({
    title: '取消报名',
    content: `确认取消报名「${item.title}」吗？`,
    success: async res => {
      if (!res.confirm) return;
      await cancelActivitySignup(item.id);
      uni.showToast({ title: '已取消', icon: 'success' });
      loadData();
    }
  });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="我的报名" back @back="uni.navigateBack()" />

    <view v-if="items.length" class="list">
      <view v-for="item in items" :key="item.id" class="card signup-card" @tap="openActivity(item)">
        <view class="title-row">
          <text class="title">{{ item.title }}</text>
          <cw-tag>已报名</cw-tag>
        </view>
        <text class="desc">{{ item.content }}</text>
        <view class="meta">
          <text>{{ item.activityTime || '待定' }}</text>
          <text>{{ item.activityLocation || '地点待补充' }}</text>
        </view>
        <view class="actions">
          <text @tap.stop="cancelSignup(item)">取消报名</text>
        </view>
      </view>
    </view>
    <cw-empty v-else text="还没有活动报名记录" />
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

.signup-card {
  margin-bottom: 18rpx;
  padding: 24rpx;
}

.title-row,
.meta,
.actions {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.title {
  color: $text-main;
  font-size: 30rpx;
  font-weight: 900;
}

.desc {
  display: block;
  margin: 12rpx 0 16rpx;
  color: $text-sub;
  font-size: 25rpx;
  line-height: 1.7;
}

.meta {
  color: $text-light;
  font-size: 22rpx;
}

.actions {
  margin-top: 18rpx;
  justify-content: flex-end;
  color: $danger-color;
  font-size: 24rpx;
}
</style>
