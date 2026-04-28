<script setup lang="ts">
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { createComment, getCommentPage } from '@/api/comment';
import { getPostDetail, toggleCollect, toggleLike } from '@/api/post';
import { createReport } from '@/api/report';
import { useUserStore } from '@/store/user';
import type { CommentItem, PostItem } from '@/types';

const userStore = useUserStore();
const postId = ref<number>(0);
const post = ref<PostItem | null>(null);
const comments = ref<CommentItem[]>([]);
const commentText = ref('');
const submitting = ref(false);

const pageTitle = computed(() => post.value?.title || '帖子详情');

async function loadDetail() {
  if (!postId.value) return;
  const [detailResult, commentResult] = await Promise.all([
    getPostDetail(postId.value),
    getCommentPage({ postId: postId.value, current: 1, size: 50 })
  ]);
  post.value = detailResult;
  comments.value = commentResult.records;
}

onLoad((query: Record<string, string>) => {
  postId.value = Number(query.id || 0);
  loadDetail();
});

function goBack() {
  uni.navigateBack({
    fail: () => {
      uni.reLaunch({ url: '/pages/home/index' });
    }
  });
}

async function handleLike() {
  if (!post.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '游客模式下不能点赞', icon: 'none' });
    return;
  }

  const liked = await toggleLike(post.value.id).catch(() => !post.value?.liked);
  if (!post.value) return;
  post.value = {
    ...post.value,
    liked,
    likeCount: liked ? post.value.likeCount + 1 : Math.max(0, post.value.likeCount - 1)
  };
}

async function handleCollect() {
  if (!post.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '游客模式下不能收藏', icon: 'none' });
    return;
  }

  const collected = await toggleCollect(post.value.id).catch(() => !post.value?.collected);
  if (!post.value) return;
  post.value = {
    ...post.value,
    collected,
    collectCount: collected ? post.value.collectCount + 1 : Math.max(0, post.value.collectCount - 1)
  };
}

async function submitComment() {
  if (!post.value || !commentText.value.trim() || submitting.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '游客模式下不能评论', icon: 'none' });
    return;
  }

  submitting.value = true;
  try {
    await createComment({
      postId: post.value.id,
      content: commentText.value.trim()
    });

    comments.value.push({
      id: Date.now(),
      postId: post.value.id,
      content: commentText.value.trim(),
      createdAt: '刚刚',
      userName: userStore.profile?.nickname || '我',
      userAvatar: userStore.profile?.avatarUrl || '/static/default-avatar.png'
    });
    post.value.commentCount += 1;
    commentText.value = '';
    uni.showToast({ title: '评论成功', icon: 'success' });
  } finally {
    submitting.value = false;
  }
}

function reportPost() {
  if (!post.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '游客模式下不能举报', icon: 'none' });
    return;
  }

  uni.showActionSheet({
    itemList: ['广告营销', '不友善言论', '违法违规', '其他原因'],
    success: async res => {
      const reasonType = ['广告营销', '不友善言论', '违法违规', '其他原因'][res.tapIndex];
      await createReport({
        targetType: 'post',
        targetId: post.value!.id,
        reasonType,
        reasonContent: reasonType
      });
      uni.showToast({ title: '举报已提交', icon: 'success' });
    }
  });
}
</script>

<template>
  <view class="safe-page detail-page">
    <cw-navbar :title="pageTitle" back @back="goBack">
      <template #right>
        <text @tap="reportPost">举报</text>
      </template>
    </cw-navbar>

    <view v-if="post" class="content-wrap">
      <cw-post-card :post="post" @like="handleLike" @collect="handleCollect" />

      <view class="comment-section card">
        <view class="section-head">
          <text class="section-title">评论 {{ comments.length }}</text>
          <text class="section-sub">按时间顺序展示</text>
        </view>

        <view v-if="comments.length" class="comment-list">
          <view v-for="item in comments" :key="item.id" class="comment-item">
            <image class="comment-avatar" :src="item.userAvatar || '/static/default-avatar.png'" mode="aspectFill" />
            <view class="comment-body">
              <view class="comment-top">
                <text class="comment-name">{{ item.userName || '校园同学' }}</text>
                <text class="comment-time">{{ item.createdAt || '刚刚' }}</text>
              </view>
              <text v-if="item.replyUserName" class="reply-name">回复 {{ item.replyUserName }}</text>
              <text class="comment-content">{{ item.content }}</text>
            </view>
          </view>
        </view>
        <cw-empty v-else text="还没有评论，来抢个沙发吧" />
      </view>
    </view>

    <view class="composer">
      <input
        v-model="commentText"
        class="composer-input"
        maxlength="200"
        placeholder="写下你的评论..."
        placeholder-class="composer-placeholder"
      />
      <view class="composer-btn" @tap="submitComment">{{ submitting ? '发送中' : '发送' }}</view>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.detail-page {
  padding-bottom: 160rpx;
}

.content-wrap {
  padding: 18rpx 24rpx 0;
}

.comment-section {
  margin-top: 18rpx;
  padding: 24rpx;
}

.section-head {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}

.section-title {
  font-size: 30rpx;
  font-weight: 900;
  color: $text-main;
}

.section-sub {
  font-size: 22rpx;
  color: $text-light;
}

.comment-list {
  margin-top: 18rpx;
}

.comment-item {
  display: flex;
  gap: 16rpx;
  padding: 18rpx 0;
  border-bottom: 1rpx solid $border-color;
}

.comment-item:last-child {
  border-bottom: 0;
  padding-bottom: 0;
}

.comment-avatar {
  width: 64rpx;
  height: 64rpx;
  border-radius: 50%;
  background: #eaf8f1;
}

.comment-body {
  flex: 1;
}

.comment-top {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.comment-name {
  font-size: 26rpx;
  font-weight: 700;
  color: $text-main;
}

.comment-time {
  font-size: 22rpx;
  color: $text-light;
}

.reply-name {
  display: block;
  margin-top: 6rpx;
  font-size: 22rpx;
  color: $primary-color;
}

.comment-content {
  display: block;
  margin-top: 10rpx;
  font-size: 26rpx;
  line-height: 1.7;
  color: $text-sub;
}

.composer {
  position: fixed;
  left: 20rpx;
  right: 20rpx;
  bottom: 20rpx;
  z-index: 20;
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 14rpx;
  border-radius: 26rpx;
  background: rgba(255, 255, 255, 0.98);
  box-shadow: 0 16rpx 36rpx rgba(15, 35, 60, 0.12);
}

.composer-input {
  flex: 1;
  height: 72rpx;
  padding: 0 22rpx;
  border-radius: 999rpx;
  background: #f3f6f8;
  font-size: 26rpx;
  color: $text-main;
}

:deep(.composer-placeholder) {
  color: $text-light;
}

.composer-btn {
  min-width: 120rpx;
  height: 72rpx;
  padding: 0 26rpx;
  border-radius: 999rpx;
  background: linear-gradient(135deg, $primary-color, $primary-dark);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26rpx;
  font-weight: 800;
}
</style>
