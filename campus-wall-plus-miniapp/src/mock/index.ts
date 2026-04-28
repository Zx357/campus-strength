import type {
  ActivityItem,
  CategoryItem,
  CommentItem,
  GoodsItem,
  HelpItem,
  LostFoundItem,
  MessageItem,
  NoticeItem,
  PostItem,
  ReportItem,
  TenantItem,
  UserProfile
} from '@/types';

export const mockTenants: TenantItem[] = [
  {
    id: 1001,
    tenantCode: 'xhxy',
    tenantName: '星河大学',
    themeColor: '#18B66A',
    auditMode: 1,
    allowAnonymous: 1
  }
];

export const mockNotices: NoticeItem[] = [
  {
    id: 5001,
    title: '新学期社团招新开始啦',
    content: '欢迎关注校园墙+活动社团栏目，发现更多校园里的精彩活动。',
    type: 1,
    topFlag: 1
  }
];

export const mockCategories: Record<string, CategoryItem[]> = {
  post: [
    { id: 2001, categoryName: '校园日常' },
    { id: 2002, categoryName: '树洞吐槽' },
    { id: 2003, categoryName: '学习分享' }
  ],
  goods: [
    { id: 3001, categoryName: '数码电子' },
    { id: 3002, categoryName: '图书教材' },
    { id: 3003, categoryName: '生活用品' }
  ],
  lost: [
    { id: 4001, categoryName: '证件卡包' },
    { id: 4002, categoryName: '电子设备' },
    { id: 4003, categoryName: '衣物配饰' }
  ]
};

export const mockPosts: PostItem[] = [
  {
    id: 4001,
    title: '今晚图书馆晚霞',
    content: '今天的晚霞也太美了吧，在图书馆后面拍到的，分享给大家。',
    images: [
      'linear-gradient(135deg, #F9C58D 0%, #FF7B72 100%)',
      'linear-gradient(135deg, #7CC6FE 0%, #5B8DEF 100%)',
      'linear-gradient(135deg, #9EE6B8 0%, #18B66A 100%)'
    ],
    topicTags: ['晚霞', '校园生活'],
    location: '星河大学 · 图书馆',
    likeCount: 128,
    commentCount: 32,
    collectCount: 15,
    viewCount: 520,
    hotScore: 88,
    createdAt: '10分钟前',
    userName: '小宇同学',
    userAvatar: '/static/default-avatar.png',
    tenantName: '星河大学'
  },
  {
    id: 4002,
    title: '求推荐面馆',
    content: '求推荐学校附近好吃的面馆，最好适合和朋友一起去。',
    images: [],
    topicTags: ['求助'],
    location: '大学生活动中心',
    likeCount: 56,
    commentCount: 18,
    collectCount: 9,
    viewCount: 180,
    hotScore: 46,
    createdAt: '2小时前',
    userName: '奶茶不加糖',
    userAvatar: '/static/default-avatar.png',
    tenantName: '星河大学'
  }
];

export const mockComments: CommentItem[] = [
  {
    id: 9101,
    postId: 4001,
    userId: 10001,
    content: '这组颜色太舒服了，图书馆后面这个机位我也想去拍。',
    createdAt: '刚刚',
    userName: '奶茶不加糖',
    userAvatar: '/static/default-avatar.png'
  },
  {
    id: 9102,
    postId: 4001,
    userId: 10002,
    parentId: 9101,
    content: '真的很出片，傍晚五点半左右光线最好。',
    createdAt: '3分钟前',
    userName: '小宇同学',
    userAvatar: '/static/default-avatar.png',
    replyUserName: '奶茶不加糖'
  },
  {
    id: 9103,
    postId: 4002,
    userId: 10003,
    content: '校门东边那家番茄牛腩面不错，晚上人会多一点。',
    createdAt: '12分钟前',
    userName: '学霸本霸',
    userAvatar: '/static/default-avatar.png'
  }
];

export const mockLostFound: LostFoundItem[] = [
  {
    id: 6001,
    type: 1,
    title: '黑色双肩包',
    description: '书包内有高数教材和一卡通。',
    images: ['linear-gradient(135deg, #1F2937 0%, #6B7280 100%)'],
    lostTime: '今天 10:30',
    lostLocation: '图书馆一楼自习室',
    contactName: '王同学',
    contactPhone: '13800000001',
    status: 1
  },
  {
    id: 6002,
    type: 1,
    title: '一串钥匙（带卡套）',
    description: '钥匙上有蓝色卡套，可能落在活动中心附近。',
    images: ['linear-gradient(135deg, #E7D7A2 0%, #B8864A 100%)'],
    lostTime: '昨天 18:20',
    lostLocation: '大学生活动中心附近',
    contactName: '李同学',
    contactPhone: '13800000002',
    status: 1
  },
  {
    id: 6003,
    type: 2,
    title: '白色 AirPods Pro',
    description: '已交给宿管阿姨，如有遗失请联系。',
    images: ['linear-gradient(135deg, #FFFFFF 0%, #D9E1E8 100%)'],
    lostTime: '5月16日 20:15',
    lostLocation: '操场看台',
    contactName: '张同学',
    contactPhone: '13800000003',
    status: 2
  }
];

