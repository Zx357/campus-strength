<script setup lang="ts">
import { computed, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { uploadImage } from '@/api/file';
import { getGoodsDetail, publishGoods, updateGoods } from '@/api/goods';
import { getLostFoundDetail, publishLostFound, updateLostFound } from '@/api/lostFound';
import { getPostDetail, publishPost, updatePost } from '@/api/post';
import { useUserStore } from '@/store/user';
import type { PublishMode } from '@/types';

const userStore = useUserStore();

const mode = ref<PublishMode>('post');
const editId = ref<number | null>(null);
const content = ref('');
const images = ref<string[]>([]);
const anonymous = ref(false);
const topic = ref('');
const location = ref('');
const loading = ref(false);

const lostType = ref<1 | 2>(1);
const lostTitle = ref('');
const lostContactName = ref('');
const lostContactPhone = ref('');
const goodsTitle = ref('');
const goodsPrice = ref('');
const goodsOriginalPrice = ref('');
const goodsCondition = ref('95新');
const goodsTradeMethod = ref('自提');
const goodsContactPhone = ref('');

const tabs = [
  { label: '发动态', value: 'post' as const },
  { label: '失物招领', value: 'lost' as const },
  { label: '二手闲置', value: 'goods' as const }
];

onLoad(async (query: Record<string, string>) => {
  if (query.type === 'lost') mode.value = 'lost';
  if (query.type === 'goods') mode.value = 'goods';
  if (query.type === 'post') mode.value = 'post';
  if (query.id) {
    editId.value = Number(query.id);
    await loadEditData();
  }
});

const pageTitle = computed(() => {
  const prefix = editId.value ? '编辑' : '发布';
  if (mode.value === 'lost') return `${prefix}失物招领`;
  if (mode.value === 'goods') return `${prefix}二手闲置`;
  return `${prefix}动态`;
});

async function loadEditData() {
  if (!editId.value) return;
  if (mode.value === 'post') {
    const detail = await getPostDetail(editId.value);
    content.value = detail.content;
    images.value = detail.images || [];
    topic.value = detail.topicTags?.[0] || '';
    location.value = detail.location || '';
    anonymous.value = detail.anonymous === 1;
  }
  if (mode.value === 'lost') {
    const detail = await getLostFoundDetail(editId.value);
    lostType.value = detail.type;
    lostTitle.value = detail.title;
    content.value = detail.description || '';
    images.value = detail.images || [];
    location.value = detail.lostLocation || '';
    lostContactName.value = detail.contactName || '';
    lostContactPhone.value = detail.contactPhone || '';
  }
  if (mode.value === 'goods') {
    const detail = await getGoodsDetail(editId.value);
    goodsTitle.value = detail.title;
    content.value = detail.description || '';
    images.value = detail.images || [];
    goodsPrice.value = String(detail.price || '');
    goodsOriginalPrice.value = detail.originalPrice ? String(detail.originalPrice) : '';
    goodsCondition.value = detail.conditionLevel || '95新';
    goodsTradeMethod.value = detail.tradeMethod || '自提';
    goodsContactPhone.value = detail.contactPhone || '';
    location.value = detail.location || '';
  }
}

function back() {
  uni.navigateBack({ fail: () => uni.reLaunch({ url: '/pages/home/index' }) });
}

function chooseImage() {
  uni.chooseImage({
    count: 9 - images.value.length,
    success: res => {
      images.value = [...images.value, ...res.tempFilePaths].slice(0, 9);
    }
  });
}

function removeImage(index: number) {
  images.value.splice(index, 1);
}

async function uploadImageList() {
  const uploaded: string[] = [];
  for (const item of images.value) {
    if (/^(https?:)?\/\//.test(item) || item.startsWith('/uploads/')) {
      uploaded.push(item);
      continue;
    }
    const result = await uploadImage(item);
    uploaded.push(result.fileUrl || result.url);
  }
  return uploaded;
}

function requireLogin() {
  if (!userStore.isLogin) {
    uni.showToast({ title: '请先登录', icon: 'none' });
    return false;
  }
  if (userStore.isGuest) {
    uni.showToast({ title: '游客不能发布内容', icon: 'none' });
    return false;
  }
  return true;
}

async function submit() {
  if (!requireLogin() || loading.value) return;
  loading.value = true;
  try {
    const uploadedImages = await uploadImageList();
    if (mode.value === 'post') {
      if (!content.value.trim()) {
        uni.showToast({ title: '内容不能为空', icon: 'none' });
        return;
      }
      const payload = {
        title: content.value.trim().slice(0, 20),
        content: content.value.trim(),
        images: JSON.stringify(uploadedImages),
        topicTags: JSON.stringify(topic.value ? [topic.value] : []),
        location: location.value,
        anonymous: anonymous.value ? 1 : 0
      };
      if (editId.value) await updatePost(editId.value, payload);
      else await publishPost(payload);
    }
    if (mode.value === 'lost') {
      if (!lostTitle.value.trim() || !content.value.trim()) {
        uni.showToast({ title: '请填写标题和描述', icon: 'none' });
        return;
      }
      const payload = {
        id: editId.value,
        type: lostType.value,
        title: lostTitle.value.trim(),
        description: content.value.trim(),
        images: JSON.stringify(uploadedImages),
        lostTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
        lostLocation: location.value,
        contactName: lostContactName.value,
        contactPhone: lostContactPhone.value
      };
      if (editId.value) await updateLostFound(payload);
      else await publishLostFound(payload);
    }
    if (mode.value === 'goods') {
      if (!goodsTitle.value.trim() || !goodsPrice.value.trim()) {
        uni.showToast({ title: '请填写商品标题和价格', icon: 'none' });
        return;
      }
      const payload = {
        id: editId.value,
        title: goodsTitle.value.trim(),
        description: content.value.trim(),
        images: JSON.stringify(uploadedImages),
        price: Number(goodsPrice.value || 0),
        originalPrice: Number(goodsOriginalPrice.value || 0),
        conditionLevel: goodsCondition.value,
        tradeMethod: goodsTradeMethod.value,
        location: location.value,
        contactPhone: goodsContactPhone.value
      };
      if (editId.value) await updateGoods(payload);
      else await publishGoods(payload);
    }
    uni.showToast({ title: editId.value ? '修改成功' : '发布成功', icon: 'success' });
    setTimeout(() => uni.reLaunch({ url: '/pages/home/index' }), 400);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <view class="publish-page">
    <cw-navbar :title="pageTitle" back @back="back" />

    <view class="tabs">
      <text v-for="item in tabs" :key="item.value" :class="{ active: mode === item.value }" @tap="!editId && (mode = item.value)">
        {{ item.label }}
      </text>
    </view>

    <view class="editor">
      <template v-if="mode === 'lost'">
        <view class="switch-row">
          <text :class="{ active: lostType === 1 }" @tap="lostType = 1">丢失</text>
          <text :class="{ active: lostType === 2 }" @tap="lostType = 2">捡到</text>
        </view>
        <input v-model="lostTitle" class="title-input" placeholder="请输入物品标题" />
      </template>

      <template v-if="mode === 'goods'">
        <input v-model="goodsTitle" class="title-input" placeholder="请输入商品标题" />
      </template>

      <textarea v-model="content" maxlength="500" placeholder="分享你的校园生活吧..." />
      <text class="count">{{ content.length }}/500</text>
    </view>

    <view class="upload-wrap">
      <cw-upload-grid :list="images" @add="chooseImage" @remove="removeImage" />
    </view>

    <view class="form">
      <view v-if="mode === 'post'" class="row">
        <text>选择话题</text>
        <input v-model="topic" placeholder="例如：晚霞、求助、吐槽" placeholder-class="placeholder" />
      </view>
      <view class="row">
        <text>所在位置</text>
        <input v-model="location" placeholder="例如：图书馆、教学楼、操场" placeholder-class="placeholder" />
      </view>
      <view v-if="mode === 'post'" class="row">
        <text>匿名发布</text>
        <switch :checked="anonymous" color="#18B66A" @change="anonymous = $event.detail.value" />
      </view>
      <view v-if="mode === 'lost'" class="row">
        <text>联系人</text>
        <input v-model="lostContactName" placeholder="请输入联系人" placeholder-class="placeholder" />
      </view>
      <view v-if="mode === 'lost'" class="row">
        <text>联系电话</text>
        <input v-model="lostContactPhone" placeholder="请输入联系电话" placeholder-class="placeholder" />
      </view>
      <view v-if="mode === 'goods'" class="row">
        <text>商品价格</text>
        <input v-model="goodsPrice" type="number" placeholder="请输入价格" placeholder-class="placeholder" />
      </view>
      <view v-if="mode === 'goods'" class="row">
        <text>原价</text>
        <input v-model="goodsOriginalPrice" type="number" placeholder="可选" placeholder-class="placeholder" />
      </view>
      <view v-if="mode === 'goods'" class="row">
        <text>商品成色</text>
        <input v-model="goodsCondition" placeholder="例如：99新、95新" placeholder-class="placeholder" />
      </view>
      <view v-if="mode === 'goods'" class="row">
        <text>交易方式</text>
        <input v-model="goodsTradeMethod" placeholder="例如：自提、送货" placeholder-class="placeholder" />
      </view>
      <view v-if="mode === 'goods'" class="row">
        <text>联系电话</text>
        <input v-model="goodsContactPhone" placeholder="请输入联系电话" placeholder-class="placeholder" />
      </view>
    </view>

    <view class="primary-btn submit-btn" @tap="submit">{{ loading ? '提交中...' : editId ? '保存修改' : '发布' }}</view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.publish-page { min-height: 100vh; background: #fff; padding-bottom: 48rpx; }
.tabs { height: 84rpx; display: flex; align-items: center; justify-content: center; gap: 70rpx; border-bottom: 1rpx solid $border-color; }
.tabs text { position: relative; color: $text-sub; font-size: 28rpx; }
.tabs .active { color: $primary-color; font-weight: 900; }
.tabs .active::after { content: ''; position: absolute; left: 8rpx; right: 8rpx; bottom: -24rpx; height: 6rpx; border-radius: 6rpx; background: $primary-color; }
.editor { position: relative; padding: 34rpx 30rpx 18rpx; }
.switch-row { display: flex; gap: 16rpx; margin-bottom: 18rpx; }
.switch-row text { min-width: 112rpx; height: 62rpx; padding: 0 24rpx; display: flex; align-items: center; justify-content: center; border-radius: 999rpx; background: #edf1f4; color: $text-sub; font-size: 24rpx; }
.switch-row .active { background: $primary-color; color: #fff; font-weight: 800; }
.title-input { width: 100%; height: 72rpx; margin-bottom: 18rpx; padding: 0 20rpx; border-radius: 18rpx; background: #f7f9fb; font-size: 28rpx; }
textarea { width: 100%; height: 250rpx; font-size: 28rpx; line-height: 1.7; color: $text-main; }
.count { position: absolute; right: 34rpx; bottom: 20rpx; color: $text-light; font-size: 24rpx; }
.upload-wrap { padding: 22rpx 30rpx; }
.form { margin-top: 12rpx; border-top: 1rpx solid $border-color; }
.row { min-height: 92rpx; padding: 0 30rpx; display: flex; align-items: center; justify-content: space-between; gap: 20rpx; border-bottom: 1rpx solid $border-color; font-size: 28rpx; }
.row input { flex: 1; height: 92rpx; text-align: right; font-size: 26rpx; color: $text-main; }
:deep(.placeholder) { color: $text-light; }
.submit-btn { margin: 44rpx 30rpx 0; }
</style>
