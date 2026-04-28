<script setup lang="ts">
defineProps<{ type?: string }>();
</script>

<template>
  <text class="badge" :class="type"><slot /></text>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.badge {
  padding: 8rpx 16rpx;
  border-radius: 999rpx;
  background: $primary-light;
  color: $primary-color;
  font-size: 22rpx;
}

.danger {
  background: #fff1f2;
  color: $danger-color;
}

.gray {
  background: #f3f4f6;
  color: $text-light;
}
</style>
