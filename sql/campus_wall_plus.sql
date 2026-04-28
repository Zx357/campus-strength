/*
 Navicat Premium Data Transfer

 Source Server         : LocalMysql
 Source Server Type    : MySQL
 Source Server Version : 80044
 Source Host           : localhost:3306
 Source Schema         : campus_wall_plus

 Target Server Type    : MySQL
 Target Server Version : 80044
 File Encoding         : 65001

 Date: 28/04/2026 10:56:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for audit_record
-- ----------------------------
DROP TABLE IF EXISTS `audit_record`;
CREATE TABLE `audit_record`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '审核对象',
  `target_id` bigint NOT NULL COMMENT '对象ID',
  `audit_status` tinyint NOT NULL COMMENT '1通过 2驳回',
  `audit_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `auditor_id` bigint NOT NULL COMMENT '审核人',
  `audited_at` datetime NOT NULL COMMENT '审核时间',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_status`(`audit_status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '审核记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of audit_record
-- ----------------------------

-- ----------------------------
-- Table structure for biz_category
-- ----------------------------
DROP TABLE IF EXISTS `biz_category`;
CREATE TABLE `biz_category`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `module_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模块编码',
  `category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of biz_category
-- ----------------------------
INSERT INTO `biz_category` VALUES (2001, 1001, 'post', '校园墙', 'NotebookPen', 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `biz_category` VALUES (2002, 1001, 'lost', '失物招领', 'Search', 2, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `biz_category` VALUES (2003, 1001, 'goods', '数码电子', 'Monitor', 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `biz_category` VALUES (2004, 1001, 'goods', '图书教材', 'Reading', 2, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `biz_category` VALUES (2005, 1001, 'help', '校园互助', 'Help', 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `biz_category` VALUES (2006, 1001, 'activity', '活动社团', 'Calendar', 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);

-- ----------------------------
-- Table structure for campus_activity
-- ----------------------------
DROP TABLE IF EXISTS `campus_activity`;
CREATE TABLE `campus_activity`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `cover_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `activity_time` datetime NULL DEFAULT NULL COMMENT '活动时间',
  `activity_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '活动地点',
  `organizer` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织者',
  `signup_required` tinyint NULL DEFAULT 0 COMMENT '是否报名',
  `signup_deadline` datetime NULL DEFAULT NULL COMMENT '报名截止',
  `max_people` int NULL DEFAULT NULL COMMENT '人数上限',
  `status` tinyint NULL DEFAULT 0 COMMENT '0草稿 1展示中 2下架',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '活动社团表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of campus_activity
-- ----------------------------

-- ----------------------------
-- Table structure for campus_activity_signup
-- ----------------------------
DROP TABLE IF EXISTS `campus_activity_signup`;
CREATE TABLE `campus_activity_signup`  (
  `id` bigint NOT NULL COMMENT '??',
  `tenant_id` bigint NOT NULL COMMENT '??ID',
  `activity_id` bigint NOT NULL COMMENT '??ID',
  `user_id` bigint NOT NULL COMMENT '????ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '0?? 1???',
  `contact_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '????',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '??',
  `created_at` datetime NOT NULL COMMENT '????',
  `updated_at` datetime NOT NULL COMMENT '????',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '????',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_activity_user`(`tenant_id` ASC, `activity_id` ASC, `user_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_activity_id`(`activity_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '?????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of campus_activity_signup
-- ----------------------------

-- ----------------------------
-- Table structure for campus_help
-- ----------------------------
DROP TABLE IF EXISTS `campus_help`;
CREATE TABLE `campus_help`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` tinyint NOT NULL COMMENT '1求助 2代取 3拼单 4咨询 5其他',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `images` json NULL COMMENT '图片',
  `reward_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '悬赏金额',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `status` tinyint NULL DEFAULT 0 COMMENT '0待审核 1展示中 2已完成 3下架 4驳回',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '校园互助表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of campus_help
-- ----------------------------

-- ----------------------------
-- Table structure for file_record
-- ----------------------------
DROP TABLE IF EXISTS `file_record`;
CREATE TABLE `file_record`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件名',
  `original_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '原始文件名',
  `file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件地址',
  `file_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
  `storage_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'local' COMMENT 'local/oss/cos/minio',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of file_record
-- ----------------------------

-- ----------------------------
-- Table structure for lost_found
-- ----------------------------
DROP TABLE IF EXISTS `lost_found`;
CREATE TABLE `lost_found`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `type` tinyint NOT NULL COMMENT '1丢失 2捡到',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `images` json NULL COMMENT '图片',
  `lost_time` datetime NULL DEFAULT NULL COMMENT '丢失/捡到时间',
  `lost_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地点',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` tinyint NULL DEFAULT 0 COMMENT '0待审核 1展示中 2已找回 3已下架 4驳回',
  `audit_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览数',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '失物招领表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of lost_found
-- ----------------------------

-- ----------------------------
-- Table structure for message_record
-- ----------------------------
DROP TABLE IF EXISTS `message_record`;
CREATE TABLE `message_record`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `message_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'comment/like/audit/system/report',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标类型',
  `target_id` bigint NULL DEFAULT NULL COMMENT '目标ID',
  `read_flag` tinyint NULL DEFAULT 0 COMMENT '0未读 1已读',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`read_flag` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message_record
-- ----------------------------

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容',
  `type` tinyint NULL DEFAULT 1 COMMENT '1学校公告 2平台公告 3系统通知',
  `status` tinyint NULL DEFAULT 0 COMMENT '0草稿 1发布 2下架',
  `top_flag` tinyint NULL DEFAULT 0 COMMENT '是否置顶',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of notice
-- ----------------------------
INSERT INTO `notice` VALUES (5001, 1001, '新学期社团招新开始啦', '欢迎同学们关注校园墙+活动社团栏目。', 1, 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);

-- ----------------------------
-- Table structure for report_record
-- ----------------------------
DROP TABLE IF EXISTS `report_record`;
CREATE TABLE `report_record`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '举报用户ID',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'post/comment/goods/lost/user',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `reason_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报类型',
  `reason_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '举报内容',
  `images` json NULL COMMENT '图片',
  `status` tinyint NULL DEFAULT 0 COMMENT '0待处理 1已处理 2驳回',
  `handle_result` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '处理结果',
  `handler_id` bigint NULL DEFAULT NULL COMMENT '处理人',
  `handled_at` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '举报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of report_record
-- ----------------------------

-- ----------------------------
-- Table structure for second_goods
-- ----------------------------
DROP TABLE IF EXISTS `second_goods`;
CREATE TABLE `second_goods`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '描述',
  `images` json NULL COMMENT '图片',
  `price` decimal(10, 2) NOT NULL COMMENT '售价',
  `original_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '原价',
  `condition_level` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '成色',
  `trade_method` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '交易方式',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `contact_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` tinyint NULL DEFAULT 0 COMMENT '0待审核 1在售 2已售出 3已下架 4驳回',
  `audit_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览数',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_price`(`price` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '二手闲置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of second_goods
-- ----------------------------

-- ----------------------------
-- Table structure for sensitive_word
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_word`;
CREATE TABLE `sensitive_word`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID为空表示平台全局',
  `word` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '敏感词',
  `level` tinyint NULL DEFAULT 2 COMMENT '1提示 2拦截 3人工审核',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '敏感词表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sensitive_word
-- ----------------------------
INSERT INTO `sensitive_word` VALUES (3001, NULL, '违法', 2, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sensitive_word` VALUES (3002, NULL, '广告引流', 3, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL COMMENT '主键',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父级ID',
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'catalog/menu/button',
  `route_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '路由路径',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件',
  `permission` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图标',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (10, 0, '工作台', 'menu', '/campus/dashboard', 'campus/dashboard/index', 'dashboard:view', 'dashboard', 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (20, 0, '租户管理', 'menu', '/campus/tenant', 'campus/tenant/index', 'tenant:view', 'school', 2, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (30, 0, '用户管理', 'menu', '/campus/user', 'campus/user/index', 'user:view', 'user', 3, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (40, 0, '内容管理', 'catalog', '/campus/content', NULL, 'content:view', 'list', 4, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (41, 40, '校园墙帖子', 'menu', '/campus/content/post', 'campus/content/post/index', 'post:view', 'document', 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (42, 40, '评论管理', 'menu', '/campus/content/comment', 'campus/content/comment/index', 'comment:view', 'chat-dot-round', 2, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (43, 40, '失物招领', 'menu', '/campus/content/lost-found', 'campus/content/lost-found/index', 'lost:view', 'search', 3, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (44, 40, '二手闲置', 'menu', '/campus/content/goods', 'campus/content/goods/index', 'goods:view', 'goods', 4, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (45, 40, '校园互助', 'menu', '/campus/content/help', 'campus/content/help/index', 'help:view', 'help', 5, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (46, 40, '活动社团', 'menu', '/campus/content/activity', 'campus/content/activity/index', 'activity:view', 'calendar', 6, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (50, 0, '审核中心', 'menu', '/campus/audit', 'campus/audit/index', 'audit:view', 'check', 5, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (60, 0, '举报管理', 'menu', '/campus/report', 'campus/report/index', 'report:view', 'warning', 6, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (70, 0, '分类管理', 'menu', '/campus/category', 'campus/category/index', 'category:view', 'menu', 7, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (80, 0, '公告管理', 'menu', '/campus/notice', 'campus/notice/index', 'notice:view', 'bell', 8, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (90, 0, '敏感词管理', 'menu', '/campus/sensitive-word', 'campus/sensitive-word/index', 'sensitive:view', 'lock', 9, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_menu` VALUES (100, 0, '操作日志', 'menu', '/campus/operation-log', 'campus/operation-log/index', 'log:view', 'document-copy', 10, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
  `user_id` bigint NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `module_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名',
  `operation_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '操作类型',
  `request_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '请求地址',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '请求参数',
  `ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP',
  `user_agent` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'UA',
  `result` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结果',
  `error_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '错误信息',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_operation_log
-- ----------------------------
INSERT INTO `sys_operation_log` VALUES (2048587913339981825, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'fail', NULL, '2026-04-27 10:19:54', '2026-04-27 10:19:54', 0);
INSERT INTO `sys_operation_log` VALUES (2048588111105499137, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'fail', NULL, '2026-04-27 10:20:41', '2026-04-27 10:20:41', 0);
INSERT INTO `sys_operation_log` VALUES (2048588354417078274, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'success', NULL, '2026-04-27 10:21:39', '2026-04-27 10:21:39', 0);
INSERT INTO `sys_operation_log` VALUES (2048588425351147522, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'success', NULL, '2026-04-27 10:21:56', '2026-04-27 10:21:56', 0);
INSERT INTO `sys_operation_log` VALUES (2048589708283883522, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/147.0.0.0 Safari/537.36', 'success', NULL, '2026-04-27 10:27:02', '2026-04-27 10:27:02', 0);
INSERT INTO `sys_operation_log` VALUES (2048592421692387330, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Codex/26.422.30944 Chrome/146.0.7680.179 Electron/41.2.0 Safari/537.36', 'success', NULL, '2026-04-27 10:37:49', '2026-04-27 10:37:49', 0);
INSERT INTO `sys_operation_log` VALUES (2048592704778547201, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'success', NULL, '2026-04-27 10:38:56', '2026-04-27 10:38:56', 0);
INSERT INTO `sys_operation_log` VALUES (2048592755064057858, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'success', NULL, '2026-04-27 10:39:08', '2026-04-27 10:39:08', 0);
INSERT INTO `sys_operation_log` VALUES (2048592805253099521, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'success', NULL, '2026-04-27 10:39:20', '2026-04-27 10:39:20', 0);
INSERT INTO `sys_operation_log` VALUES (2048592912497258498, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '0:0:0:0:0:0:0:1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'success', NULL, '2026-04-27 10:39:46', '2026-04-27 10:39:46', 0);
INSERT INTO `sys_operation_log` VALUES (2048643870503804930, NULL, NULL, 'null', 'AdminAuthController', 'POST', 'POST', '/api/admin/auth/login', NULL, '127.0.0.1', 'Mozilla/5.0 (Windows NT; Windows NT 10.0; zh-CN) WindowsPowerShell/5.1.26100.8115', 'success', NULL, '2026-04-27 14:02:15', '2026-04-27 14:02:15', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL COMMENT '主键',
  `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `role_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色类型',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_code`(`role_code` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'PLATFORM_ADMIN', '平台超级管理员', 'platform', 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_role` VALUES (2, 'TENANT_ADMIN', '学校管理员', 'tenant', 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_role` VALUES (3, 'TENANT_AUDITOR', '学校审核员', 'tenant', 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_role` VALUES (4, 'STUDENT', '学生用户', 'app', 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_role` VALUES (5, 'GUEST', '游客', 'app', 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_role_menu`(`role_id` ASC, `menu_id` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (10010, 1, 10, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10020, 1, 20, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10030, 1, 30, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10040, 1, 40, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10041, 1, 41, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10042, 1, 42, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10043, 1, 43, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10044, 1, 44, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10045, 1, 45, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10046, 1, 46, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10050, 1, 50, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10060, 1, 60, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10070, 1, 70, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10080, 1, 80, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10090, 1, 90, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (10100, 1, 100, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20010, 2, 10, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20030, 2, 30, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20040, 2, 40, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20041, 2, 41, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20042, 2, 42, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20043, 2, 43, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20044, 2, 44, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20045, 2, 45, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20046, 2, 46, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20050, 2, 50, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20060, 2, 60, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20070, 2, 70, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20080, 2, 80, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20090, 2, 90, '2026-04-27 10:00:00', NULL, 0);
INSERT INTO `sys_role_menu` VALUES (20100, 2, 100, '2026-04-27 10:00:00', NULL, 0);

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校编码',
  `tenant_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '学校名称',
  `logo_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Logo地址',
  `theme_color` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '#18B66A' COMMENT '主题色',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` tinyint NULL DEFAULT 1 COMMENT '0停用 1启用',
  `audit_mode` tinyint NULL DEFAULT 1 COMMENT '0先发后审 1先审后发',
  `allow_anonymous` tinyint NULL DEFAULT 1 COMMENT '是否允许匿名',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_code`(`tenant_code` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (1001, 'xhxy', '星河大学', NULL, '#18B66A', '张老师', '13800000000', 1, 1, 1, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT NULL COMMENT '租户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `openid` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信openid',
  `student_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学号',
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `college` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '学院',
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '专业',
  `grade` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '年级',
  `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色编码',
  `auth_status` tinyint NULL DEFAULT 0 COMMENT '认证状态：0未认证 1待审核 2已认证 3驳回',
  `status` tinyint NULL DEFAULT 1 COMMENT '0禁用 1正常',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, NULL, 'admin', '123456', '平台管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'PLATFORM_ADMIN', 2, 1, '2026-04-27 14:02:15', '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_user` VALUES (2, 1001, 'tenant_admin', '123456', '星河大学管理员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'TENANT_ADMIN', 2, 1, NULL, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_user` VALUES (3, 1001, 'auditor', '123456', '星河审核员', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'TENANT_AUDITOR', 2, 1, NULL, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_user` VALUES (4, 1001, 'student', '123456', '小宇同学', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'STUDENT', 2, 1, NULL, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);
INSERT INTO `sys_user` VALUES (2048599997351997442, 1001, 'wx_b6073eb7', '', '小宇同学', NULL, NULL, 'mock_03c7769b-eab0-40d5-97f0-6cd32a243058', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 11:07:55', '2026-04-27 11:07:55', 0);
INSERT INTO `sys_user` VALUES (2048600180525641730, 1001, 'wx_288d18a5', '', '小宇同学', NULL, NULL, 'mock_89809157-e265-47e4-bb1a-5125ffac7692', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 11:08:39', '2026-04-27 11:08:39', 0);
INSERT INTO `sys_user` VALUES (2048606602193809410, 1001, 'wx_c2ae83c9', '', '?????', NULL, NULL, 'mock_b7110f1e-d045-4f65-9b84-e215bbb1f1b3', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 11:34:10', '2026-04-27 11:34:10', 0);
INSERT INTO `sys_user` VALUES (2048630244382355458, 1001, 'wx_ee309ec9', '', '校园同学', NULL, NULL, 'mock_0c1KLDGa1dBCAL0dJnJa1BQsWF2KLDGY', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 13:08:07', '2026-04-27 13:08:07', 0);
INSERT INTO `sys_user` VALUES (2048631595829047298, 1001, 'wx_ad9f29de', '', '校园同学', NULL, NULL, 'mock_0f1ZZ5Ga1KK9BL0tGxJa18tIrP3ZZ5GX', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 13:13:29', '2026-04-27 13:13:29', 0);
INSERT INTO `sys_user` VALUES (2048639163154575361, 1001, 'wx_603a66a7', '', '校园同学', NULL, NULL, 'mock_0d1b8rll23R2Bh4pqvll2iaQ6E3b8rl4', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 13:43:33', '2026-04-27 13:43:33', 0);
INSERT INTO `sys_user` VALUES (2048644726288650242, 1001, 'wx_7a20ba02', '', '校园同学', NULL, NULL, 'mock_0d1c8SFa12nnBL0ZlKGa18bp5x0c8SFO', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 14:05:39', '2026-04-27 14:05:39', 0);
INSERT INTO `sys_user` VALUES (2048644845843091457, 1001, 'wx_e8da590b', '', '校园同学', NULL, NULL, 'mock_0d1V3kFa1TsVBL0tFsJa1nNTLn2V3kF6', NULL, NULL, NULL, NULL, NULL, 'STUDENT', 0, 1, NULL, '2026-04-27 14:06:08', '2026-04-27 14:06:08', 0);

-- ----------------------------
-- Table structure for wall_collect
-- ----------------------------
DROP TABLE IF EXISTS `wall_collect`;
CREATE TABLE `wall_collect`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'post/goods/lost',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_collect`(`tenant_id` ASC, `user_id` ASC, `target_type` ASC, `target_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wall_collect
-- ----------------------------

-- ----------------------------
-- Table structure for wall_comment
-- ----------------------------
DROP TABLE IF EXISTS `wall_comment`;
CREATE TABLE `wall_comment`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `post_id` bigint NOT NULL COMMENT '帖子ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父评论ID',
  `reply_user_id` bigint NULL DEFAULT NULL COMMENT '回复用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `status` tinyint NULL DEFAULT 1 COMMENT '0待审核 1正常 2驳回 3删除',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_post_id`(`post_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wall_comment
-- ----------------------------

-- ----------------------------
-- Table structure for wall_like
-- ----------------------------
DROP TABLE IF EXISTS `wall_like`;
CREATE TABLE `wall_like`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `target_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'post/comment',
  `target_id` bigint NOT NULL COMMENT '目标ID',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_like`(`tenant_id` ASC, `user_id` ASC, `target_type` ASC, `target_id` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wall_like
-- ----------------------------

-- ----------------------------
-- Table structure for wall_post
-- ----------------------------
DROP TABLE IF EXISTS `wall_post`;
CREATE TABLE `wall_post`  (
  `id` bigint NOT NULL COMMENT '主键',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `title` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容',
  `images` json NULL COMMENT '图片JSON数组',
  `topic_tags` json NULL COMMENT '话题标签JSON数组',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `anonymous` tinyint NULL DEFAULT 0 COMMENT '匿名发布',
  `status` tinyint NULL DEFAULT 0 COMMENT '0待审核 1已发布 2已驳回 3已删除 4已下架',
  `audit_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核原因',
  `like_count` int NULL DEFAULT 0 COMMENT '点赞数',
  `comment_count` int NULL DEFAULT 0 COMMENT '评论数',
  `collect_count` int NULL DEFAULT 0 COMMENT '收藏数',
  `view_count` int NULL DEFAULT 0 COMMENT '浏览数',
  `hot_score` decimal(12, 2) NULL DEFAULT 0.00 COMMENT '热度分',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_created_at`(`created_at` ASC) USING BTREE,
  INDEX `idx_hot_score`(`hot_score` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '校园墙帖子表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of wall_post
-- ----------------------------
INSERT INTO `wall_post` VALUES (4001, 1001, 4, 2001, '今晚图书馆晚霞', '今天的晚霞也太美了吧，在图书馆后面拍到的，分享给大家！', '[]', '[\"晚霞\", \"校园生活\"]', '星河大学·图书馆', 0, 1, NULL, 128, 32, 15, 522, 88.00, '2026-04-27 10:00:00', '2026-04-27 10:00:00', 0);

SET FOREIGN_KEY_CHECKS = 1;
