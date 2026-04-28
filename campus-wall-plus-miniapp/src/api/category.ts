import { mockCategories } from '@/mock';
import type { CategoryItem } from '@/types';
import { request } from '@/utils/request';

export const getCategoryList = (moduleCode: string) =>
  request<CategoryItem[]>({
    url: `/api/app/category/list?moduleCode=${moduleCode}`,
    method: 'GET',
    fallback: mockCategories[moduleCode] || []
  });
