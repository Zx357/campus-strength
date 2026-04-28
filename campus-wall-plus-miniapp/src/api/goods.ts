import { mockGoods } from '@/mock';
import type { GoodsItem, PageResult } from '@/types';
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

function normalizeGoods(item: any): GoodsItem {
  return {
    id: item.id,
    title: item.title,
    description: item.description,
    images: normalizeImages(item.images),
    price: Number(item.price || 0),
    originalPrice: Number(item.originalPrice || 0),
    conditionLevel: item.conditionLevel || '成色佳',
    tradeMethod: item.tradeMethod || '自提',
    location: item.location,
    contactPhone: item.contactPhone,
    status: Number(item.status || 1),
    createdAt: item.createdAt || '刚刚',
    sellerName: item.sellerName || '校园同学',
    sellerAvatar: toAssetUrl(item.sellerAvatar || '/static/default-avatar.png')
  };
}

function normalizePage(result: PageResult<any>) {
  return {
    ...result,
    records: result.records.map(normalizeGoods)
  } as PageResult<GoodsItem>;
}

export const getGoodsPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/goods/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockGoods,
      total: mockGoods.length,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getMyGoodsPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/goods/my-page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockGoods.slice(0, 2),
      total: 2,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getGoodsDetail = async (id: number) => {
  const result = await request<any>({
    url: '/api/app/goods/detail',
    method: 'GET',
    data: { id },
    fallback: mockGoods.find(item => item.id === id) || mockGoods[0]
  });

  return normalizeGoods(result);
};

export const publishGoods = (data: Record<string, any>) =>
  request<number>({
    url: '/api/app/goods/publish',
    method: 'POST',
    data
  });

export const updateGoods = (data: Record<string, any>) =>
  request<void>({
    url: '/api/app/goods/update',
    method: 'PUT',
    data
  });

export const markGoodsSold = (id: number) =>
  request<void>({
    url: '/api/app/goods/sold',
    method: 'PUT',
    data: { id }
  });

export const deleteGoods = (id: number) =>
  request<void>({
    url: `/api/app/goods/delete?id=${id}`,
    method: 'DELETE'
  });
