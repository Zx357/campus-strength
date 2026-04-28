<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(
  defineProps<{
    title: string;
    back?: boolean;
    transparent?: boolean;
  }>(),
  {
    back: false,
    transparent: false
  }
);

const emit = defineEmits<{
  back: [];
}>();

const statusBarHeight = computed(() => uni.getSystemInfoSync().statusBarHeight || 20);

function handleBack() {
  if (props.back) {
    emit('back');
  }
}
</script>

<template>
  <view class="navbar" :class="{ transparent }" :style="{ paddingTop: `${statusBarHeight}px` }">
    <view class="navbar__side left" @tap="handleBack">
      <view v-if="back" class="navbar__icon" />
    </view>
    <view class="navbar__title">{{ title }}</view>
    <view class="navbar__side right">
      <slot name="right" />
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.navbar {
  position: sticky;
  top: 0;
  z-index: 30;
  display: flex;
  align-items: center;
  height: 88rpx;
  padding-left: 20rpx;
  padding-right: 20rpx;
  background: rgba(255, 255, 255, 0.96);
  backdrop-filter: blur(14rpx);

  &.transparent {
    background: transparent;
    backdrop-filter: none;
  }
}

.navbar__side {
  display: flex;
  align-items: center;
  width: 120rpx;
  min-height: 88rpx;
  color: $text-main;
}

.left {
  justify-content: flex-start;
}

.right {
  justify-content: flex-end;
  color: $primary-color;
  font-size: 26rpx;
  font-weight: 700;
}

.navbar__title {
  flex: 1;
  text-align: center;
  font-size: 32rpx;
  font-weight: 800;
  color: $text-main;
}

.navbar__icon {
  width: 20rpx;
  height: 20rpx;
  border-left: 4rpx solid $text-main;
  border-bottom: 4rpx solid $text-main;
  transform: rotate(45deg);
}
</style>
