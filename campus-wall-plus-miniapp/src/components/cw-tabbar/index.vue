<script setup lang="ts">
const props = defineProps<{ active: string }>();

const items = [
  { key: 'home', label: '首页', icon: '首', url: '/pages/home/index' },
  { key: 'discover', label: '发现', icon: '逛', url: '/pages/goods/index' },
  { key: 'publish', label: '发布', icon: '+', url: '/pages/publish/index' },
  { key: 'message', label: '消息', icon: '信', url: '/pages/message/index' },
  { key: 'mine', label: '我的', icon: '我', url: '/pages/mine/index' }
];

function go(item: (typeof items)[number]) {
  if (item.key === props.active) return;

  if (item.key === 'publish') {
    uni.navigateTo({ url: item.url });
    return;
  }

  uni.reLaunch({ url: item.url });
}
</script>

<template>
  <view class="tabbar">
    <view
      v-for="item in items"
      :key="item.key"
      class="tabbar__item"
      :class="{ active: active === item.key, publish: item.key === 'publish' }"
      @tap="go(item)"
    >
      <view class="tabbar__icon">{{ item.icon }}</view>
      <text>{{ item.label }}</text>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.tabbar {
  position: fixed;
  left: 20rpx;
  right: 20rpx;
  bottom: 24rpx;
  z-index: 20;
  display: flex;
  align-items: center;
  justify-content: space-around;
  height: 108rpx;
  padding: 0 8rpx;
  border-radius: 34rpx;
  background: rgba(255, 255, 255, 0.98);
  box-shadow: 0 16rpx 36rpx rgba(15, 35, 60, 0.12);
}

.tabbar__item {
  width: 20%;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 6rpx;
  color: $text-light;
  font-size: 22rpx;
}

.tabbar__icon {
  font-size: 30rpx;
  line-height: 36rpx;
}

.active {
  color: $primary-color;
  font-weight: 700;
}

.publish {
  margin-top: -34rpx;

  .tabbar__icon {
    width: 74rpx;
    height: 74rpx;
    border-radius: 999rpx;
    background: linear-gradient(135deg, $primary-color, $primary-dark);
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 12rpx 24rpx rgba(24, 182, 106, 0.32);
    font-size: 42rpx;
  }
}
</style>
