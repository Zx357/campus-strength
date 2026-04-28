const LOCAL_BASE_URL = 'http://localhost:8080';

export interface RequestOptions<T> extends Omit<UniApp.RequestOptions, 'url' | 'success' | 'fail'> {
  url: string;
  fallback?: T | (() => T | Promise<T>);
  silent?: boolean;
}

async function resolveFallback<T>(fallback?: T | (() => T | Promise<T>)) {
  if (typeof fallback === 'function') {
    return (fallback as () => T | Promise<T>)();
  }

  return fallback as T;
}

export function getBaseUrl() {
  return uni.getStorageSync('apiBaseUrl') || LOCAL_BASE_URL;
}

export function toAssetUrl(url?: string | null) {
  if (!url) return '';
  if (/^(https?:)?\/\//.test(url)) return url;
  if (url.startsWith('/')) return `${getBaseUrl()}${url}`;
  return url;
}

export function request<T>(options: RequestOptions<T>) {
  const token = uni.getStorageSync('token');
  const tenantCode = uni.getStorageSync('tenantCode') || 'xhxy';
  const baseUrl = getBaseUrl();

  return new Promise<T>((resolve, reject) => {
    uni.request({
      ...options,
      url: `${baseUrl}${options.url}`,
      header: {
        'X-Tenant-Code': tenantCode,
        Authorization: token ? `Bearer ${token}` : '',
        ...(options.header || {})
      },
      success: async res => {
        const payload: any = res.data;
        if (payload?.code === 200) {
          resolve(payload.data as T);
          return;
        }

        try {
          const fallback = await resolveFallback(options.fallback);
          if (fallback !== undefined) {
            resolve(fallback);
            return;
          }
        } catch (error) {
          reject(error);
          return;
        }

        if (!options.silent) {
          uni.showToast({ title: payload?.message || '请求失败', icon: 'none' });
        }
        reject(new Error(payload?.message || '请求失败'));
      },
      fail: async error => {
        try {
          const fallback = await resolveFallback(options.fallback);
          if (fallback !== undefined) {
            resolve(fallback);
            return;
          }
        } catch (fallbackError) {
          reject(fallbackError);
          return;
        }

        if (!options.silent) {
          uni.showToast({ title: '网络开小差了', icon: 'none' });
        }
        reject(error);
      }
    });
  });
}

export function uploadFile(filePath: string) {
  const token = uni.getStorageSync('token');
  const tenantCode = uni.getStorageSync('tenantCode') || 'xhxy';
  const baseUrl = getBaseUrl();

  return new Promise<{ fileUrl: string; url: string }>((resolve, reject) => {
    uni.uploadFile({
      url: `${baseUrl}/api/common/file/upload`,
      filePath,
      name: 'file',
      header: {
        'X-Tenant-Code': tenantCode,
        Authorization: token ? `Bearer ${token}` : ''
      },
      success: res => {
        const payload = JSON.parse(res.data || '{}');
        if (payload?.code === 200) {
          resolve(payload.data);
          return;
        }

        reject(new Error(payload?.message || '上传失败'));
      },
      fail: reject
    });
  });
}
