import type { FlatResponseData } from '@sa/axios';
import { request } from '../request';

type BackendPage<T> = Api.Common.PaginatingQueryRecord<T>;
type ApiResult<T> = FlatResponseData<any, T>;

function enabledToUi(status?: number | string): Api.Common.EnableStatus {
  return Number(status) === 1 ? '1' : '2';
}

function enabledToBackend(status?: number | string) {
  return String(status) === '1' ? 1 : 0;
}

function routeNameFromPath(routePath?: string) {
  return (routePath || '').replace(/^\//, '').replace(/\//g, '_');
}

function componentToUi(component?: string | null) {
  if (!component) return '';
  if (component.startsWith('layout.') || component.startsWith('view.')) return component;

  return `view.${component.replace(/\/index$/, '').replace(/\//g, '_')}`;
}

function componentToBackend(component?: string | null) {
  if (!component) return '';

  return component
    .replace(/^layout\.[^$]+\$?/, '')
    .replace(/^view\./, '')
    .replace(/_/g, '/')
    .replace(/\/?$/, '/index');
}

function toUiUser(item: any): Api.SystemManage.User {
  return {
    ...item,
    userName: item.username,
    nickName: item.nickname,
    userPhone: item.phone,
    userEmail: item.email || '',
    userGender: undefined,
    roleCode: item.roleCode,
    userRoles: item.roleCode ? [item.roleCode] : [],
    status: enabledToUi(item.status)
  };
}

function toBackendUser(item: Partial<Api.SystemManage.User> & Record<string, any>) {
  return {
    id: item.id,
    username: item.userName || item.username,
    password: item.password || '123456',
    nickname: item.nickName || item.nickname,
    phone: item.userPhone || item.phone,
    roleCode: item.roleCode || item.userRoles?.[0] || 'TENANT_AUDITOR',
    authStatus: item.authStatus ?? 0,
    status: enabledToBackend(item.status)
  };
}

function toUiRole(item: any): Api.SystemManage.Role {
  return {
    ...item,
    roleDesc: item.roleType || '',
    status: enabledToUi(item.status)
  };
}

function toBackendRole(item: Partial<Api.SystemManage.Role> & Record<string, any>) {
  return {
    id: item.id,
    roleName: item.roleName,
    roleCode: item.roleCode,
    roleType: item.roleDesc || item.roleType || 'tenant',
    status: enabledToBackend(item.status)
  };
}

function toUiMenu(item: any): Api.SystemManage.Menu {
  const routeName = item.routeName || routeNameFromPath(item.routePath);

  return {
    ...item,
    routeName,
    menuType: item.menuType === 'catalog' ? '1' : '2',
    component: componentToUi(item.component),
    iconType: '1',
    order: item.sort ?? item.order ?? 0,
    status: enabledToUi(item.status),
    keepAlive: false,
    constant: false,
    hideInMenu: false,
    multiTab: false,
    query: [],
    buttons: []
  };
}

function toBackendMenu(item: Partial<Api.SystemManage.Menu> & Record<string, any>) {
  return {
    id: item.id,
    parentId: item.parentId ?? 0,
    menuName: item.menuName,
    menuType: item.menuType === '1' ? 'catalog' : 'menu',
    routePath: item.routePath,
    component: componentToBackend(item.component),
    permission: item.permission || `${item.routeName || routeNameFromPath(item.routePath)}:view`,
    icon: item.icon,
    sort: item.order ?? item.sort ?? 0,
    status: enabledToBackend(item.status)
  };
}

function getFirstFilledValue(values: unknown[]) {
  return values.find(value => typeof value === 'string' && value.trim()) as string | undefined;
}

function toUserQuery(params?: Api.SystemManage.UserSearchParams) {
  return {
    current: params?.current,
    size: params?.size,
    keyword: getFirstFilledValue([params?.userName, params?.nickName, params?.userPhone, params?.userEmail])
  };
}

function toRoleQuery(params?: Api.SystemManage.RoleSearchParams) {
  return {
    current: params?.current,
    size: params?.size,
    keyword: getFirstFilledValue([params?.roleName, params?.roleCode])
  };
}

function mapPage<T, U>(page: BackendPage<T>, mapper: (item: T) => U): BackendPage<U> {
  return {
    ...page,
    records: (page?.records || []).map(mapper)
  };
}

async function mapResult<T, U>(promise: Promise<ApiResult<T>>, mapper: (data: T) => U): Promise<ApiResult<U>> {
  const response = await promise;
  if (response?.error) return response as unknown as ApiResult<U>;

  return {
    ...response,
    data: mapper(response.data as T)
  } as ApiResult<U>;
}

async function fetchAllRecords<T>(url: string): Promise<ApiResult<BackendPage<T>>> {
  const size = 100;
  const first = await request<BackendPage<T>>({
    url,
    method: 'get',
    params: { current: 1, size }
  });

  if (first.error || !first.data) return first;

  const total = first.data.total || 0;
  const pages = Math.ceil(total / size);
  const records = [...(first.data.records || [])];

  for (let current = 2; current <= pages; current += 1) {
    const response = await request<BackendPage<T>>({
      url,
      method: 'get',
      params: { current, size }
    });

    if (response.error || !response.data) return response;
    records.push(...(response.data.records || []));
  }

  return {
    ...first,
    data: {
      ...first.data,
      records,
      current: 1,
      size,
      total
    }
  };
}

const viewPages = import.meta.glob('../../views/**/*.vue');
const allPages = Object.keys(viewPages)
  .map(path =>
    path
      .replace('../../views/', '')
      .replace('/index.vue', '')
      .replace('.vue', '')
      .replace(/\//g, '_')
      .replace(/\[|\]/g, '')
  )
  .filter(Boolean)
  .sort();

/** get role list */
export function fetchGetRoleList(params?: Api.SystemManage.RoleSearchParams) {
  return mapResult(
    request<BackendPage<any>>({
      url: '/api/admin/role/admin-page',
      method: 'get',
      params: toRoleQuery(params)
    }),
    page => mapPage(page, toUiRole)
  );
}

/** get all roles */
export function fetchGetAllRoles() {
  return mapResult(
    fetchAllRecords<any>('/api/admin/role/admin-page'),
    page => page.records.map(toUiRole)
  );
}

/** get user list */
export function fetchGetUserList(params?: Api.SystemManage.UserSearchParams) {
  return mapResult(
    request<BackendPage<any>>({
      url: '/api/admin/user/admin-page',
      method: 'get',
      params: toUserQuery(params)
    }),
    page => mapPage(page, toUiUser)
  );
}

/** get menu list */
export function fetchGetMenuList() {
  return mapResult(
    fetchAllRecords<any>('/api/admin/menu/page'),
    page => mapPage(page, toUiMenu)
  );
}

/** get all pages */
export function fetchGetAllPages() {
  return Promise.resolve({ data: allPages, error: null, response: null } as unknown as ApiResult<string[]>);
}

/** get menu tree */
export function fetchGetMenuTree() {
  return mapResult(fetchGetMenuList(), page => {
    const records = page.records;
    const nodeMap = new Map<number, Api.SystemManage.MenuTree>();
    const tree: Api.SystemManage.MenuTree[] = [];

    records.forEach(item => {
      nodeMap.set(item.id, { id: item.id, label: item.menuName, pId: item.parentId, children: [] });
    });

    records.forEach(item => {
      const node = nodeMap.get(item.id)!;
      const parent = nodeMap.get(item.parentId);
      if (parent) parent.children?.push(node);
      else tree.push(node);
    });

    return tree;
  });
}

export function fetchCreateUser(data: Partial<Api.SystemManage.User>) {
  return request<number>({ url: '/api/admin/user/create', method: 'post', data: toBackendUser(data) });
}

export function fetchUpdateUser(data: Partial<Api.SystemManage.User>) {
  return request<void>({ url: '/api/admin/user/update', method: 'put', data: toBackendUser(data) });
}

export function fetchDeleteUser(id: number) {
  return request<void>({ url: '/api/admin/user/delete', method: 'delete', params: { id } });
}

export function fetchCreateRole(data: Partial<Api.SystemManage.Role>) {
  return request<number>({ url: '/api/admin/role/create', method: 'post', data: toBackendRole(data) });
}

export function fetchUpdateRole(data: Partial<Api.SystemManage.Role>) {
  return request<void>({ url: '/api/admin/role/update', method: 'put', data: toBackendRole(data) });
}

export function fetchDeleteRole(id: number) {
  return request<void>({ url: '/api/admin/role/delete', method: 'delete', params: { id } });
}

export function fetchCreateMenu(data: Partial<Api.SystemManage.Menu>) {
  return request<number>({ url: '/api/admin/menu/create', method: 'post', data: toBackendMenu(data) });
}

export function fetchUpdateMenu(data: Partial<Api.SystemManage.Menu>) {
  return request<void>({ url: '/api/admin/menu/update', method: 'put', data: toBackendMenu(data) });
}

export function fetchDeleteMenu(id: number) {
  return request<void>({ url: '/api/admin/menu/delete', method: 'delete', params: { id } });
}

export function fetchGetRoleMenuIds(roleId: number) {
  return request<number[]>({ url: `/api/admin/role-menu/${roleId}/menus`, method: 'get' });
}

export function fetchUpdateRoleMenuIds(roleId: number, menuIds: number[]) {
  return request<void>({ url: `/api/admin/role-menu/${roleId}/menus`, method: 'put', data: { menuIds } });
}
