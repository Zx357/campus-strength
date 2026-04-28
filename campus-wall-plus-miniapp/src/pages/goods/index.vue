<script setup lang="ts">
import { computed, ref } from 'vue';
import { onShow } from '@dcloudio/uni-app';
import { getGoodsPage } from '@/api/goods';
import type { GoodsItem } from '@/types';

const category = ref('全部');
const categories = ['全部', '数码电子', '图书教材', '生活用品', '服饰鞋包'];
const goods = ref<GoodsItem[]>([]);

const filteredGoods = computed(() => {
  if (category.value === '全部') return goods.value;
  return goods.value.filter(item => item.title.includes(category.value.slice(0, 2)));
});

async function loadGoods() {
  const result = await getGoodsPage({ current: 1, size: 20 });
  goods.value = result.records;
}

onShow(loadGoods);

function publish() {
  uni.navigateTo({ url: '/pages/publish/index?type=goods' });
}

function openGoods(item: GoodsItem) {
  uni.navigateTo({ url: `/pages/goods-detail/index?id=${item.id}` });
}
</script>

<template>
  <view class="safe-page goods-page">
    <cw-navbar title="二手闲置">
      <template #right>
        <text @tap="publish">发布</text>
      </template>
    </cw-navbar>

    <scroll-view scroll-x class="categories">
      <text v-for="item in categories" :key="item" :class="{ active: category === item }" @tap="category = item">
        {{ item }}
      </text>
    </scroll-view>

    <view v-if="filteredGoods.length" class="waterfall">
      <cw-goods-card v-for="item in filteredGoods" :key="item.id" :item="item" @click="openGoods" />
    </view>
    <cw-empty v-else text="还没有闲置商品，来发布第一件吧" />

    <cw-tabbar active="discover" />
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.goods-page {
  padding: 0 22rpx 150rpx;
}

.categories {
  margin: 22rpx 0;
  white-space: nowrap;
}

.categories text {
  display: inline-flex;
  margin-right: 16rpx;
  padding: 14rpx 24rpx;
  border-radius: 999rpx;
  background: #fff;
  color: $text-sub;
  font-size: 24rpx;
}

.categories .active {
  background: $primary-color;
  color: #fff;
}

.waterfall {
  column-count: 2;
  column-gap: 18rpx;
}
</style>
