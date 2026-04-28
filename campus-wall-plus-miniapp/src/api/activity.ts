import { mockActivities } from '@/mock';
import type { ActivityItem, PageResult } from '@/types';
import { request, toAssetUrl } from '@/utils/request';

function normalizeActivity(item: any): ActivityItem {
  return {
    id: Number(item.id),
    title: item.title,
    content: item.content || '',
    coverUrl: toAssetUrl(item.coverUrl),
    activityTime: item.activityTime || item.createdAt || '待定',
    activityLocation: item.activityLocation,
    organizer: item.organizer,
    status: Number(item.status || 1),
    createdAt: item.createdAt || '刚刚'
  };
}

function normalizePage(result: PageResult<any>) {
  return {
    ...result,
    records: result.records.map(normalizeActivity)
  } as PageResult<ActivityItem>;
}

export const getActivityPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/activity/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockActivities,
      total: mockActivities.length,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getMySignupActivityPage = async (params: { current?: number; size?: number }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/activity/my-signup-page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockActivities.slice(0, 1),
      total: 1,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getActivityDetail = async (id: number) => {
  const result = await request<any>({
    url: '/api/app/activity/detail',
    method: 'GET',
    data: { id },
    fallback: mockActivities.find(item => item.id === id) || mockActivities[0]
  });

  return normalizeActivity(result);
};

export const signupActivity = (id: number) =>
  request<void>({
    url: '/api/app/activity/signup',
    method: 'POST',
    data: { id }
  });

export const cancelActivitySignup = (id: number) =>
  request<void>({
    url: '/api/app/activity/cancel-signup',
    method: 'PUT',
    data: { id }
  });
