import type { ElegantConstRoute, ElegantRoute } from '@elegant-router/types';
import { generatedRoutes } from '../elegant/routes';
import { layouts, views } from '../elegant/imports';
import { transformElegantRoutesToVueRoutes } from '../elegant/transform';

/**
 * 校园墙+ 后台菜单
 *
 * 这里只保留业务后台需要的菜单；登录、404、iframe 等基础路由继续从 generatedRoutes
 * 中按 constant 路由引入，Soybean 示例菜单不再进入左侧栏。
 */
const customRoutes: any[] = [
  {
    name: 'dashboard',
    path: '/campus/dashboard',
    component: 'layout.base$view.campus_dashboard',
    meta: {
      title: '工作台',
      localIcon: 'campus-dashboard',
      order: 1
    }
  },
  {
    name: 'tenant',
    path: '/campus/tenant',
    component: 'layout.base$view.campus_tenant',
    meta: {
      title: '租户管理',
      localIcon: 'campus-tenant',
      order: 2
    }
  },
  {
    name: 'user',
    path: '/campus/user',
    component: 'layout.base$view.campus_user',
    meta: {
      title: '用户管理',
      localIcon: 'campus-user',
      order: 3
    }
  },
  {
    name: 'content',
    path: '/campus/content',
    component: 'layout.base',
    meta: {
      title: '内容管理',
      localIcon: 'campus-content',
      order: 4
    },
    children: [
      {
        name: 'campus_content_post',
        path: '/campus/content/post',
        component: 'view.campus_content_post',
        meta: { title: '校园墙帖子', localIcon: 'campus-post', order: 1 }
      },
      {
        name: 'campus_content_comment',
        path: '/campus/content/comment',
        component: 'view.campus_content_comment',
        meta: { title: '评论管理', localIcon: 'campus-comment', order: 2 }
      },
      {
        name: 'campus_content_lost-found',
        path: '/campus/content/lost-found',
        component: 'view.campus_content_lost-found',
        meta: { title: '失物招领', localIcon: 'campus-lost', order: 3 }
      },
      {
        name: 'campus_content_goods',
        path: '/campus/content/goods',
        component: 'view.campus_content_goods',
        meta: { title: '二手闲置', localIcon: 'campus-goods', order: 4 }
      },
      {
        name: 'campus_content_help',
        path: '/campus/content/help',
        component: 'view.campus_content_help',
        meta: { title: '校园互助', localIcon: 'campus-help', order: 5 }
      },
      {
        name: 'campus_content_activity',
        path: '/campus/content/activity',
        component: 'view.campus_content_activity',
        meta: { title: '活动社团', localIcon: 'campus-activity', order: 6 }
      }
    ]
  },
  {
    name: 'audit',
    path: '/campus/audit',
    component: 'layout.base$view.campus_audit',
    meta: {
      title: '审核中心',
      localIcon: 'campus-audit',
      order: 5
    }
  },
  {
    name: 'report',
    path: '/campus/report',
    component: 'layout.base$view.campus_report',
    meta: {
      title: '举报管理',
      localIcon: 'campus-report',
      order: 6
    }
  },
  {
    name: 'category',
    path: '/campus/category',
    component: 'layout.base$view.campus_category',
    meta: {
      title: '分类管理',
      localIcon: 'campus-category',
      order: 7
    }
  },
  {
    name: 'notice',
    path: '/campus/notice',
    component: 'layout.base$view.campus_notice',
    meta: {
      title: '公告管理',
      localIcon: 'campus-notice',
      order: 8
    }
  },
  {
    name: 'sensitive',
    path: '/campus/sensitive-word',
    component: 'layout.base$view.campus_sensitive-word',
    meta: {
      title: '敏感词管理',
      localIcon: 'campus-sensitive',
      order: 9
    }
  },
  {
    name: 'operation',
    path: '/campus/operation-log',
    component: 'layout.base$view.campus_operation-log',
    meta: {
      title: '操作日志',
      localIcon: 'campus-log',
      order: 10
    }
  },
  {
    name: 'systemManage',
    path: '/manage',
    component: 'layout.base',
    meta: {
      title: '系统管理',
      localIcon: 'campus-system',
      order: 11
    },
    children: [
      {
        name: 'manage_user',
        path: '/manage/user',
        component: 'view.manage_user',
        meta: { title: '管理员管理', localIcon: 'campus-user', order: 1 }
      },
      {
        name: 'manage_role',
        path: '/manage/role',
        component: 'view.manage_role',
        meta: { title: '角色管理', localIcon: 'campus-role', order: 2 }
      },
      {
        name: 'manage_menu',
        path: '/manage/menu',
        component: 'view.manage_menu',
        meta: { title: '菜单管理', localIcon: 'campus-menu', order: 3 }
      }
    ]
  },
  {
    name: 'account',
    path: '/user-center',
    component: 'layout.base$view.user-center',
    meta: {
      title: '个人中心',
      hideInMenu: true
    }
  }
];

/** create routes when the auth route mode is static */
export function createStaticRoutes() {
  const constantRoutes: ElegantRoute[] = [];
  const authRoutes: ElegantRoute[] = [];

  [...generatedRoutes.filter(item => item.meta?.constant), ...customRoutes].forEach(item => {
    if (item.meta?.constant) {
      constantRoutes.push(item);
    } else {
      authRoutes.push(item);
    }
  });

  return {
    constantRoutes,
    authRoutes
  };
}

/**
 * Get auth vue routes
 *
 * @param routes Elegant routes
 */
export function getAuthVueRoutes(routes: ElegantConstRoute[]) {
  return transformElegantRoutesToVueRoutes(routes, layouts, views);
}
