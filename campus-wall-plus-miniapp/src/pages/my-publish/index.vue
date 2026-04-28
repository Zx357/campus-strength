<script setup lang="ts">
import { ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { deleteGoods, getMyGoodsPage, markGoodsSold } from '@/api/goods';
import { deleteHelp, finishHelp, getMyHelpPage } from '@/api/help';
import { deleteLostFound, getMyLostFoundPage, markLostFoundFound } from '@/api/lostFound';
import { deletePost, getMyPostPage } from '@/api/post';
import type { GoodsItem, HelpItem, LostFoundItem, PostItem } from '@/types';

const tab = ref<'post' | 'goods' | 'lost' | 'help'>('post');
const posts = ref<PostItem[]>([]);
const goods = ref<GoodsItem[]>([]);
const losts = ref<LostFoundItem[]>([]);
const helps = ref<HelpItem[]>([]);

async function loadData() {
  const [postResult, goodsResult, lostResult, helpResult] = await Promise.all([
    getMyPostPage({ current: 1, size: 20 }),
    getMyGoodsPage({ current: 1, size: 20 }),
    getMyLostFoundPage({ current: 1, size: 20 }),
    getMyHelpPage({ current: 1, size: 20 })
  ]);
  posts.value = postResult.records;
  goods.value = goodsResult.records;
  losts.value = lostResult.records;
  helps.value = helpResult.records;
}

onShow(loadData);

function editContent(type: 'post' | 'goods' | 'lost', id: number) {
  uni.navigateTo({ url: `/pages/publish/index?type=${type}&id=${id}` });
}

function openPost(item: PostItem) {
  uni.navigateTo({ url: `/pages/post-detail/index?id=${item.id}` });
}

function openGoods(item: GoodsItem) {
  uni.navigateTo({ url: `/pages/goods-detail/index?id=${item.id}` });
}

function openLost(item: LostFoundItem) {
  uni.navigateTo({ url: `/pages/lost-found-detail/index?id=${item.id}` });
}

function openHelp(item: HelpItem) {
  uni.navigateTo({ url: `/pages/help-detail/index?id=${item.id}` });
}

function confirmDelete(type: 'post' | 'goods' | 'lost' | 'help', id: number) {
  uni.showModal({
    title: '确认删除',
    content: '删除后前台将不再展示该内容。',
    success: async res => {
      if (!res.confirm) return;
      if (type === 'post') await deletePost(id);
      if (type === 'goods') await deleteGoods(id);
      if (type === 'lost') await deleteLostFound(id);
      if (type === 'help') await deleteHelp(id);
      uni.showToast({ title: '已删除', icon: 'success' });
      loadData();
    }
  });
}

async function markDone(type: 'goods' | 'lost' | 'help', id: number) {
  if (type === 'goods') await markGoodsSold(id);
  if (type === 'lost') await markLostFoundFound(id);
  if (type === 'help') await finishHelp(id);
  uni.showToast({ title: '状态已更新', icon: 'success' });
  loadData();
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="我的发布" back @back="uni.navigateBack()" />

    <view class="segment">
      <text :class="{ active: tab === 'post' }" @tap="tab = 'post'">动态</text>
      <text :class="{ active: tab === 'goods' }" @tap="tab = 'goods'">闲置</text>
      <text :class="{ active: tab === 'lost' }" @tap="tab = 'lost'">失物</text>
      <text :class="{ active: tab === 'help' }" @tap="tab = 'help'">互助</text>
    </view>

    <view v-if="tab === 'post' && posts.length">
      <view v-for="item in posts" :key="item.id" class="manage-card">
        <cw-post-card :post="item" @click="openPost" />
        <view class="actions">
          <text @tap="editContent('post', item.id)">编辑</text>
          <text class="danger" @tap="confirmDelete('post', item.id)">删除</text>
        </view>
      </view>
    </view>

    <view v-else-if="tab === 'goods' && goods.length" class="waterfall">
      <view v-for="item in goods" :key="item.id" class="manage-card">
        <cw-goods-card :item="item" @click="openGoods" />
        <view class="actions compact">
          <text @tap="editContent('goods', item.id)">编辑</text>
          <text @tap="markDone('goods', item.id)">售出</text>
          <text class="danger" @tap="confirmDelete('goods', item.id)">删除</text>
        </view>
      </view>
    </view>

    <view v-else-if="tab === 'lost' && losts.length" class="list">
      <view v-for="item in losts" :key="item.id" class="manage-card">
        <cw-lost-card :item="item" @click="openLost" />
        <view class="actions">
          <text @tap="editContent('lost', item.id)">编辑</text>
          <text @tap="markDone('lost', item.id)">找回</text>
          <text class="danger" @tap="confirmDelete('lost', item.id)">删除</text>
        </view>
      </view>
    </view>

    <view v-else-if="tab === 'help' && helps.length" class="list">
      <view v-for="item in helps" :key="item.id" class="card help-card" @tap="openHelp(item)">
        <view class="help-title">{{ item.title }}</view>
        <text class="help-content">{{ item.content }}</text>
        <view class="actions">
          <text @tap.stop="markDone('help', item.id)">完成</text>
          <text class="danger" @tap.stop="confirmDelete('help', item.id)">删除</text>
        </view>
      </view>
    </view>

    <cw-empty v-else text="你还没有发布过内容" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page { padding: 0 24rpx 40rpx; }
.segment { margin: 22rpx 4rpx; padding: 6rpx; display: flex; border-radius: 16rpx; background: #edf1f4; }
.segment text { flex: 1; height: 64rpx; display: flex; align-items: center; justify-content: center; border-radius: 14rpx; color: $text-sub; }
.segment .active { background: $primary-color; color: #fff; font-weight: 900; }
.waterfall { column-count: 2; column-gap: 18rpx; }
.manage-card { break-inside: avoid; }
.actions { display: flex; justify-content: flex-end; gap: 20rpx; margin: -10rpx 0 18rpx; padding: 0 12rpx; color: $primary-color; font-size: 24rpx; }
.actions.compact { margin-top: -6rpx; }
.danger { color: $danger-color; }
.help-card { margin-bottom: 18rpx; padding: 24rpx; }
.help-title { color: $text-main; font-size: 30rpx; font-weight: 900; }
.help-content { display: block; margin-top: 12rpx; color: $text-sub; font-size: 26rpx; line-height: 1.6; }
</style>
