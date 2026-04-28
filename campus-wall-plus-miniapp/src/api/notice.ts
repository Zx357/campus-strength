import { mockNotices } from '@/mock';
import type { NoticeItem, PageResult } from '@/types';
import { request } from '@/utils/request';

export const getNoticePage = async (params: { current?: number; size?: number }) => {
  const result = await request<PageResult<NoticeItem>>({
    url: '/api/app/notice/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockNotices,
      total: mockNotices.length,
      current: 1,
      size: 5
    }
  });

  return result;
};
