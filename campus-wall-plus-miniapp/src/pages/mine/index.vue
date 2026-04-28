<script setup lang="ts">
import { computed, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getMessagePage } from '@/api/message';
import { getProfile } from '@/api/user';
import { useUserStore } from '@/store/user';
import { toAssetUrl } from '@/utils/request';

const userStore = useUserStore();
const stats = ref([
  { label: '我的发布', num: 12 },
  { label: '我的收藏', num: 36 },
  { label: '我的报名', num: 3 },
  { label: '浏览记录', num: 128 }
]);

const menus = [
  { label: '我的发布', url: '/pages/my-publish/index' },
  { label: '我的收藏', url: '/pages/my-collect/index' },
  { label: '我的评论', url: '/pages/my-comments/index' },
  { label: '我的报名', url: '/pages/my-signups/index' },
  { label: '我的消息', url: '/pages/message/index' },
  { label: '我的举报', url: '/pages/my-reports/index' },
  { label: '设置' }
];

const profile = computed(() => userStore.profile);
const schoolLabel = computed(() => {
  const parts = [userStore.tenantName, profile.value?.college].filter(Boolean);
  return parts.join('  ');
});

async function loadMine() {
  const [profileResult, messageResult] = await Promise.all([getProfile(), getMessagePage({ current: 1, size: 20 })]);
  userStore.setProfile(profileResult);
  userStore.setUnreadCount(messageResult.records.filter(item => item.readFlag === 0).length);
}

onShow(loadMine);

function onMenuClick(item: { label: string; url?: string }) {
  if (item.url) {
    uni.navigateTo({ url: item.url });
    return;
  }

  uni.showToast({ title: `${item.label}功能已预留`, icon: 'none' });
}
</script>

<template>
  <view class="safe-page mine-page">
    <view class="hero">
      <view class="hero-actions">
        <text>设</text>
        <view class="message-icon" @tap="onMenuClick({ label: '我的消息', url: '/pages/message/index' })">
          <text>信</text>
          <view v-if="userStore.unreadCount" class="message-badge">{{ userStore.unreadCount }}</view>
        </view>
      </view>
    </view>

    <cw-user-card
      :avatar="toAssetUrl(profile?.avatarUrl || '/static/default-avatar.png')"
      :name="profile?.nickname || '校园同学'"
      :school="schoolLabel || '星河大学  计算机学院'"
    />

    <view class="stats card">
      <view v-for="item in stats" :key="item.label" class="stats-item">
        <text class="stats-num">{{ item.num }}</text>
        <text class="stats-label">{{ item.label }}</text>
      </view>
    </view>

    <view class="menu card">
      <view v-for="item in menus" :key="item.label" class="menu-row" @tap="onMenuClick(item)">
        <text>{{ item.label }}</text>
        <text :class="{ highlight: item.label === '我的消息' && userStore.unreadCount > 0 }">
          {{ item.label === '我的消息' && userStore.unreadCount > 0 ? `${userStore.unreadCount} 条` : '>' }}
        </text>
      </view>
    </view>

    <cw-tabbar active="mine" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.mine-page {
  padding-bottom: 150rpx;
}

.hero {
  height: 220rpx;
  padding: 72rpx 30rpx 0;
  border-bottom-left-radius: 36rpx;
  border-bottom-right-radius: 36rpx;
  background: linear-gradient(135deg, $primary-color, $primary-dark);
}

.hero-actions {
  display: flex;
  justify-content: flex-end;
  gap: 22rpx;
  color: #fff;
  font-size: 34rpx;
}

.message-icon {
  position: relative;
}

.message-badge {
  position: absolute;
  top: -10rpx;
  right: -14rpx;
  min-width: 28rpx;
  height: 28rpx;
  padding: 0 6rpx;
  border-radius: 999rpx;
  background: $danger-color;
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18rpx;
}

.stats {
  margin: 22rpx 28rpx;
  padding: 22rpx;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  text-align: center;
}

.stats-item {
  display: flex;
  flex-direction: column;
}

.stats-num {
  font-size: 30rpx;
  font-weight: 900;
  color: $text-main;
}

.stats-label {
  margin-top: 6rpx;
  font-size: 22rpx;
  color: $text-sub;
}

.menu {
  margin: 0 28rpx;
  overflow: hidden;
}

.menu-row {
  height: 88rpx;
  padding: 0 24rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1rpx solid $border-color;
  font-size: 28rpx;
}

.menu-row:last-child {
  border-bottom: 0;
}

.menu-row text:last-child {
  color: $text-light;
}

.highlight {
  color: $danger-color !important;
}
</style>
