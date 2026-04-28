<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getActivityPage } from '@/api/activity';
import type { ActivityItem } from '@/types';

const items = ref<ActivityItem[]>([]);

async function loadActivity() {
  const result = await getActivityPage({ current: 1, size: 20 });
  items.value = result.records;
}

onShow(loadActivity);

function openActivity(item: ActivityItem) {
  uni.navigateTo({ url: `/pages/activity-detail/index?id=${item.id}` });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="活动社团" />

    <view v-if="items.length" class="list">
      <view v-for="item in items" :key="item.id" class="card activity-card" @tap="openActivity(item)">
        <view class="cover" />
        <view class="body">
          <text class="title">{{ item.title }}</text>
          <text class="desc">{{ item.content }}</text>
          <view class="meta">
            <text>{{ item.activityTime || '待定' }}</text>
            <text>{{ item.activityLocation || '地点待补充' }}</text>
          </view>
          <view class="organizer">{{ item.organizer || '校园社团' }}</view>
        </view>
      </view>
    </view>
    <cw-empty v-else text="还没有活动内容，稍后再来看看" />

    <cw-tabbar active="discover" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page {
  padding: 0 24rpx 150rpx;
}

.list {
  padding-top: 22rpx;
}

.activity-card {
  margin-bottom: 18rpx;
  overflow: hidden;
}

.cover {
  height: 180rpx;
  background: linear-gradient(135deg, #4da3ff, #18b66a);
}

.body {
  padding: 24rpx;
}

.title {
  display: block;
  font-size: 30rpx;
  font-weight: 900;
  color: $text-main;
}

.desc {
  display: block;
  margin-top: 12rpx;
  font-size: 25rpx;
  line-height: 1.7;
  color: $text-sub;
}

.meta {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
  margin-top: 18rpx;
  font-size: 22rpx;
  color: $text-light;
}

.organizer {
  margin-top: 12rpx;
  color: $primary-color;
  font-size: 24rpx;
  font-weight: 700;
}
</style>
