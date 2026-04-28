import { mockReports } from '@/mock';
import type { PageResult, ReportItem } from '@/types';
import { request } from '@/utils/request';

function normalizeReport(item: any): ReportItem {
  return {
    id: Number(item.id),
    targetType: item.targetType,
    targetId: item.targetId ? Number(item.targetId) : undefined,
    reasonType: item.reasonType,
    reasonContent: item.reasonContent,
    status: Number(item.status || 0),
    handleResult: item.handleResult,
    createdAt: item.createdAt || '刚刚'
  };
}

export const getMyReportPage = async (params: { current?: number; size?: number }) => {
  const result = await request<PageResult<any>>({
    url: '/api/app/report/page',
    method: 'GET',
    data: params,
    fallback: {
      records: mockReports,
      total: mockReports.length,
      current: 1,
      size: 10
    }
  });

  return {
    ...result,
    records: result.records.map(normalizeReport)
  } as PageResult<ReportItem>;
};

export const createReport = (data: {
  targetType: string;
  targetId: number;
  reasonType: string;
  reasonContent?: string;
  images?: string;
}) =>
  request<number>({
    url: '/api/app/report/create',
    method: 'POST',
    data
  });
