<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { deleteComment, getMyCommentPage } from '@/api/comment';
import type { CommentItem } from '@/types';

const comments = ref<CommentItem[]>([]);

async function loadData() {
  const result = await getMyCommentPage({ current: 1, size: 20 });
  comments.value = result.records;
}

onShow(loadData);

function confirmDelete(item: CommentItem) {
  uni.showModal({
    title: '删除评论',
    content: '确认删除这条评论吗？',
    success: async res => {
      if (!res.confirm) return;
      await deleteComment(item.id);
      uni.showToast({ title: '已删除', icon: 'success' });
      loadData();
    }
  });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="我的评论" back @back="uni.navigateBack()" />

    <view v-if="comments.length" class="list">
      <view v-for="item in comments" :key="item.id" class="card comment-card">
        <view class="top">
          <text class="name">{{ item.userName || '我' }}</text>
          <text class="time">{{ item.createdAt || '刚刚' }}</text>
        </view>
        <text v-if="item.replyUserName" class="reply">回复 {{ item.replyUserName }}</text>
        <text class="content">{{ item.content }}</text>
        <view class="actions">
          <text @tap="confirmDelete(item)">删除</text>
        </view>
      </view>
    </view>
    <cw-empty v-else text="还没有评论记录" />
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

.comment-card {
  margin-bottom: 18rpx;
  padding: 22rpx;
}

.top {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.name {
  color: $text-main;
  font-size: 28rpx;
  font-weight: 800;
}

.time,
.reply {
  color: $text-light;
  font-size: 22rpx;
}

.reply {
  display: block;
  margin-top: 10rpx;
}

.content {
  display: block;
  margin-top: 12rpx;
  font-size: 26rpx;
  line-height: 1.7;
  color: $text-sub;
}

.actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12rpx;
  color: $danger-color;
  font-size: 24rpx;
}
</style>
