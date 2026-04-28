<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getMyCollectPostPage } from '@/api/post';
import type { PostItem } from '@/types';

const posts = ref<PostItem[]>([]);

async function loadData() {
  const result = await getMyCollectPostPage({ current: 1, size: 20 });
  posts.value = result.records;
}

onShow(loadData);

function openPost(post: PostItem) {
  uni.navigateTo({ url: `/pages/post-detail/index?id=${post.id}` });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="我的收藏" back @back="uni.navigateBack()" />
    <view v-if="posts.length" class="list">
      <cw-post-card v-for="item in posts" :key="item.id" :post="item" @click="openPost" />
    </view>
    <cw-empty v-else text="还没有收藏内容" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page {
  padding: 0 24rpx 40rpx;
}

.list {
  padding-top: 22rpx;
}
</style>
