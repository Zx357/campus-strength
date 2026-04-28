<script setup lang="ts">
import { computed } from 'vue';
import type { PostItem } from '@/types';
import { toAssetUrl } from '@/utils/request';

const props = defineProps<{
  post: PostItem;
}>();

const emit = defineEmits<{
  click: [PostItem];
  like: [PostItem];
  collect: [PostItem];
}>();

function isRenderableImage(url: string) {
  return /^(https?:)?\/\//.test(url) || url.startsWith('/') || url.startsWith('wxfile://') || url.startsWith('data:');
}

const imageList = computed(() =>
  props.post.images.map(item => ({
    raw: item,
    url: toAssetUrl(item),
    asImage: isRenderableImage(item)
  }))
);
</script>

<template>
  <view class="post-card card" @tap="emit('click', post)">
    <view class="post-card__head">
      <image class="avatar" :src="toAssetUrl(post.userAvatar || '/static/default-avatar.png')" mode="aspectFill" />
      <view class="meta">
        <view class="name-row">
          <text class="name">{{ post.userName || '校园同学' }}</text>
          <cw-tag>{{ post.tenantName || '校园墙+' }}</cw-tag>
        </view>
        <text class="time">{{ post.createdAt || '刚刚' }}</text>
      </view>
      <text class="more">...</text>
    </view>

    <view class="content">{{ post.content }}</view>

    <view v-if="imageList.length" class="images">
      <view v-for="(image, index) in imageList" :key="`${post.id}-${index}`" class="image-cell">
        <image v-if="image.asImage" class="image" :src="image.url" mode="aspectFill" />
        <view v-else class="image image-bg" :style="{ background: image.raw }" />
      </view>
    </view>

    <view class="footer">
      <text v-if="post.location" class="location">地点 {{ post.location }}</text>
      <view class="actions">
        <text class="action" :class="{ active: post.liked }" @tap.stop="emit('like', post)">赞 {{ post.likeCount }}</text>
        <text class="action">评 {{ post.commentCount }}</text>
        <text class="action" :class="{ active: post.collected }" @tap.stop="emit('collect', post)">
          藏 {{ post.collectCount }}
        </text>
      </view>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.post-card {
  padding: 24rpx;
  margin-bottom: 22rpx;
}

.post-card__head {
  display: flex;
  align-items: center;
}

.avatar {
  width: 68rpx;
  height: 68rpx;
  border-radius: 50%;
  background: #e6f6ee;
}

.meta {
  flex: 1;
  margin-left: 14rpx;
}

.name-row {
  display: flex;
  align-items: center;
  gap: 10rpx;
}

.name {
  font-size: 27rpx;
  font-weight: 800;
}

.time,
.more {
  color: $text-light;
  font-size: 22rpx;
}

.content {
  margin: 18rpx 0;
  font-size: 28rpx;
  line-height: 1.6;
  color: $text-main;
  white-space: pre-line;
}

.images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10rpx;
}

.image-cell,
.image {
  width: 100%;
  height: 176rpx;
  border-radius: 16rpx;
  overflow: hidden;
}

.image {
  display: block;
}

.image-bg {
  background-size: cover;
}

.footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 16rpx;
}

.location {
  max-width: 50%;
  padding: 8rpx 14rpx;
  border-radius: 999rpx;
  background: $primary-light;
  color: $primary-color;
  font-size: 22rpx;
}

.actions {
  display: flex;
  gap: 22rpx;
  color: $text-sub;
  font-size: 24rpx;
}

.action.active {
  color: $danger-color;
}
</style>
