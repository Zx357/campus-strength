import { request } from '@/utils/request';
import type { UserProfile, UserSession } from '@/types';
import { mockProfile } from '@/mock';

export const wxLogin = (data: { tenantCode: string; code?: string; nickname?: string; avatarUrl?: string }) =>
  request<UserSession>({
    url: '/api/app/auth/wx-login',
    method: 'POST',
    data
  });

export const guestLogin = (tenantCode: string) =>
  request<UserSession>({
    url: `/api/app/auth/guest?tenantCode=${tenantCode}`,
    method: 'POST'
  });

export const getProfile = () =>
  request<UserProfile>({
    url: '/api/app/user/profile',
    method: 'GET',
    fallback: mockProfile
  });
