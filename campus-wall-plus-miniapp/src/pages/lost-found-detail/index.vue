<script setup lang="ts">
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getLostFoundDetail, markLostFoundFound } from '@/api/lostFound';
import { createReport } from '@/api/report';
import { useUserStore } from '@/store/user';
import type { LostFoundItem } from '@/types';

const userStore = useUserStore();
const detail = ref<LostFoundItem | null>(null);

onLoad(async (query: Record<string, string>) => {
  const id = Number(query.id || 0);
  if (id) detail.value = await getLostFoundDetail(id);
});

function contact() {
  if (!detail.value) return;
  uni.showModal({
    title: detail.value.contactName || '联系方式',
    content: detail.value.contactPhone || '暂未填写联系方式',
    showCancel: false
  });
}

async function found() {
  if (!detail.value) return;
  await markLostFoundFound(detail.value.id);
  uni.showToast({ title: '已标记找回', icon: 'success' });
  detail.value.status = 2;
}

async function report() {
  if (!detail.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '请登录后举报', icon: 'none' });
    return;
  }
  await createReport({
    targetType: 'lost',
    targetId: detail.value.id,
    reasonType: '信息问题',
    reasonContent: '失物招领信息可能不实'
  });
  uni.showToast({ title: '举报已提交', icon: 'success' });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="失物详情" back @back="uni.navigateBack()">
      <template #right>
        <text @tap="report">举报</text>
      </template>
    </cw-navbar>

    <template v-if="detail">
      <view class="card header">
        <view>
          <cw-status-badge :type="detail.status === 2 ? 'gray' : 'success'">
            {{ detail.status === 2 ? '已找回' : detail.type === 1 ? '寻找中' : '待认领' }}
          </cw-status-badge>
          <text class="type">{{ detail.type === 1 ? '丢失物品' : '捡到物品' }}</text>
        </view>
        <text class="title">{{ detail.title }}</text>
        <text class="desc">{{ detail.description || '暂无详细描述' }}</text>
      </view>

      <view v-if="detail.images.length" class="image-grid">
        <image v-for="img in detail.images" :key="img" :src="img" mode="aspectFill" />
      </view>

      <view class="card meta">
        <view><text>地点</text><text>{{ detail.lostLocation || '待补充' }}</text></view>
        <view><text>时间</text><text>{{ detail.lostTime || detail.createdAt || '刚刚' }}</text></view>
        <view><text>联系人</text><text>{{ detail.contactName || '未填写' }}</text></view>
      </view>

      <view class="bottom-bar">
        <button class="ghost" @tap="found">标记找回</button>
        <button class="primary" @tap="contact">联系TA</button>
      </view>
    </template>
    <cw-empty v-else text="信息加载中" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page { padding: 24rpx 24rpx 150rpx; }
.header { padding: 28rpx; }
.header > view { display: flex; align-items: center; gap: 14rpx; }
.type { color: $text-sub; font-size: 24rpx; }
.title { display: block; margin: 20rpx 0 14rpx; color: $text-main; font-size: 36rpx; font-weight: 900; }
.desc { color: $text-sub; font-size: 28rpx; line-height: 1.7; }
.image-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12rpx; margin: 22rpx 0; }
.image-grid image { width: 100%; height: 210rpx; border-radius: 16rpx; background: #edf1f4; }
.meta { padding: 22rpx 28rpx; }
.meta view { display: flex; justify-content: space-between; padding: 18rpx 0; border-bottom: 1rpx solid $border-color; color: $text-sub; font-size: 26rpx; }
.meta view:last-child { border-bottom: 0; }
.bottom-bar { position: fixed; left: 0; right: 0; bottom: 0; display: flex; gap: 18rpx; padding: 18rpx 28rpx 34rpx; background: #fff; }
button { flex: 1; height: 82rpx; border-radius: 999rpx; font-size: 28rpx; }
.ghost { color: $primary-color; background: $primary-light; }
.primary { color: #fff; background: $primary-color; }
</style>
