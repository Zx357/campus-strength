<script setup lang="ts">
defineProps<{ list: string[] }>();

defineEmits<{
  add: [];
  remove: [number];
}>();

function isRenderableImage(url?: string) {
  if (!url) return false;
  return /^(https?:)?\/\//.test(url) || url.startsWith('/') || url.startsWith('wxfile://') || url.startsWith('data:');
}
</script>

<template>
  <view class="upload-grid">
    <view class="cell add" @tap="$emit('add')">
      <text class="add-icon">+</text>
      <text class="add-text">添加图片</text>
      <text class="add-count">({{ list.length }}/9)</text>
    </view>
    <view v-for="(img, index) in list" :key="`${img}-${index}`" class="cell image-cell">
      <image v-if="isRenderableImage(img)" class="image" :src="img" mode="aspectFill" />
      <view v-else class="image" :style="{ background: img }" />
      <text class="remove" @tap="$emit('remove', index)">x</text>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.upload-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 18rpx;
}

.cell {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 150rpx;
  border-radius: 18rpx;
  background: #f2f4f6;
}

.add {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2rpx dashed #d7dce2;
  color: $text-light;
}

.add-icon {
  font-size: 40rpx;
  line-height: 1;
}

.add-text,
.add-count {
  font-size: 22rpx;
  line-height: 1.5;
}

.image {
  width: 100%;
  height: 100%;
}

.remove {
  position: absolute;
  top: 8rpx;
  right: 8rpx;
  width: 34rpx;
  height: 34rpx;
  border-radius: 50%;
  background: rgba(0, 0, 0, 0.38);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24rpx;
}
</style>
