<script setup lang="ts">
import { computed, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getLostFoundPage } from '@/api/lostFound';
import type { LostFoundItem } from '@/types';

const tab = ref<1 | 2>(1);
const items = ref<LostFoundItem[]>([]);

const filtered = computed(() => items.value.filter(item => item.type === tab.value));

async function loadList() {
  const result = await getLostFoundPage({ current: 1, size: 20 });
  items.value = result.records;
}

onShow(loadList);

function contact(item: LostFoundItem) {
  uni.showModal({
    title: item.contactName || '联系方式',
    content: `${item.contactPhone || '暂未填写联系方式'}\n${item.lostLocation || ''}`,
    showCancel: false
  });
}

function openDetail(item: LostFoundItem) {
  uni.navigateTo({ url: `/pages/lost-found-detail/index?id=${item.id}` });
}

function publish() {
  uni.navigateTo({ url: '/pages/publish/index?type=lost' });
}
</script>

<template>
  <view class="safe-page list-page">
    <cw-navbar title="失物招领" back @back="uni.navigateBack()">
      <template #right>
        <text @tap="publish">发布</text>
      </template>
    </cw-navbar>

    <view class="segment">
      <text :class="{ active: tab === 1 }" @tap="tab = 1">丢失</text>
      <text :class="{ active: tab === 2 }" @tap="tab = 2">捡到</text>
    </view>

    <view v-if="filtered.length" class="items">
      <cw-lost-card v-for="item in filtered" :key="item.id" :item="item" @click="openDetail" @contact.stop="contact" />
    </view>
    <cw-empty v-else text="还没有相关信息，发布一条试试看吧" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.list-page {
  padding-bottom: 40rpx;
}

.segment {
  margin: 22rpx 28rpx;
  padding: 6rpx;
  display: flex;
  border-radius: 16rpx;
  background: #edf1f4;
}

.segment text {
  flex: 1;
  height: 64rpx;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14rpx;
  color: $text-sub;
  font-size: 28rpx;
}

.segment .active {
  background: $primary-color;
  color: #fff;
  font-weight: 900;
}

.items {
  padding: 0 28rpx;
}
</style>
