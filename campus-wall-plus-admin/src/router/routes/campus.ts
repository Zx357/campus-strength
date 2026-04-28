import type { RouteRecordRaw } from 'vue-router';

export const campusRoutes: RouteRecordRaw[] = [
  { path: '/campus/dashboard', component: () => import('@/views/campus/dashboard/index.vue'), meta: { title: '工作台' } },
  { path: '/campus/tenant', component: () => import('@/views/campus/tenant/index.vue'), meta: { title: '租户管理' } },
  { path: '/campus/user', component: () => import('@/views/campus/user/index.vue'), meta: { title: '用户管理' } },
  { path: '/campus/content/post', component: () => import('@/views/campus/content/post/index.vue'), meta: { title: '校园墙帖子' } },
  { path: '/campus/content/comment', component: () => import('@/views/campus/content/comment/index.vue'), meta: { title: '评论管理' } },
  { path: '/campus/content/lost-found', component: () => import('@/views/campus/content/lost-found/index.vue'), meta: { title: '失物招领' } },
  { path: '/campus/content/goods', component: () => import('@/views/campus/content/goods/index.vue'), meta: { title: '二手闲置' } },
  { path: '/campus/content/help', component: () => import('@/views/campus/content/help/index.vue'), meta: { title: '校园互助' } },
  { path: '/campus/content/activity', component: () => import('@/views/campus/content/activity/index.vue'), meta: { title: '活动社团' } },
  { path: '/campus/audit', component: () => import('@/views/campus/audit/index.vue'), meta: { title: '审核中心' } },
  { path: '/campus/report', component: () => import('@/views/campus/report/index.vue'), meta: { title: '举报管理' } },
  { path: '/campus/category', component: () => import('@/views/campus/category/index.vue'), meta: { title: '分类管理' } },
  { path: '/campus/notice', component: () => import('@/views/campus/notice/index.vue'), meta: { title: '公告管理' } },
  { path: '/campus/sensitive-word', component: () => import('@/views/campus/sensitive-word/index.vue'), meta: { title: '敏感词管理' } },
  { path: '/campus/operation-log', component: () => import('@/views/campus/operation-log/index.vue'), meta: { title: '操作日志' } }
];
