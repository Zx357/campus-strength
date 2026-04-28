<script setup lang="ts">
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getActivityDetail, signupActivity } from '@/api/activity';
import { useUserStore } from '@/store/user';
import type { ActivityItem } from '@/types';

const userStore = useUserStore();
const detail = ref<ActivityItem | null>(null);
const signing = ref(false);

onLoad(async (query: Record<string, string>) => {
  detail.value = await getActivityDetail(Number(query.id || 0));
});

async function signup() {
  if (!detail.value || signing.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '游客模式下不能报名', icon: 'none' });
    return;
  }

  signing.value = true;
  try {
    await signupActivity(detail.value.id);
    uni.showToast({ title: '报名成功', icon: 'success' });
  } finally {
    signing.value = false;
  }
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="活动详情" back @back="uni.navigateBack()" />

    <view v-if="detail" class="detail">
      <view class="cover" />
      <view class="card content-card">
        <text class="title">{{ detail.title }}</text>
        <view class="meta">
          <text>时间：{{ detail.activityTime || '待定' }}</text>
          <text>地点：{{ detail.activityLocation || '待定' }}</text>
          <text>主办：{{ detail.organizer || '校园社团' }}</text>
        </view>
        <text class="content">{{ detail.content }}</text>
      </view>
      <view class="primary-btn" @tap="signup">{{ signing ? '报名中...' : '我要报名' }}</view>
    </view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page {
  padding: 0 24rpx 48rpx;
}

.cover {
  height: 280rpx;
  margin-top: 22rpx;
  border-radius: 24rpx;
  background: linear-gradient(135deg, #4da3ff, #18b66a);
}

.content-card {
  margin-top: 22rpx;
  padding: 28rpx;
}

.title {
  font-size: 36rpx;
  font-weight: 900;
  color: $text-main;
}

.meta {
  display: flex;
  flex-direction: column;
  gap: 10rpx;
  margin: 22rpx 0;
  color: $text-sub;
  font-size: 25rpx;
}

.content {
  font-size: 28rpx;
  line-height: 1.8;
  color: $text-main;
}

.primary-btn {
  margin-top: 30rpx;
}
</style>
