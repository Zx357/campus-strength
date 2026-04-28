export interface TenantItem {
  id: number;
  tenantCode: string;
  tenantName: string;
  logoUrl?: string | null;
  themeColor?: string;
  auditMode?: number;
  allowAnonymous?: number;
}

export interface UserSession {
  token: string;
  userId: number;
  tenantId: number | null;
  username: string;
  nickname: string;
  roleCode: string;
}

export interface UserProfile {
  id: number;
  tenantId: number | null;
  username?: string;
  nickname: string;
  avatarUrl?: string | null;
  phone?: string | null;
  college?: string | null;
  major?: string | null;
  grade?: string | null;
  roleCode: string;
  authStatus?: number;
  status?: number;
}

export interface CategoryItem {
  id?: number;
  moduleCode?: string;
  categoryName: string;
  icon?: string;
  sort?: number;
}

export interface NoticeItem {
  id: number;
  title: string;
  content: string;
  type?: number;
  topFlag?: number;
  createdAt?: string;
}

export interface PostItem {
  id: number;
  title?: string;
  content: string;
  images: string[];
  topicTags: string[];
  location?: string;
  likeCount: number;
  commentCount: number;
  collectCount: number;
  viewCount?: number;
  hotScore?: number;
  createdAt?: string;
  userName?: string;
  userAvatar?: string;
  tenantName?: string;
  liked?: boolean;
  collected?: boolean;
  anonymous?: number;
}

export interface CommentItem {
  id: number;
  postId: number;
  userId?: number;
  parentId?: number | null;
  replyUserId?: number | null;
  content: string;
  status?: number;
  likeCount?: number;
  createdAt?: string;
  userName?: string;
  userAvatar?: string;
  replyUserName?: string;
}

export interface LostFoundItem {
  id: number;
  type: 1 | 2;
  title: string;
  description?: string;
  images: string[];
  lostTime?: string;
  lostLocation?: string;
  contactName?: string;
  contactPhone?: string;
  status: number;
  viewCount?: number;
  createdAt?: string;
}

export interface GoodsItem {
  id: number;
  title: string;
  description?: string;
  images: string[];
  price: number;
  originalPrice?: number;
  conditionLevel?: string;
  tradeMethod?: string;
  location?: string;
  contactPhone?: string;
  status: number;
  createdAt?: string;
  sellerName?: string;
  sellerAvatar?: string;
}

export interface HelpItem {
  id: number;
  type?: number;
  title: string;
  content: string;
  images?: string[];
  rewardAmount?: number;
  location?: string;
  status?: number;
  createdAt?: string;
}

export interface ActivityItem {
  id: number;
  title: string;
  content: string;
  coverUrl?: string;
  activityTime?: string;
  activityLocation?: string;
  organizer?: string;
  status?: number;
  createdAt?: string;
}

export interface ReportItem {
  id: number;
  targetType?: string;
  targetId?: number;
  reasonType?: string;
  reasonContent?: string;
  status?: number;
  handleResult?: string;
  createdAt?: string;
}

export interface MessageItem {
  id: number;
  messageType?: string;
  title: string;
  content: string;
  targetType?: string;
  targetId?: number;
  readFlag: number;
  createdAt?: string;
}

export interface PageResult<T> {
  records: T[];
  total: number;
  current: number;
  size: number;
}

export type PublishMode = 'post' | 'lost' | 'goods';
