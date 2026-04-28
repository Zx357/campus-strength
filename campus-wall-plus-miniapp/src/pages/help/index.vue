<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getHelpPage } from '@/api/help';
import type { HelpItem } from '@/types';

const items = ref<HelpItem[]>([]);

async function loadHelp() {
  const result = await getHelpPage({ current: 1, size: 20 });
  items.value = result.records;
}

onShow(loadHelp);

function publish() {
  uni.navigateTo({ url: '/pages/help-publish/index' });
}

function openHelp(item: HelpItem) {
  uni.navigateTo({ url: `/pages/help-detail/index?id=${item.id}` });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="校园互助">
      <template #right>
        <text @tap="publish">发布</text>
      </template>
    </cw-navbar>

    <view v-if="items.length" class="list">
      <view v-for="item in items" :key="item.id" class="card help-card" @tap="openHelp(item)">
        <view class="title-row">
          <text class="title">{{ item.title }}</text>
          <cw-tag>{{ item.rewardAmount ? `￥${item.rewardAmount}` : '互助' }}</cw-tag>
        </view>
        <text class="content">{{ item.content }}</text>
        <view class="meta">
          <text>{{ item.location || '地点待补充' }}</text>
          <text>{{ item.createdAt || '刚刚' }}</text>
        </view>
      </view>
    </view>
    <cw-empty v-else text="还没有互助内容，稍后来看看" />

    <cw-tabbar active="discover" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page {
  padding: 0 24rpx 150rpx;
}

.list {
  padding-top: 22rpx;
}

.help-card {
  margin-bottom: 18rpx;
  padding: 24rpx;
}

.title-row,
.meta {
  display: flex;
  justify-content: space-between;
  gap: 16rpx;
}

.title {
  font-size: 30rpx;
  font-weight: 900;
  color: $text-main;
}

.content {
  display: block;
  margin: 14rpx 0 18rpx;
  font-size: 26rpx;
  line-height: 1.7;
  color: $text-sub;
}

.meta {
  font-size: 22rpx;
  color: $text-light;
}
</style>
