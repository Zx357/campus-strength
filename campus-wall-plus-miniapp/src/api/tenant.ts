import { request } from '@/utils/request';
import type { TenantItem } from '@/types';
import { mockTenants } from '@/mock';

export const getTenantList = () =>
  request<TenantItem[]>({
    url: '/api/app/tenant/list',
    method: 'GET',
    fallback: mockTenants
  });
