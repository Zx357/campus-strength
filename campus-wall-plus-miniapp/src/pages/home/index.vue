<script setup lang="ts">
import { computed, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getMessagePage } from '@/api/message';
import { getNoticePage } from '@/api/notice';
import { toggleCollect, toggleLike, getPostPage } from '@/api/post';
import { getProfile } from '@/api/user';
import { useUserStore } from '@/store/user';
import type { NoticeItem, PostItem } from '@/types';

const userStore = useUserStore();
const searchKeyword = ref('');
const tab = ref<'latest' | 'hot' | 'follow'>('latest');
const posts = ref<PostItem[]>([]);
const notice = ref<NoticeItem | null>(null);

const tabs = [
  { label: '最新', value: 'latest' as const },
  { label: '推荐', value: 'hot' as const },
  { label: '关注', value: 'follow' as const }
];

const categories = [
  { text: '校园墙', icon: '墙', bg: 'linear-gradient(135deg, #FFB547, #FF9D2E)', url: '/pages/home/index' },
  { text: '失物招领', icon: '失', bg: 'linear-gradient(135deg, #4DA3FF, #6F8CFF)', url: '/pages/lost-found/index' },
  { text: '二手闲置', icon: '闲', bg: 'linear-gradient(135deg, #FFB547, #F97316)', url: '/pages/goods/index' },
  { text: '校园互助', icon: '助', bg: 'linear-gradient(135deg, #22C6A3, #18B66A)', url: '/pages/help/index' },
  { text: '活动社团', icon: '社', bg: 'linear-gradient(135deg, #FFD76A, #FFB547)', url: '/pages/activity/index' },
  { text: '公告通知', icon: '告', bg: 'linear-gradient(135deg, #FF8A8A, #FF5A5F)', url: '/pages/message/index' }
];

const schoolName = computed(() => userStore.tenantName || '星河大学');

async function loadHomeData() {
  const [postResult, noticeResult, profile, messageResult] = await Promise.all([
    getPostPage({
      current: 1,
      size: 10,
      keyword: searchKeyword.value,
      sort: tab.value === 'hot' ? 'hot' : ''
    }),
    getNoticePage({ current: 1, size: 3 }),
    getProfile(),
    getMessagePage({ current: 1, size: 20 })
  ]);

  posts.value = postResult.records.map(item => ({
    ...item,
    liked: item.liked || false,
    collected: item.collected || false
  }));
  notice.value = noticeResult.records[0] || null;
  userStore.setProfile(profile);
  userStore.setUnreadCount(messageResult.records.filter(item => item.readFlag === 0).length);
}

onShow(loadHomeData);

async function handleLike(post: PostItem) {
  if (userStore.isGuest) {
    uni.showToast({ title: '游客只能浏览，登录后可点赞', icon: 'none' });
    return;
  }

  const liked = await toggleLike(post.id).catch(() => !post.liked);
  posts.value = posts.value.map(item =>
    item.id === post.id
      ? { ...item, liked, likeCount: liked ? item.likeCount + 1 : Math.max(0, item.likeCount - 1) }
      : item
  );
}

async function handleCollect(post: PostItem) {
  if (userStore.isGuest) {
    uni.showToast({ title: '游客只能浏览，登录后可收藏', icon: 'none' });
    return;
  }

  const collected = await toggleCollect(post.id).catch(() => !post.collected);
  posts.value = posts.value.map(item =>
    item.id === post.id
      ? { ...item, collected, collectCount: collected ? item.collectCount + 1 : Math.max(0, item.collectCount - 1) }
      : item
  );
}

function goPublish() {
  uni.navigateTo({ url: '/pages/publish/index' });
}

function onCategory(item: any) {
  if (item.url) {
    uni.navigateTo({ url: item.url });
  }
}

function openPostDetail(post: PostItem) {
  uni.navigateTo({ url: `/pages/post-detail/index?id=${post.id}` });
}

function switchTenant() {
  uni.reLaunch({ url: '/pages/login/index' });
}

function goMessage() {
  uni.navigateTo({ url: '/pages/message/index' });
}
</script>

