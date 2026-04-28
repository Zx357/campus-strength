<script setup lang="ts">
defineProps<{
  modelValue?: string;
  placeholder?: string;
}>();

defineEmits<{
  'update:modelValue': [string];
  tap: [];
  confirm: [string];
}>();
</script>

<template>
  <view class="search-bar" @tap="$emit('tap')">
    <text class="search-bar__icon">搜</text>
    <input
      class="search-bar__input"
      :value="modelValue"
      :placeholder="placeholder || '搜索内容'"
      placeholder-class="search-placeholder"
      confirm-type="search"
      @input="$emit('update:modelValue', $event.detail.value)"
      @confirm="$emit('confirm', $event.detail.value)"
    />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.search-bar {
  display: flex;
  align-items: center;
  gap: 10rpx;
  height: 68rpx;
  padding: 0 22rpx;
  border-radius: 999rpx;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: inset 0 0 0 2rpx rgba(229, 231, 235, 0.9);
}

.search-bar__icon {
  color: $text-light;
  font-size: 24rpx;
}

.search-bar__input {
  flex: 1;
  height: 68rpx;
  font-size: 24rpx;
  color: $text-main;
}

:deep(.search-placeholder) {
  color: $text-light;
}
</style>
