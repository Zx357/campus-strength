import { defineStore } from 'pinia';
import type { TenantItem, UserProfile, UserSession } from '@/types';
import { mockProfile, mockTenants } from '@/mock';

interface UserState {
  token: string;
  roleCode: string;
  tenantCode: string;
  tenantName: string;
  session: UserSession | null;
  profile: UserProfile | null;
  unreadCount: number;
}

function getStoredTenantName() {
  const stored = uni.getStorageSync('tenantName');
  return stored || mockTenants[0].tenantName;
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: uni.getStorageSync('token') || '',
    roleCode: uni.getStorageSync('roleCode') || 'GUEST',
    tenantCode: uni.getStorageSync('tenantCode') || mockTenants[0].tenantCode,
    tenantName: getStoredTenantName(),
    session: null,
    profile: null,
    unreadCount: 0
  }),
  getters: {
    isGuest: state => state.roleCode === 'GUEST',
    isLogin: state => Boolean(state.token)
  },
  actions: {
    setTenant(tenant: Partial<TenantItem>) {
      if (tenant.tenantCode) {
        this.tenantCode = tenant.tenantCode;
        uni.setStorageSync('tenantCode', tenant.tenantCode);
      }
      if (tenant.tenantName) {
        this.tenantName = tenant.tenantName;
        uni.setStorageSync('tenantName', tenant.tenantName);
      }
    },
    setSession(session: UserSession) {
      this.token = session.token;
      this.roleCode = session.roleCode;
      this.session = session;
      uni.setStorageSync('token', session.token);
      uni.setStorageSync('roleCode', session.roleCode);
    },
    setProfile(profile: UserProfile | null) {
      this.profile = profile || mockProfile;
    },
    setUnreadCount(count: number) {
      this.unreadCount = count;
    },
    logout() {
      this.token = '';
      this.roleCode = 'GUEST';
      this.session = null;
      this.profile = null;
      this.unreadCount = 0;
      uni.removeStorageSync('token');
      uni.removeStorageSync('roleCode');
    }
  }
});