export const mockGoods: GoodsItem[] = [
  {
    id: 7001,
    title: 'MacBook Air M1 8+256G',
    description: '自用电脑，外观完整，电池健康良好。',
    images: ['linear-gradient(135deg, #DCEBFF 0%, #A7C8FF 100%)'],
    price: 3200,
    originalPrice: 5999,
    conditionLevel: '99新',
    tradeMethod: '自提',
    location: '图书馆门口',
    contactPhone: '13800000004',
    status: 1,
    sellerName: '小宇同学',
    sellerAvatar: '/static/default-avatar.png'
  },
  {
    id: 7002,
    title: '富士拍立得 mini11',
    description: '成色好，配一盒相纸。',
    images: ['linear-gradient(135deg, #FFF7ED 0%, #FFD8A8 100%)'],
    price: 280,
    originalPrice: 499,
    conditionLevel: '95新',
    tradeMethod: '自提',
    location: '女生宿舍楼下',
    contactPhone: '13800000005',
    status: 1,
    sellerName: '奶茶不加糖',
    sellerAvatar: '/static/default-avatar.png'
  },
  {
    id: 7003,
    title: '高数教材 + 习题册',
    description: '书页整洁，适合大一同学复习。',
    images: ['linear-gradient(135deg, #D8F3DC 0%, #B7E4C7 100%)'],
    price: 40,
    originalPrice: 88,
    conditionLevel: '9成新',
    tradeMethod: '自提',
    location: '教学楼 A 区',
    contactPhone: '13800000006',
    status: 1,
    sellerName: '学霸本霸',
    sellerAvatar: '/static/default-avatar.png'
  },
  {
    id: 7004,
    title: '捷安特山地车 ATX660',
    description: '日常骑行使用，刹车灵敏。',
    images: ['linear-gradient(135deg, #E5E7EB 0%, #8A8F98 100%)'],
    price: 680,
    originalPrice: 1299,
    conditionLevel: '8成新',
    tradeMethod: '自提',
    location: '操场西门',
    contactPhone: '13800000007',
    status: 1,
    sellerName: '骑行少年',
    sellerAvatar: '/static/default-avatar.png'
  }
];

export const mockHelps: HelpItem[] = [
  {
    id: 11001,
    type: 1,
    title: '求帮取快递',
    content: '今晚在图书馆复习走不开，有同学能帮忙从菜鸟驿站带一下快递吗？',
    rewardAmount: 5,
    location: '图书馆一楼',
    status: 1,
    createdAt: '刚刚'
  },
  {
    id: 11002,
    type: 3,
    title: '宿舍拼单水果',
    content: '想拼一单水果外卖，满减后很划算，还差两个人。',
    rewardAmount: 0,
    location: '梅园 3 栋',
    status: 1,
    createdAt: '18分钟前'
  }
];

export const mockActivities: ActivityItem[] = [
  {
    id: 12001,
    title: '摄影社春日采风',
    content: '本周六一起去河边公园采风，欢迎喜欢拍照的同学报名参加。',
    coverUrl: '',
    activityTime: '周六 14:00',
    activityLocation: '校门口集合',
    organizer: '摄影社',
    status: 1,
    createdAt: '今天'
  },
  {
    id: 12002,
    title: '算法训练营分享会',
    content: 'ACM 学长分享刷题路线和比赛经验，适合想打竞赛的新同学。',
    coverUrl: '',
    activityTime: '周三 19:00',
    activityLocation: '教学楼 B201',
    organizer: '程序设计协会',
    status: 1,
    createdAt: '昨天'
  }
];

export const mockReports: ReportItem[] = [
  {
    id: 13001,
    targetType: 'post',
    targetId: 4002,
    reasonType: '广告营销',
    reasonContent: '疑似带有明显营销内容',
    status: 0,
    createdAt: '1小时前'
  },
  {
    id: 13002,
    targetType: 'comment',
    targetId: 9103,
    reasonType: '不友善言论',
    reasonContent: '评论措辞不太友好',
    status: 1,
    handleResult: '已处理并提醒对方',
    createdAt: '昨天'
  }
];

export const mockMessages: MessageItem[] = [
  {
    id: 8001,
    title: '审核通过',
    content: '你的校园墙动态已通过审核，快去看看大家的互动吧。',
    messageType: 'audit',
    targetType: 'post',
    targetId: 4001,
    readFlag: 0,
    createdAt: '刚刚'
  },
  {
    id: 8002,
    title: '评论通知',
    content: '奶茶不加糖评论了你的帖子：“这也太好看了！”',
    messageType: 'comment',
    targetType: 'post',
    targetId: 4001,
    readFlag: 0,
    createdAt: '10分钟前'
  }
];

export const mockProfile: UserProfile = {
  id: 9001,
  tenantId: 1001,
  username: 'demo_user',
  nickname: '小宇同学',
  avatarUrl: '/static/default-avatar.png',
  college: '计算机学院',
  major: '软件工程',
  grade: '2023级',
  roleCode: 'STUDENT',
  authStatus: 2,
  status: 1
};
