import { mockMessages } from '@/mock';
import type { MessageItem, PageResult } from '@/types';
import { request } from '@/utils/request';

function normalizeMessage(item: any): MessageItem {
  return {
    id: item.id,
    messageType: item.messageType,
    title: item.title,
    content: item.content,
    targetType: item.targetType,
    targetId: item.targetId,
    readFlag: Number(item.readFlag || 0),
    createdAt: item.createdAt || '刚刚'
  };
}

export const getMessagePage = async (params: { current?: number; size?: number }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/message/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockMessages,
      total: mockMessages.length,
      current: 1,
      size: 10
    }
  });

  return {
    ...result,
    records: result.records.map(normalizeMessage)
  } as PageResult<MessageItem>;
};

export const markMessageRead = (id: number) =>
  request<void>({
    url: '/api/app/message/read',
    method: 'PUT',
    data: { id },
    silent: true
  });
