import { mockPosts } from '@/mock';
import type { PageResult, PostItem } from '@/types';
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

function normalizePost(item: any): PostItem {
  let topicTags: string[] = [];
  try {
    topicTags = item.topicTags ? JSON.parse(item.topicTags) : [];
  } catch {
    topicTags = [];
  }

  return {
    id: item.id,
    title: item.title,
    content: item.content || '',
    images: normalizeImages(item.images),
    topicTags,
    location: item.location,
    likeCount: Number(item.likeCount || 0),
    commentCount: Number(item.commentCount || 0),
    collectCount: Number(item.collectCount || 0),
    viewCount: Number(item.viewCount || 0),
    hotScore: Number(item.hotScore || 0),
    createdAt: item.createdAt || '刚刚',
    userName: item.userName || '校园同学',
    userAvatar: toAssetUrl(item.userAvatar || '/static/default-avatar.png'),
    tenantName: item.tenantName || uni.getStorageSync('tenantName') || '星河大学',
    anonymous: Number(item.anonymous || 0)
  };
}

function normalizePage(result: PageResult<any>) {
  return {
    ...result,
    records: result.records.map(normalizePost)
  } as PageResult<PostItem>;
}

export const getPostPage = async (params: { current?: number; size?: number; keyword?: string; sort?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/post/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockPosts,
      total: mockPosts.length,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getMyPostPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/post/my-page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockPosts,
      total: mockPosts.length,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getMyCollectPostPage = async (params: { current?: number; size?: number }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/post/my-collect-page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockPosts.slice(0, 1),
      total: 1,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const getPostDetail = async (id: number) => {
  const result = await request<any>({
    url: '/api/app/post/detail',
    method: 'GET',
    data: { id },
    fallback: mockPosts.find(item => item.id === id) || mockPosts[0]
  });

  return normalizePost(result);
};

export const publishPost = (data: {
  categoryId?: number;
  title?: string;
  content: string;
  images?: string;
  topicTags?: string;
  location?: string;
  anonymous?: number;
}) =>
  request<number>({
    url: '/api/app/post/publish',
    method: 'POST',
    data
  });

export const updatePost = (
  id: number,
  data: {
    categoryId?: number;
    title?: string;
    content: string;
    images?: string;
    topicTags?: string;
    location?: string;
    anonymous?: number;
  }
) =>
  request<void>({
    url: '/api/app/post/update',
    method: 'PUT',
    data: { ...data, id }
  });

export const deletePost = (id: number) =>
  request<void>({
    url: `/api/app/post/delete?id=${id}`,
    method: 'DELETE'
  });

export const toggleLike = (id: number) =>
  request<boolean>({
    url: '/api/app/post/like',
    method: 'POST',
    data: { id }
  });

export const toggleCollect = (id: number) =>
  request<boolean>({
    url: '/api/app/post/collect',
    method: 'POST',
    data: { id }
  });