<template>
  <view class="safe-page home-page">
    <view class="top-bar">
      <view class="school" @tap="switchTenant">
        <text>{{ schoolName }}</text>
        <view class="arrow" />
      </view>
      <cw-search-bar
        v-model="searchKeyword"
        class="search"
        placeholder="搜索内容、用户、话题"
        @confirm="loadHomeData"
      />
      <view class="message-entry" @tap="goMessage">
        <text>信</text>
        <view v-if="userStore.unreadCount" class="badge">{{ userStore.unreadCount }}</view>
      </view>
    </view>

    <view class="banner">
      <view class="banner-copy">
        <text class="banner-title">新学期 · 新开始</text>
        <text class="banner-sub">遇见更好的校园生活</text>
        <text v-if="notice" class="banner-tip">{{ notice.title }}</text>
      </view>
      <image class="banner-image" src="/static/banner-campus.png" mode="aspectFill" />
    </view>

    <view class="card category-card">
      <cw-category-grid :items="categories" @select="onCategory" />
    </view>

    <view class="tab-row">
      <view class="tabs">
        <text v-for="item in tabs" :key="item.value" :class="{ active: tab === item.value }" @tap="tab = item.value; loadHomeData()">
          {{ item.label }}
        </text>
      </view>
      <text class="filter">筛</text>
    </view>

    <view v-if="posts.length" class="list">
      <cw-post-card
        v-for="post in posts"
        :key="post.id"
        :post="post"
        @click="openPostDetail"
        @like="handleLike"
        @collect="handleCollect"
      />
    </view>
    <cw-empty v-else text="还没有内容，快来发布第一条动态吧" />

    <view class="float-publish" @tap="goPublish">+</view>
    <cw-tabbar active="home" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.home-page {
  padding: 88rpx 24rpx 150rpx;
}

.top-bar {
  display: flex;
  align-items: center;
  gap: 16rpx;
}

.school {
  display: flex;
  align-items: center;
  gap: 4rpx;
  white-space: nowrap;
  font-size: 30rpx;
  font-weight: 900;
  color: $text-main;
}

.arrow {
  width: 12rpx;
  height: 12rpx;
  border-top: 3rpx solid $text-light;
  border-right: 3rpx solid $text-light;
  transform: rotate(45deg);
}

.search {
  flex: 1;
}

.message-entry {
  position: relative;
  width: 52rpx;
  height: 52rpx;
  border-radius: 50%;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: $text-main;
}

.badge {
  position: absolute;
  top: -6rpx;
  right: -6rpx;
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

.banner {
  position: relative;
  display: flex;
  align-items: center;
  height: 182rpx;
  margin: 24rpx 0;
  padding: 28rpx;
  border-radius: 24rpx;
  overflow: hidden;
  background: linear-gradient(135deg, #2f9cff, #2bd283);
}

.banner-copy {
  position: relative;
  z-index: 2;
  display: flex;
  flex-direction: column;
  color: #fff;
}

.banner-title {
  font-size: 38rpx;
  font-weight: 900;
}

.banner-sub {
  margin-top: 10rpx;
  font-size: 24rpx;
  opacity: 0.95;
}

.banner-tip {
  margin-top: 16rpx;
  font-size: 22rpx;
  opacity: 0.85;
}

.banner-image {
  position: absolute;
  right: -8rpx;
  bottom: 0;
  width: 260rpx;
  height: 182rpx;
  opacity: 0.32;
}

.category-card {
  margin-bottom: 18rpx;
}

.tab-row {
  height: 72rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 8rpx;
}

.tabs {
  display: flex;
  gap: 42rpx;
}

.tabs text {
  position: relative;
  color: $text-sub;
  font-size: 26rpx;
}

.tabs .active {
  color: $text-main;
  font-weight: 900;
}

.tabs .active::after {
  content: '';
  position: absolute;
  left: 0;
  right: 0;
  bottom: -14rpx;
  height: 6rpx;
  border-radius: 6rpx;
  background: $primary-color;
}

.filter {
  color: $text-light;
  font-size: 28rpx;
}

.list {
  padding-top: 14rpx;
}

.float-publish {
  position: fixed;
  right: 34rpx;
  bottom: 156rpx;
  z-index: 12;
  width: 96rpx;
  height: 96rpx;
  border-radius: 50%;
  background: linear-gradient(135deg, $primary-color, $primary-dark);
  color: #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 58rpx;
  box-shadow: 0 14rpx 24rpx rgba(24, 182, 106, 0.32);
}
</style>
