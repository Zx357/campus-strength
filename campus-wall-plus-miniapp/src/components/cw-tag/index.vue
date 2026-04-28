<template>
  <text class="tag"><slot /></text>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.tag {
  display: inline-flex;
  align-items: center;
  padding: 6rpx 14rpx;
  border-radius: 999rpx;
  background: $primary-light;
  color: $primary-color;
  font-size: 22rpx;
  font-weight: 700;
}
</style>
