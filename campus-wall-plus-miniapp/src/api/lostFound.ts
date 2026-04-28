import { mockLostFound } from '@/mock';
import type { LostFoundItem, PageResult } from '@/types';
import { request, toAssetUrl } from '@/utils/request';

function normalizeImages(raw?: string | string[] | null) {
  if (Array.isArray(raw)) return raw.map(item => toAssetUrl(item));
  if (!raw) return [];

  try {
    return JSON.parse(raw).map((item: string) => toAssetUrl(item));
  } catch {
    return [];
  }
}

function normalizeLostFound(item: any): LostFoundItem {
  return {
    id: item.id,
    type: Number(item.type || 1) as 1 | 2,
    title: item.title,
    description: item.description,
    images: normalizeImages(item.images),
    lostTime: item.lostTime || item.createdAt || '刚刚',
    lostLocation: item.lostLocation,
    contactName: item.contactName,
    contactPhone: item.contactPhone,
    status: Number(item.status || 1),
    viewCount: Number(item.viewCount || 0),
    createdAt: item.createdAt || '刚刚'
  };
}

function normalizePage(result: PageResult<any>) {
  return {
    ...result,
    records: result.records.map(normalizeLostFound)
  } as PageResult<LostFoundItem>;
}

export const getLostFoundPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/lost-found/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockLostFound,
      total: mockLostFound.length,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getMyLostFoundPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/lost-found/my-page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockLostFound.slice(0, 2),
      total: 2,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getLostFoundDetail = async (id: number) => {
  const result = await request<any>({
    url: '/api/app/lost-found/detail',
    method: 'GET',
    data: { id },
    fallback: mockLostFound.find(item => item.id === id) || mockLostFound[0]
  });

  return normalizeLostFound(result);
};

export const publishLostFound = (data: Record<string, any>) =>
  request<number>({
    url: '/api/app/lost-found/publish',
    method: 'POST',
    data
  });

export const updateLostFound = (data: Record<string, any>) =>
  request<void>({
    url: '/api/app/lost-found/update',
    method: 'PUT',
    data
  });

export const markLostFoundFound = (id: number) =>
  request<void>({
    url: '/api/app/lost-found/found',
    method: 'PUT',
    data: { id }
  });

export const deleteLostFound = (id: number) =>
  request<void>({
    url: `/api/app/lost-found/delete?id=${id}`,
    method: 'DELETE'
  });
