import { mockComments } from '@/mock';
import type { CommentItem, PageResult } from '@/types';
import { request, toAssetUrl } from '@/utils/request';

function normalizeComment(item: any): CommentItem {
  return {
    id: Number(item.id),
    postId: Number(item.postId),
    userId: item.userId ? Number(item.userId) : undefined,
    parentId: item.parentId ? Number(item.parentId) : null,
    replyUserId: item.replyUserId ? Number(item.replyUserId) : null,
    content: item.content || '',
    status: item.status ? Number(item.status) : 1,
    likeCount: Number(item.likeCount || 0),
    createdAt: item.createdAt || '刚刚',
    userName: item.userName || '校园同学',
    userAvatar: toAssetUrl(item.userAvatar || '/static/default-avatar.png'),
    replyUserName: item.replyUserName
  };
}

function normalizePage(result: PageResult<any>) {
  return {
    ...result,
    records: result.records.map(normalizeComment)
  } as PageResult<CommentItem>;
}

export const getCommentPage = async (params: { postId: number; current?: number; size?: number }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/comment/page',
    method: 'GET',
    data: params,
    fallback: () => {
      const records = mockComments.filter(item => item.postId === params.postId);
      return {
        records,
        total: records.length,
        current: params.current || 1,
        size: params.size || 20
      };
    }
  });

  return normalizePage(result);
};

export const getMyCommentPage = async (params: { current?: number; size?: number; keyword?: string }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/comment/my-page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockComments,
      total: mockComments.length,
      current: 1,
      size: 10
    }
  });

  return normalizePage(result);
};

export const createComment = (data: { postId: number; parentId?: number; replyUserId?: number; content: string }) =>
  request<number>({
    url: '/api/app/comment/create',
    method: 'POST',
    data
  });

export const deleteComment = (id: number) =>
  request<void>({
    url: `/api/app/comment/delete?id=${id}`,
    method: 'DELETE'
  });
