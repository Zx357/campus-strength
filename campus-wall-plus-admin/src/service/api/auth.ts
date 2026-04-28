import { request } from '../request';

/**
 * Login
 *
 * @param userName User name
 * @param password Password
 */
export function fetchLogin(userName: string, password: string) {
  return request<any>({
    url: '/api/admin/auth/login',
    method: 'post',
    data: {
      username: userName,
      password
    }
  }).then(res => {
    if (res.data) {
      res.data = {
        token: res.data.token,
        refreshToken: res.data.token
      };
    }
    return res as any;
  });
}

/** Get user info */
export function fetchGetUserInfo() {
  return request<any>({ url: '/api/admin/auth/profile' }).then(res => {
    const profile = res.data || {};
    const role = profile.roleCode || 'TENANT_ADMIN';
    if (res.data) {
      res.data = {
        userId: String(profile.id || profile.userId || ''),
        userName: profile.nickname || profile.username || '管理员',
        roles: role === 'PLATFORM_ADMIN' ? ['R_SUPER', role] : [role],
        buttons: role === 'PLATFORM_ADMIN' ? ['*'] : ['tenant:*']
      };
    }
    return res as any;
  });
}

/**
 * Refresh token
 *
 * @param refreshToken Refresh token
 */
export function fetchRefreshToken(refreshToken: string) {
  return Promise.resolve({
    data: {
      token: refreshToken,
      refreshToken
    },
    error: null
  } as any);
}

/**
 * return custom backend error
 *
 * @param code error code
 * @param msg error message
 */
export function fetchCustomBackendError(code: string, msg: string) {
  return request({ url: '/auth/error', params: { code, msg } });
}
