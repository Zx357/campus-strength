declare namespace Api.Campus {
  interface PageResult<T = any> { records: T[]; total: number; current: number; size: number }
  interface Tenant { id?: number; tenantCode?: string; tenantName?: string; themeColor?: string; status?: number }
  type Recordable = Record<string, any>
}
