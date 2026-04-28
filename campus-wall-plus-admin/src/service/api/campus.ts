import { request } from '../request';

export type PageQuery = { current?: number; size?: number; keyword?: string };
export type CampusRecord = Record<string, any>;

async function unwrap<T>(promise: Promise<any>): Promise<T> {
  const response = await promise;

  return (response?.data ?? response) as T;
}

const get = <T>(url: string, params?: any) => unwrap<T>(request<T>({ url, method: 'get', params }));
const post = <T>(url: string, data?: any) => unwrap<T>(request<T>({ url, method: 'post', data }));
const put = <T>(url: string, data?: any) => unwrap<T>(request<T>({ url, method: 'put', data }));
const del = <T>(url: string, params?: any) => unwrap<T>(request<T>({ url, method: 'delete', params }));

export const campusApi = {
  dashboard: {
    summary: () => get<Record<string, number>>('/api/admin/dashboard/summary'),
    trend: () => get<any[]>('/api/admin/dashboard/post-trend'),
    ratio: () => get<any[]>('/api/admin/dashboard/content-ratio')
  },
  page: (base: string, params: PageQuery) => get<any>(base + '/page', params),
  create: (base: string, data: CampusRecord) => post<number>(base + '/create', data),
  update: (base: string, data: CampusRecord) => put<void>(base + '/update', data),
  remove: (base: string, id: number) => del<void>(base + '/delete', { id }),
  status: (base: string, id: number, status: number) => put<void>(base + '/status', { id, status }),
  auditPass: (data: CampusRecord) => post<void>('/api/admin/audit/pass', data),
  auditReject: (data: CampusRecord) => post<void>('/api/admin/audit/reject', data),
  handleReport: (data: CampusRecord) => put<void>('/api/admin/report/handle', data),
  userBan: (id: number, status: number) => put<void>('/api/admin/user/ban', { id, status }),
  userAuth: (id: number, authStatus: number, reason?: string) => put<void>('/api/admin/user/auth', { id, authStatus, reason }),
  userRole: (id: number, roleCode: string) => put<void>('/api/admin/user/role', { id, roleCode }),
  activitySignupPage: (params: PageQuery & { activityId?: number }) => get<any>('/api/admin/activity/signup-page', params)
};
