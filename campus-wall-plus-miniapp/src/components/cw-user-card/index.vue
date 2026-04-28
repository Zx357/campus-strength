<script setup lang="ts">
defineProps<{
  avatar: string;
  name: string;
  school: string;
}>();
</script>

<template>
  <view class="user-card card">
    <image class="avatar" :src="avatar" mode="aspectFill" />
    <view class="info">
      <view class="name-row">
        <text class="name">{{ name }}</text>
        <cw-tag>学校认证</cw-tag>
      </view>
      <text class="school">{{ school }}</text>
    </view>
    <view class="arrow" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.user-card {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  gap: 18rpx;
  margin: -54rpx 28rpx 0;
  padding: 24rpx;
}

.avatar {
  width: 108rpx;
  height: 108rpx;
  border-radius: 50%;
  background: #eaf8f1;
}

.info {
  flex: 1;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 10rpx;
  margin-bottom: 8rpx;
}

.name {
  font-size: 30rpx;
  font-weight: 900;
  color: $text-main;
}

.school {
  font-size: 24rpx;
  color: $text-sub;
}

.arrow {
  width: 16rpx;
  height: 16rpx;
  border-top: 3rpx solid $text-light;
  border-right: 3rpx solid $text-light;
  transform: rotate(45deg);
}
</style>
