<script setup lang="ts">
import { computed, ref } from 'vue';
import { guestLogin, wxLogin } from '@/api/auth';
import { mockTenants } from '@/mock';
import { useUserStore } from '@/store/user';
import { getTenantList } from '@/api/tenant';
import type { TenantItem } from '@/types';

const userStore = useUserStore();
const agree = ref(false);
const loading = ref(false);
const tenants = ref<TenantItem[]>(mockTenants);
const selectedIndex = ref(0);

const selectedTenant = computed(() => tenants.value[selectedIndex.value] || mockTenants[0]);
const tenantNames = computed(() => tenants.value.map(item => item.tenantName));

async function loadTenants() {
  const result = await getTenantList();
  if (result.length) {
    tenants.value = result;
  }
}

loadTenants();

function onTenantChange(event: any) {
  selectedIndex.value = Number(event.detail.value || 0);
}

function verifyBeforeLogin() {
  if (!selectedTenant.value) {
    uni.showToast({ title: '请选择学校', icon: 'none' });
    return false;
  }

  if (!agree.value) {
    uni.showToast({ title: '请先同意用户协议和隐私政策', icon: 'none' });
    return false;
  }

  userStore.setTenant(selectedTenant.value);
  return true;
}

function getWxCode() {
  return new Promise<string>(resolve => {
    // #ifdef MP-WEIXIN
    uni.login({
      provider: 'weixin',
      success: res => resolve(res.code || `mock-${Date.now()}`),
      fail: () => resolve(`mock-${Date.now()}`)
    });
    // #endif

    // #ifndef MP-WEIXIN
    resolve(`mock-${Date.now()}`);
    // #endif
  });
}

async function handleWxLogin() {
  if (!verifyBeforeLogin() || loading.value) return;

  loading.value = true;
  try {
    const code = await getWxCode();
    const session = await wxLogin({
      tenantCode: selectedTenant.value.tenantCode,
      code,
      nickname: '校园同学'
    });

    userStore.setSession(session);
    userStore.setTenant(selectedTenant.value);
    uni.showToast({ title: '登录成功', icon: 'success' });
    setTimeout(() => {
      uni.reLaunch({ url: '/pages/home/index' });
    }, 300);
  } finally {
    loading.value = false;
  }
}

async function handleGuest() {
  if (!verifyBeforeLogin() || loading.value) return;

  loading.value = true;
  try {
    const session = await guestLogin(selectedTenant.value.tenantCode);
    userStore.setSession(session);
    userStore.setTenant(selectedTenant.value);
    uni.showToast({ title: '已进入游客模式', icon: 'none' });
    setTimeout(() => {
      uni.reLaunch({ url: '/pages/home/index' });
    }, 300);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <view class="login-page">
    <view class="hero">
      <image class="logo-image" src="/static/logo.png" mode="aspectFit" />
      <view class="brand-title">校园墙+</view>
      <view class="brand-sub">多租户校园生活社区</view>
    </view>

    <view class="login-card card">
      <view class="field-label">选择学校</view>
      <picker :range="tenantNames" :value="selectedIndex" @change="onTenantChange">
        <view class="picker-field">
          <text>{{ selectedTenant.tenantName || '请选择学校' }}</text>
          <view class="picker-arrow" />
        </view>
      </picker>

      <view class="primary-btn action-btn" @tap="handleWxLogin">{{ loading ? '登录中...' : '微信快捷登录' }}</view>
      <view class="ghost-btn action-btn" @tap="handleGuest">游客预览</view>

      <label class="agreement" @tap="agree = !agree">
        <checkbox :checked="agree" color="#18B66A" />
        <text>我已阅读并同意《用户协议》和《隐私政策》</text>
      </label>

      <view class="other-login">其他登录方式</view>
      <view class="other-icon">微</view>
    </view>

    <view class="footer-slogan">连接校园 分享美好 互助成长</view>
  </view>
</template>

<style scoped lang="scss">
@import '@/styles/variables.scss';

.login-page {
  min-height: 100vh;
  padding: 92rpx 48rpx 48rpx;
  background:
    radial-gradient(circle at 15% 15%, rgba(77, 163, 255, 0.18), transparent 30%),
    radial-gradient(circle at 85% 18%, rgba(24, 182, 106, 0.14), transparent 34%),
    linear-gradient(160deg, #ecf7ff 0%, #f4fff7 56%, #e7f6ff 100%);
}

.hero {
  text-align: center;
  margin-top: 24rpx;
}

.logo-image {
  width: 132rpx;
  height: 132rpx;
  margin: 0 auto 24rpx;
}

.brand-title {
  font-size: 56rpx;
  font-weight: 900;
  color: $primary-dark;
}

.brand-sub {
  margin-top: 10rpx;
  color: $text-sub;
  font-size: 26rpx;
}

.login-card {
  margin-top: 62rpx;
  padding: 34rpx;
}

.field-label {
  margin-bottom: 14rpx;
  color: $text-main;
  font-size: 26rpx;
  font-weight: 700;
}

.picker-field {
  height: 82rpx;
  padding: 0 22rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-radius: 20rpx;
  background: #fff;
  box-shadow: inset 0 0 0 2rpx $border-color;
  font-size: 28rpx;
  color: $text-main;
}

.picker-arrow {
  width: 12rpx;
  height: 12rpx;
  border-top: 3rpx solid $text-light;
  border-right: 3rpx solid $text-light;
  transform: rotate(45deg);
}

.action-btn {
  margin-top: 20rpx;
}

.agreement {
  margin-top: 24rpx;
  display: flex;
  align-items: center;
  gap: 8rpx;
  font-size: 22rpx;
  color: $text-light;
}

.other-login {
  margin: 34rpx 0 14rpx;
  text-align: center;
  color: $text-light;
  font-size: 22rpx;
}

.other-icon {
  width: 58rpx;
  height: 58rpx;
  margin: 0 auto;
  border-radius: 50%;
  background: #f3f6f9;
  color: $text-sub;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28rpx;
}

.footer-slogan {
  margin-top: 72rpx;
  text-align: center;
  color: $primary-color;
  font-size: 28rpx;
  font-weight: 800;
}
</style>
