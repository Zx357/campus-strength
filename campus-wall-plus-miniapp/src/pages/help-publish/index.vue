<script setup lang="ts">
import { ref } from 'vue';
import { publishHelp } from '@/api/help';
import { useUserStore } from '@/store/user';

const userStore = useUserStore();
const title = ref('');
const content = ref('');
const location = ref('');
const rewardAmount = ref('');
const type = ref<1 | 2 | 3 | 4 | 5>(1);
const loading = ref(false);

const types = [
  { label: '求助', value: 1 },
  { label: '代取', value: 2 },
  { label: '拼单', value: 3 },
  { label: '咨询', value: 4 },
  { label: '其他', value: 5 }
];

function back() {
  uni.navigateBack();
}

async function submit() {
  if (userStore.isGuest) {
    uni.showToast({ title: '游客不能发布互助', icon: 'none' });
    return;
  }

  if (!title.value.trim() || !content.value.trim()) {
    uni.showToast({ title: '请填写标题和内容', icon: 'none' });
    return;
  }

  if (loading.value) return;
  loading.value = true;
  try {
    await publishHelp({
      type: type.value,
      title: title.value.trim(),
      content: content.value.trim(),
      location: location.value,
      rewardAmount: Number(rewardAmount.value || 0),
      images: JSON.stringify([])
    });
    uni.showToast({ title: '发布成功，待审核', icon: 'success' });
    setTimeout(() => uni.navigateBack(), 400);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <view class="publish-page">
    <cw-navbar title="发布互助" back @back="back" />

    <view class="type-row">
      <text v-for="item in types" :key="item.value" :class="{ active: type === item.value }" @tap="type = item.value">
        {{ item.label }}
      </text>
    </view>

    <view class="form card">
      <input v-model="title" placeholder="互助标题" placeholder-class="placeholder" />
      <textarea v-model="content" maxlength="300" placeholder="说清楚你需要什么帮助..." placeholder-class="placeholder" />
      <input v-model="location" placeholder="地点，例如：图书馆一楼" placeholder-class="placeholder" />
      <input v-model="rewardAmount" type="number" placeholder="酬谢金额，可不填" placeholder-class="placeholder" />
    </view>

    <view class="primary-btn submit" @tap="submit">{{ loading ? '提交中...' : '发布' }}</view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.publish-page {
  min-height: 100vh;
  padding: 0 24rpx 48rpx;
}

.type-row {
  display: flex;
  gap: 14rpx;
  padding: 24rpx 0;
  overflow-x: auto;
  white-space: nowrap;
}

.type-row text {
  padding: 14rpx 24rpx;
  border-radius: 999rpx;
  background: #fff;
  color: $text-sub;
  font-size: 24rpx;
}

.type-row .active {
  background: $primary-color;
  color: #fff;
  font-weight: 800;
}

.form {
  padding: 8rpx 24rpx;
}

input {
  height: 92rpx;
  border-bottom: 1rpx solid $border-color;
  font-size: 28rpx;
  color: $text-main;
}

textarea {
  width: 100%;
  height: 220rpx;
  padding-top: 24rpx;
  border-bottom: 1rpx solid $border-color;
  font-size: 28rpx;
  line-height: 1.7;
}

:deep(.placeholder) {
  color: $text-light;
}

.submit {
  margin-top: 40rpx;
}
</style>
