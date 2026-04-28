<script setup lang="ts">
defineProps<{ items: any[] }>();

defineEmits<{
  select: [any];
}>();
</script>

<template>
  <view class="grid">
    <view v-for="item in items" :key="item.text" class="item" @tap="$emit('select', item)">
      <view class="icon" :style="{ background: item.bg }">{{ item.icon }}</view>
      <text>{{ item.text }}</text>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 28rpx 10rpx;
  padding: 26rpx 16rpx;
}

.item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12rpx;
  font-size: 24rpx;
  color: $text-main;
}

.icon {
  width: 60rpx;
  height: 60rpx;
  border-radius: 20rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 30rpx;
}
</style>
