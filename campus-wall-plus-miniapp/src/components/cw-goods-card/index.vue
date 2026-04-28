<script setup lang="ts">
import type { GoodsItem } from '@/types';
import { toAssetUrl } from '@/utils/request';

defineProps<{ item: GoodsItem }>();
defineEmits<{
  click: [GoodsItem];
}>();

function isRenderableImage(url?: string) {
  if (!url) return false;
  return /^(https?:)?\/\//.test(url) || url.startsWith('/') || url.startsWith('wxfile://') || url.startsWith('data:');
}
</script>

<template>
  <view class="goods-card card" @tap="$emit('click', item)">
    <image
      v-if="isRenderableImage(item.images[0])"
      class="photo"
      :src="toAssetUrl(item.images[0])"
      mode="aspectFill"
    />
    <view v-else class="photo photo-bg" :style="{ background: item.images[0] || '#E5E7EB' }" />
    <view class="title">{{ item.title }}</view>
    <view class="price-row">
      <text class="price">￥{{ item.price }}</text>
      <text v-if="item.originalPrice" class="origin">￥{{ item.originalPrice }}</text>
    </view>
    <view class="tags">
      <cw-tag>{{ item.conditionLevel || '成色佳' }}</cw-tag>
      <cw-tag>{{ item.tradeMethod || '自提' }}</cw-tag>
    </view>
    <view class="seller">
      <image class="avatar" :src="toAssetUrl(item.sellerAvatar || '/static/default-avatar.png')" mode="aspectFill" />
      <text>{{ item.sellerName || '校园同学' }}</text>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.goods-card {
  padding: 14rpx;
  margin-bottom: 18rpx;
  break-inside: avoid;
}

.photo {
  width: 100%;
  height: 220rpx;
  border-radius: 16rpx;
  background: #eef2f7;
}

.photo-bg {
  background-size: cover;
}

.title {
  margin-top: 14rpx;
  font-size: 26rpx;
  font-weight: 700;
  line-height: 1.4;
  color: $text-main;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 10rpx;
  margin-top: 8rpx;
}

.price {
  color: $danger-color;
  font-size: 30rpx;
  font-weight: 900;
}

.origin {
  color: $text-light;
  font-size: 22rpx;
  text-decoration: line-through;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 8rpx;
  margin: 10rpx 0;
}

.seller {
  display: flex;
  align-items: center;
  gap: 8rpx;
  color: $text-sub;
  font-size: 22rpx;
}

.avatar {
  width: 32rpx;
  height: 32rpx;
  border-radius: 50%;
  background: #eaf8f1;
}
</style>
