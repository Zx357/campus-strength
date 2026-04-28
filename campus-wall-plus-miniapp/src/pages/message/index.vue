<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getMessagePage, markMessageRead } from '@/api/message';
import { useUserStore } from '@/store/user';
import type { MessageItem } from '@/types';

const userStore = useUserStore();
const messages = ref<MessageItem[]>([]);

async function loadMessages() {
  const result = await getMessagePage({ current: 1, size: 20 });
  messages.value = result.records;
  userStore.setUnreadCount(messages.value.filter(item => item.readFlag === 0).length);
}

onShow(loadMessages);

async function openMessage(item: MessageItem) {
  if (item.readFlag === 0) {
    await markMessageRead(item.id).catch(() => undefined);
    item.readFlag = 1;
    userStore.setUnreadCount(messages.value.filter(msg => msg.readFlag === 0).length);
  }

  if (item.targetType === 'post' && item.targetId) {
    uni.navigateTo({ url: `/pages/post-detail/index?id=${item.targetId}` });
    return;
  }

  uni.showModal({
    title: item.title,
    content: item.content,
    showCancel: false
  });
}
</script>

<template>
  <view class="safe-page message-page">
    <cw-navbar title="我的消息" />

    <view v-if="messages.length" class="message-list">
      <view
        v-for="item in messages"
        :key="item.id"
        class="card message-card"
        :class="{ unread: item.readFlag === 0 }"
        @tap="openMessage(item)"
      >
        <view class="title-row">
          <text class="title">{{ item.title }}</text>
          <text class="time">{{ item.createdAt || '刚刚' }}</text>
        </view>
        <text class="content">{{ item.content }}</text>
      </view>
    </view>
    <cw-empty v-else text="暂时还没有新消息" />

    <cw-tabbar active="message" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.message-page {
  padding-bottom: 150rpx;
}

.message-list {
  padding: 18rpx 28rpx 0;
}

.message-card {
  margin-bottom: 18rpx;
  padding: 24rpx;
}

.message-card.unread {
  box-shadow:
    0 8rpx 24rpx rgba(15, 35, 60, 0.06),
    inset 8rpx 0 0 $primary-color;
}

.title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16rpx;
}

.title {
  font-size: 28rpx;
  font-weight: 800;
  color: $text-main;
}

.time {
  color: $text-light;
  font-size: 22rpx;
}

.content {
  display: block;
  margin-top: 10rpx;
  font-size: 24rpx;
  line-height: 1.6;
  color: $text-sub;
}
</style>
