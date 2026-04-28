<script setup lang="ts">
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { finishHelp, getHelpDetail } from '@/api/help';
import { createReport } from '@/api/report';
import { useUserStore } from '@/store/user';
import type { HelpItem } from '@/types';

const userStore = useUserStore();
const detail = ref<HelpItem | null>(null);

onLoad(async (query: Record<string, string>) => {
  const id = Number(query.id || 0);
  if (id) detail.value = await getHelpDetail(id);
});

async function finish() {
  if (!detail.value) return;
  await finishHelp(detail.value.id);
  uni.showToast({ title: '已标记完成', icon: 'success' });
  detail.value.status = 2;
}

async function report() {
  if (!detail.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '请登录后举报', icon: 'none' });
    return;
  }
  await createReport({
    targetType: 'help',
    targetId: detail.value.id,
    reasonType: '互助问题',
    reasonContent: '互助内容可能不实'
  });
  uni.showToast({ title: '举报已提交', icon: 'success' });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="互助详情" back @back="uni.navigateBack()">
      <template #right>
        <text @tap="report">举报</text>
      </template>
    </cw-navbar>

    <template v-if="detail">
      <view class="card header">
        <view class="title-row">
          <text class="title">{{ detail.title }}</text>
          <cw-status-badge :type="detail.status === 2 ? 'gray' : 'success'">
            {{ detail.status === 2 ? '已完成' : '进行中' }}
          </cw-status-badge>
        </view>
        <text class="content">{{ detail.content }}</text>
      </view>

      <view v-if="detail.images?.length" class="image-grid">
        <image v-for="img in detail.images" :key="img" :src="img" mode="aspectFill" />
      </view>

      <view class="card meta">
        <view><text>地点</text><text>{{ detail.location || '待沟通' }}</text></view>
        <view><text>酬谢</text><text>{{ detail.rewardAmount ? `￥${detail.rewardAmount}` : '可商量' }}</text></view>
        <view><text>发布时间</text><text>{{ detail.createdAt || '刚刚' }}</text></view>
      </view>

      <view class="bottom-bar">
        <button class="ghost" @tap="finish">标记完成</button>
        <button class="primary" open-type="contact">联系TA</button>
      </view>
    </template>
    <cw-empty v-else text="互助内容加载中" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page { padding: 24rpx 24rpx 150rpx; }
.header { padding: 28rpx; }
.title-row { display: flex; align-items: center; justify-content: space-between; gap: 16rpx; }
.title { color: $text-main; font-size: 36rpx; font-weight: 900; }
.content { display: block; margin-top: 20rpx; color: $text-sub; font-size: 28rpx; line-height: 1.8; }
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
