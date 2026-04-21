/*
 Navicat Premium Data Transfer

 Source Server         : MySQL-Master（106.54.209.225）
 Source Server Type    : MySQL
 Source Server Version : 80025
 Source Host           : 106.54.209.225:3307
 Source Schema         : cuhk_mkt

 Target Server Type    : MySQL
 Target Server Version : 80025
 File Encoding         : 65001

 Date: 28/08/2021 13:47:50
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for biz_approval_record
-- ----------------------------
DROP TABLE IF EXISTS `biz_approval_record`;
CREATE TABLE `biz_approval_record` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
  `course_master_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '课程主表id',
  `operator_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作人名称',
  `action_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '动作名称',
  `remark` text COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模块-审批记录表';

-- ----------------------------
-- Table structure for biz_course_category
-- ----------------------------
DROP TABLE IF EXISTS `biz_course_category`;
CREATE TABLE `biz_course_category` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
  `major` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业',
  `category_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程分类编号',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程分类名称',
  `step` int DEFAULT '0' COMMENT '步骤',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uq_major_code` (`category_code`,`major`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模块-课程分类表';

-- ----------------------------
-- Table structure for biz_course_detail
-- ----------------------------
DROP TABLE IF EXISTS `biz_course_detail`;
CREATE TABLE `biz_course_detail` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
  `course_master_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程主id',
  `course_category_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程分类编码',
  `course_part` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '课程子部分',
  `course_template_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '课程模板id',
  `course_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程编码',
  `course_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程标题',
  `units` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '得分',
  `self_check` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '自检',
  `minor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '选修课',
  `passed_not_release` varchar(100) DEFAULT NULL,
  `in_process` varchar(100) DEFAULT NULL,
  `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '备注',
  `front_show` int DEFAULT '0' COMMENT '是否显示（0：不显示，1：显示）',
  `attr1` varchar(100) DEFAULT NULL COMMENT '扩展属性1',
  `attr2` varchar(100) DEFAULT NULL COMMENT '扩展属性2',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模块-学生课程数据明细表';

-- ----------------------------
-- Table structure for biz_course_master
-- ----------------------------
DROP TABLE IF EXISTS `biz_course_master`;
CREATE TABLE `biz_course_master` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
  `status` int NOT NULL DEFAULT '0' COMMENT '状态（0：草稿，1：待审核，2、已驳回，3、已完成）',
  `student_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '申请人id（学生）',
  `student_submit_time` datetime DEFAULT NULL COMMENT '提交时间（学生）',
  `file_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件id',
  `signature_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '学生签名图片url',
  `signature_date` date DEFAULT NULL COMMENT '学生签字日期',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `teacher_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '审批人id（老师）',
  `teacher_approve_time` datetime DEFAULT NULL COMMENT '审批日期（老师）',
  `reason` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '打回原因',
  `officer_checked_date` date DEFAULT NULL COMMENT '检查日期（老师）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模块-学生课程数据主表';

-- ----------------------------
-- Table structure for biz_course_template
-- ----------------------------
DROP TABLE IF EXISTS `biz_course_template`;
CREATE TABLE `biz_course_template` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
  `course_category_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程分类id',
  `course_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程编号',
  `course_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程标题',
  `part` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '课程子部分',
  `units` int DEFAULT NULL COMMENT '最高评分',
  `sort_index` int DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模块-课程模板表';

-- ----------------------------
-- Table structure for biz_email_record
-- ----------------------------
DROP TABLE IF EXISTS `biz_email_record`;
CREATE TABLE `biz_email_record` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本号',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '备注信息',
  `biz_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务数据id',
  `biz_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '业务类型',
  `mail_from` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '发送邮箱',
  `mail_to` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '接收邮箱，多个用逗号分隔',
  `status` int DEFAULT '0' COMMENT '邮件状态（0：待发送，1：成功，2：失败）',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '主题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '邮件内容',
  `retrys` int DEFAULT '0' COMMENT '重试次数',
  `error_info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '错误信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='业务模块-邮件记录表';

-- ----------------------------
-- Table structure for sys_ad_org_temp
-- ----------------------------
DROP TABLE IF EXISTS `sys_ad_org_temp`;
CREATE TABLE `sys_ad_org_temp` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int DEFAULT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本',
  `description` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `batch_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '批次号',
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '英文名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `distinguished_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '层级',
  `title` varchar(255) DEFAULT NULL COMMENT '职称',
  `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父id',
  `has_children` int DEFAULT NULL COMMENT '是否有子集',
  `weight` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '权重',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统模块-ad组织临时表';

-- ----------------------------
-- Table structure for sys_ad_student_temp
-- ----------------------------
DROP TABLE IF EXISTS `sys_ad_student_temp`;
CREATE TABLE `sys_ad_student_temp` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int DEFAULT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本',
  `description` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `batch_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '批次号',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组织编码',
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '英文名称',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `employee_type` varchar(255) DEFAULT NULL COMMENT '性别(F M)',
  `employee_id` varchar(255) DEFAULT NULL COMMENT '员工号',
  `mail` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mail_nick_name` varchar(255) DEFAULT NULL COMMENT '邮箱前缀',
  `title` varchar(255) DEFAULT NULL COMMENT '职称',
  `distinguished_name` varchar(255) DEFAULT NULL COMMENT '层级',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_employee_id` (`employee_id`) USING BTREE,
  KEY `idx_batch_number` (`batch_number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统模块-ad学生临时表';

-- ----------------------------
-- Table structure for sys_ad_teacher_temp
-- ----------------------------
DROP TABLE IF EXISTS `sys_ad_teacher_temp`;
CREATE TABLE `sys_ad_teacher_temp` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime DEFAULT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime DEFAULT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int DEFAULT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本',
  `description` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `batch_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '批次号',
  `department` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组织编码',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '名称',
  `display_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '英文名称',
  `employee_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '员工号',
  `ip_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工作电话',
  `mail` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `mail_nick_name` varchar(255) DEFAULT NULL COMMENT '邮箱前缀',
  `title` varchar(255) DEFAULT NULL COMMENT '职称',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机号',
  `physical_delivery_office_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '工作地址',
  `distinguished_name` varchar(255) DEFAULT NULL COMMENT '层级',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_employee_id` (`employee_id`) USING BTREE,
  KEY `idx_batch_number` (`batch_number`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统模块-ad老师临时表';

-- ----------------------------
-- Table structure for sys_file_store
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_store`;
CREATE TABLE `sys_file_store` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本',
  `description` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `file_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件名称（不带后缀）',
  `origin_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '原文件名称（带后缀）',
  `file_size` bigint DEFAULT '0' COMMENT '文件大小',
  `extension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件后缀',
  `content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '文件类型',
  `file_url` varchar(255) DEFAULT NULL COMMENT '源文件url',
  `compress_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '压缩图url',
  `thumbnail_url` varchar(255) DEFAULT NULL COMMENT '缩略图url',
  `preview_url` varchar(255) DEFAULT NULL COMMENT '预览图url',
  `md5` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT 'minio object md5',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统模块-文件表';

-- ----------------------------
-- Table structure for sys_operate_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本',
  `description` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `category_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类编号',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类名称',
  `attr1` varchar(255) DEFAULT '0' COMMENT '字段1',
  `attr2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段2',
  `attr3` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段3',
  `attr4` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段4',
  `attr5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段5',
  `attr6` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段6',
  `attr7` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段7',
  `attr8` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段8',
  `attr9` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段9',
  `attr10` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '0' COMMENT '字段10',
  `attr11` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '字段11',
  `attr12` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '字段12',
  `attr13` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '字段13',
  `attr14` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '字段14',
  `attr15` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '字段15',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统模块-操作日志表';

-- ----------------------------
-- Table structure for sys_profiles
-- ----------------------------
DROP TABLE IF EXISTS `sys_profiles`;
CREATE TABLE `sys_profiles` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键id',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '更新时间',
  `updated_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '更新人',
  `remove_flag` int NOT NULL DEFAULT '0' COMMENT '删除标识',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '描述',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '编码',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '变量值',
  `status` int DEFAULT '0' COMMENT '启用状态（0：禁用，1：启用）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统模块-全局配置表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(50) NOT NULL COMMENT '主键-uuid',
  `created_date` datetime NOT NULL COMMENT '创建时间',
  `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `updated_date` datetime NOT NULL COMMENT '最后修改时间',
  `updated_by` varchar(50) DEFAULT NULL COMMENT '最后更新人',
  `remove_flag` int NOT NULL COMMENT '删除标识（0：正常，1：已删除）',
  `version` bigint DEFAULT '0' COMMENT '数据版本',
  `description` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `login_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账号',
  `password` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密码',
  `user_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户类型（STUDENT：学生，TEACHER：老师，SYSTEM：系统账号）',
  `status` int NOT NULL DEFAULT '0' COMMENT '状态（0：无效，1：有效）',
  `name_ch` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '中文名称',
  `name_en` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '英文名称',
  `major` varchar(255) DEFAULT NULL COMMENT '主修专业',
  `minor` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '选修课程',
  `expected_year` varchar(20) DEFAULT NULL COMMENT '预计毕业年份',
  `area_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '手机区号',
  `contact_tel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '联系电话',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uq_login_name` (`login_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统模块-用户表';

SET FOREIGN_KEY_CHECKS = 1;
