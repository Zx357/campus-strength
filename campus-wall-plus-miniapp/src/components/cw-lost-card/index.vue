<script setup lang="ts">
import type { LostFoundItem } from '@/types';
import { toAssetUrl } from '@/utils/request';

const props = defineProps<{ item: LostFoundItem }>();

defineEmits<{
  contact: [LostFoundItem];
  click: [LostFoundItem];
}>();

function isRenderableImage(url?: string) {
  if (!url) return false;
  return /^(https?:)?\/\//.test(url) || url.startsWith('/') || url.startsWith('wxfile://') || url.startsWith('data:');
}

const badgeType = props.item.status === 2 ? 'gray' : '';
</script>

<template>
  <view class="lost-card card" @tap="$emit('click', item)">
    <image
      v-if="isRenderableImage(item.images[0])"
      class="photo"
      :src="toAssetUrl(item.images[0])"
      mode="aspectFill"
    />
    <view v-else class="photo photo-bg" :style="{ background: item.images[0] || '#E5E7EB' }" />
    <view class="body">
      <view class="row">
        <text class="title">{{ item.title }}</text>
        <cw-status-badge :type="badgeType">{{ item.status === 2 ? '已找到' : '寻找中' }}</cw-status-badge>
      </view>
      <text class="line">地点 {{ item.lostLocation || '地点待补充' }}</text>
      <text class="line">时间 {{ item.lostTime || '刚刚' }}</text>
      <button class="contact" @tap="$emit('contact', item)">联系TA</button>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.lost-card {
  display: flex;
  gap: 20rpx;
  padding: 18rpx;
  margin-bottom: 20rpx;
}

.photo {
  width: 150rpx;
  height: 150rpx;
  border-radius: 18rpx;
  background: #eef2f7;
}

.photo-bg {
  background-size: cover;
}

.body {
  flex: 1;
}

.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12rpx;
}

.title {
  flex: 1;
  font-size: 30rpx;
  font-weight: 800;
  color: $text-main;
}

.line {
  display: block;
  margin-top: 14rpx;
  font-size: 24rpx;
  color: $text-sub;
}

.contact {
  float: right;
  margin-top: 8rpx;
  padding: 0 22rpx;
  height: 54rpx;
  line-height: 54rpx;
  border-radius: 999rpx;
  background: $primary-light;
  color: $primary-color;
  font-size: 24rpx;
  border: 0;
}
</style>
