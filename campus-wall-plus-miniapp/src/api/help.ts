import { mockHelps } from '@/mock';
import type { HelpItem, PageResult } from '@/types';
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

function normalizeHelp(item: any): HelpItem {
  return {
    id: Number(item.id),
    type: item.type ? Number(item.type) : 1,
    title: item.title,
    content: item.content || '',
    images: normalizeImages(item.images),
    rewardAmount: Number(item.rewardAmount || 0),
    location: item.location,
    status: Number(item.status || 1),
    createdAt: item.createdAt || '刚刚'
  };
}

export const getHelpPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/help/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockHelps,
      total: mockHelps.length,
      current: 1,
      size: 10
    }
  });

  return {
    ...result,
    records: result.records.map(normalizeHelp)
  } as PageResult<HelpItem>;
};

export const getHelpDetail = async (id: number) => {
  const result = await request<any>({
    url: '/api/app/help/detail',
    method: 'GET',
    data: { id },
    fallback: mockHelps.find(item => item.id === id) || mockHelps[0]
  });

  return normalizeHelp(result);
};

export const getMyHelpPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/help/my-page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockHelps.slice(0, 2),
      total: 2,
      current: 1,
      size: 10
    }
  });

  return {
    ...result,
    records: result.records.map(normalizeHelp)
  } as PageResult<HelpItem>;
};

export const publishHelp = (data: Record<string, any>) =>
  request<number>({
    url: '/api/app/help/publish',
    method: 'POST',
    data
  });

export const updateHelp = (data: Record<string, any>) =>
  request<void>({
    url: '/api/app/help/update',
    method: 'PUT',
    data
  });

export const finishHelp = (id: number) =>
  request<void>({
    url: '/api/app/help/finish',
    method: 'PUT',
    data: { id }
  });

export const deleteHelp = (id: number) =>
  request<void>({
    url: `/api/app/help/delete?id=${id}`,
    method: 'DELETE'
  });
