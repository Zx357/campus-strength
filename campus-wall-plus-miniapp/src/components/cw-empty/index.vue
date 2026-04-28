<script setup lang="ts">
defineProps<{
  text?: string;
}>();
</script>

<template>
  <view class="empty">
    <image class="empty-image" src="/static/empty.png" mode="aspectFit" />
    <text>{{ text || '暂无内容' }}</text>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.empty {
  padding: 80rpx 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 16rpx;
  color: $text-light;
  font-size: 24rpx;
}

.empty-image {
  width: 180rpx;
  height: 180rpx;
}
</style>
