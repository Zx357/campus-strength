<script setup lang="ts">
import { ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getGoodsDetail, markGoodsSold } from '@/api/goods';
import { createReport } from '@/api/report';
import { useUserStore } from '@/store/user';
import type { GoodsItem } from '@/types';

const userStore = useUserStore();
const detail = ref<GoodsItem | null>(null);

onLoad(async (query: Record<string, string>) => {
  const id = Number(query.id || 0);
  if (id) detail.value = await getGoodsDetail(id);
});

function back() {
  uni.navigateBack();
}

function contact() {
  if (!detail.value) return;
  uni.showModal({
    title: '联系卖家',
    content: detail.value.contactPhone || '卖家暂未填写联系方式',
    showCancel: false
  });
}

async function sold() {
  if (!detail.value) return;
  await markGoodsSold(detail.value.id);
  uni.showToast({ title: '已标记售出', icon: 'success' });
  detail.value.status = 2;
}

async function report() {
  if (!detail.value) return;
  if (userStore.isGuest) {
    uni.showToast({ title: '请登录后举报', icon: 'none' });
    return;
  }
  await createReport({
    targetType: 'goods',
    targetId: detail.value.id,
    reasonType: '商品问题',
    reasonContent: '商品信息可能不实'
  });
  uni.showToast({ title: '举报已提交', icon: 'success' });
}
</script>

<template>
  <view class="safe-page page">
    <cw-navbar title="商品详情" back @back="back">
      <template #right>
        <text @tap="report">举报</text>
      </template>
    </cw-navbar>

    <template v-if="detail">
      <swiper v-if="detail.images.length" class="swiper" indicator-dots circular>
        <swiper-item v-for="image in detail.images" :key="image">
          <image class="cover" :src="image" mode="aspectFill" />
        </swiper-item>
      </swiper>
      <view v-else class="cover placeholder">校园墙+</view>

      <view class="card info">
        <view class="price-row">
          <text class="price">￥{{ detail.price }}</text>
          <text v-if="detail.originalPrice" class="origin">￥{{ detail.originalPrice }}</text>
          <cw-status-badge :type="detail.status === 2 ? 'gray' : 'success'">
            {{ detail.status === 2 ? '已售出' : '在售' }}
          </cw-status-badge>
        </view>
        <view class="title">{{ detail.title }}</view>
        <view class="tags">
          <cw-tag>{{ detail.conditionLevel || '成色佳' }}</cw-tag>
          <cw-tag>{{ detail.tradeMethod || '自提' }}</cw-tag>
        </view>
        <text class="desc">{{ detail.description || '卖家还没有填写详细描述' }}</text>
      </view>

      <view class="card meta">
        <view><text>交易地点</text><text>{{ detail.location || '待沟通' }}</text></view>
        <view><text>发布时间</text><text>{{ detail.createdAt || '刚刚' }}</text></view>
        <view><text>卖家</text><text>{{ detail.sellerName || '校园同学' }}</text></view>
      </view>

      <view class="bottom-bar">
        <button class="ghost" @tap="sold">标记售出</button>
        <button class="primary" @tap="contact">联系卖家</button>
      </view>
    </template>
    <cw-empty v-else text="商品信息加载中" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.page { padding-bottom: 150rpx; }
.swiper, .cover { width: 100%; height: 520rpx; }
.cover { background: linear-gradient(135deg, #eaf8f1, #dbeafe); }
.placeholder { display: flex; align-items: center; justify-content: center; color: $primary-color; font-weight: 900; }
.info, .meta { margin: 24rpx; padding: 26rpx; }
.price-row { display: flex; align-items: center; gap: 16rpx; }
.price { color: $danger-color; font-size: 42rpx; font-weight: 900; }
.origin { color: $text-light; text-decoration: line-through; }
.title { margin-top: 18rpx; color: $text-main; font-size: 34rpx; font-weight: 900; }
.tags { display: flex; gap: 12rpx; margin: 18rpx 0; }
.desc { color: $text-sub; font-size: 28rpx; line-height: 1.7; }
.meta view { display: flex; justify-content: space-between; padding: 18rpx 0; color: $text-sub; font-size: 26rpx; border-bottom: 1rpx solid $border-color; }
.meta view:last-child { border-bottom: 0; }
.bottom-bar { position: fixed; left: 0; right: 0; bottom: 0; display: flex; gap: 18rpx; padding: 18rpx 28rpx 34rpx; background: #fff; box-shadow: 0 -8rpx 24rpx rgba(15, 35, 60, 0.06); }
button { flex: 1; height: 82rpx; border-radius: 999rpx; font-size: 28rpx; }
.ghost { color: $primary-color; background: $primary-light; }
.primary { color: #fff; background: $primary-color; }
</style>
