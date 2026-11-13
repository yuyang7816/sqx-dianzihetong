/*
 电子合同（e-contract）开源数据库脚本

 Source Server Type    : MySQL
 Source Server Version : 5.7 / 8.0
 Source Schema         : dianzihetong

 说明：
 1. 包含全部表结构
 2. 包含管理端菜单 / 角色 / 角色菜单权限
 3. 包含演示管理员：admin / admin123（绑定超级管理员角色）
 4. common_info / sys_config 中的第三方密钥、域名、证书已清空，请在管理端自行配置
 5. 不含真实用户、合同、企业等业务数据

 导入前请先创建库：
   CREATE DATABASE IF NOT EXISTS dianzihetong DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
DROP TABLE IF EXISTS `app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'app升级配置id',
  `android_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '安卓升级地址',
  `create_at` varchar(600) DEFAULT NULL COMMENT '创建时间',
  `des` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `ios_version` varchar(255) DEFAULT NULL COMMENT '苹果版本',
  `ios_wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '苹果升级地址',
  `method` varchar(600) DEFAULT NULL COMMENT '是否强制升级',
  `version` varchar(600) DEFAULT NULL COMMENT '安卓版本',
  `wgt_url` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '通用下载地址',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC COMMENT='app升级配置';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'bannerid',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `name` varchar(64) DEFAULT NULL COMMENT '名称',
  `image_url` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `state` int(2) DEFAULT NULL COMMENT '状态1正常2隐藏',
  `classify` int(2) DEFAULT NULL COMMENT '分类1banner图2金刚区分类',
  `url` varchar(255) DEFAULT NULL COMMENT '跳转地址 ',
  `sort` int(4) DEFAULT NULL COMMENT '顺序',
  `describes` varchar(255) DEFAULT NULL COMMENT '描述',
  `type` int(1) DEFAULT NULL COMMENT '分类 1首页 2好店',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='banner图表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `cash_out`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cash_out` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '申请提现id',
  `create_at` varchar(255) DEFAULT NULL COMMENT '申请时间',
  `is_out` int(2) DEFAULT NULL COMMENT '是否转账',
  `money` decimal(10,2) DEFAULT NULL COMMENT '提现金额',
  `out_at` varchar(255) DEFAULT NULL COMMENT '转账时间',
  `relation_id` varchar(255) DEFAULT NULL COMMENT '会员编号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `zhifubao` varchar(255) DEFAULT NULL COMMENT '支付宝账号',
  `zhifubao_name` varchar(64) DEFAULT NULL COMMENT '支付宝姓名',
  `order_number` varchar(255) DEFAULT NULL COMMENT '订单编号',
  `state` int(11) DEFAULT NULL COMMENT '状态 0待转账 1成功 -1退款',
  `refund` varchar(1000) DEFAULT NULL COMMENT '退款原因',
  `classify` int(11) DEFAULT NULL COMMENT '提现方式 1支付宝 2微信小程序  3微信公众号',
  `wx_img` varchar(600) DEFAULT NULL COMMENT '微信图片',
  `rate` varchar(10) DEFAULT NULL COMMENT '利息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='提现表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `chat_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_content` (
  `chat_content_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天内容id',
  `chat_conversation_id` int(11) DEFAULT NULL COMMENT '聊天会话id',
  `content` varchar(2000) DEFAULT NULL COMMENT '内容',
  `message_type` int(11) DEFAULT NULL COMMENT '类型 1文字 2图片 3语音',
  `width` varchar(64) DEFAULT NULL COMMENT '图片宽度',
  `height` varchar(64) DEFAULT NULL COMMENT '图片高度',
  `user_id` int(11) DEFAULT NULL COMMENT '发送用户id',
  `status` int(11) DEFAULT NULL COMMENT '0未读 1已读',
  `create_time` varchar(64) DEFAULT NULL COMMENT '发送时间',
  `voice_length` varchar(64) DEFAULT NULL COMMENT '语音长度',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=825 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天内容表（暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `chat_conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chat_conversation` (
  `chat_conversation_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '聊天会话id',
  `user_id` int(11) DEFAULT NULL COMMENT '发送用户id',
  `focused_user_id` int(11) DEFAULT NULL COMMENT '接受用户id',
  `status` varchar(255) DEFAULT NULL COMMENT '状态',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_send_msg` int(11) DEFAULT NULL,
  `is_wx_msg` int(11) DEFAULT NULL,
  PRIMARY KEY (`chat_conversation_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='暂未使用';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `chats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats` (
  `chat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话id',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `user_count` int(20) DEFAULT '0' COMMENT '用户未读条数',
  `user_count2` int(20) DEFAULT '0' COMMENT '好友未读条数',
  `user_head` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `user_head2` varchar(255) DEFAULT NULL COMMENT '好友头像',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) DEFAULT NULL COMMENT '好友id',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户昵称',
  `user_name2` varchar(255) DEFAULT NULL COMMENT '好友昵称',
  `store_count` int(11) DEFAULT '0' COMMENT '后台未读条数',
  `store_head` varchar(255) DEFAULT NULL COMMENT '后台头像',
  `store_id` bigint(20) DEFAULT NULL COMMENT '总后台id(总后台传0',
  `store_name` varchar(255) DEFAULT NULL COMMENT '商户昵称',
  PRIMARY KEY (`chat_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=149 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天会话（暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `chats_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chats_content` (
  `chat_content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会话内容id',
  `chat_id` bigint(20) DEFAULT NULL COMMENT '会话Id',
  `content` text COMMENT '聊天内容',
  `create_time` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `send_type` bigint(20) DEFAULT NULL COMMENT '消息来源（1用户消息 2好友消息）',
  `status` int(11) DEFAULT '1' COMMENT '是否已读(1未读 2已读)',
  `type` int(1) DEFAULT NULL COMMENT '类型（1文字 2图片 3链接）',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_id2` bigint(20) DEFAULT NULL COMMENT '好友id',
  `store_id` bigint(20) DEFAULT NULL COMMENT '后台id（总后台传0）',
  PRIMARY KEY (`chat_content_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1249 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='聊天会话内容（暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `common_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `common_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置文件id',
  `create_at` varchar(255) DEFAULT NULL COMMENT '创建时间',
  `max` varchar(255) DEFAULT NULL COMMENT '暂未使用',
  `min` varchar(2000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '配置文件名称',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '值',
  `condition_from` varchar(255) DEFAULT NULL COMMENT '分类',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=805 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC COMMENT='暂未使用';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同id',
  `template_id` int(11) DEFAULT NULL COMMENT '合同模板id',
  `contract_name` varchar(255) DEFAULT NULL COMMENT '合同名称',
  `contract_number` varchar(255) DEFAULT NULL COMMENT '合同编号',
  `contract_type` int(1) DEFAULT NULL COMMENT '1模板创建  2微信创建  3草稿箱创建 4个人签署合同',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `data_json` text COMMENT 'json数据',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `file_url` text COMMENT '上传的文件路径',
  `annex_url` text COMMENT '附件',
  `is_complete` int(1) DEFAULT '0' COMMENT '合同是否已签署完成：0未完成 1已完成 2已审批 3已过期',
  `draft_status` int(1) DEFAULT '0' COMMENT '合同是否加入草稿箱：0未加入  1已加入',
  `late_sign_days` int(10) DEFAULT NULL COMMENT '最晚签署天数',
  `contract_user_name` varchar(255) DEFAULT NULL COMMENT '合同发起人姓名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=184 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='合同表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract_additional_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_additional_fields` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同额外字段表id',
  `contract_id` int(11) DEFAULT NULL COMMENT '合同id',
  `listed_name` varchar(255) DEFAULT NULL COMMENT '合同实例表额外字段名',
  `column_comment` varchar(255) DEFAULT NULL COMMENT '合同实例表额外字段值',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='合同额外字段表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract_and_signed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_and_signed` (
  `id` int(11) NOT NULL COMMENT '签署或审批人与合同表id',
  `contract_id` int(11) DEFAULT NULL COMMENT '模板合同、微信合同、草稿箱合同id',
  `signed_by` int(11) DEFAULT NULL COMMENT '签署人或审批人id',
  `status` int(1) DEFAULT NULL COMMENT '0 未签署 1未审批 2已签署  3已审批',
  `start_time` date DEFAULT NULL COMMENT '合同开始时间',
  `end_time` date DEFAULT NULL COMMENT '合同结束时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sign_or_approved` int(1) DEFAULT NULL COMMENT '是签署还是审批： 0签署  1审批',
  `copy_user` int(11) DEFAULT NULL COMMENT '抄送人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='签署或审批人与合同相关表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract_and_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_and_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '发起人和合同关联表id',
  `contract_id` int(11) DEFAULT NULL COMMENT '模板合同、微信合同、草稿箱合同id',
  `user_id` int(11) DEFAULT NULL COMMENT '发起人id',
  `signed_by` int(11) DEFAULT NULL COMMENT '签署人或审批人或收件人id',
  `status` int(1) DEFAULT NULL COMMENT '0 未签署 1未审批 2已签署  3已审批 4已过期',
  `draft_status` int(1) DEFAULT '0' COMMENT '是否加入草稿箱：0未加入  1已加入',
  `start_time` date DEFAULT NULL COMMENT '合同开始时间',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人',
  `end_time` date DEFAULT NULL COMMENT '合同结束时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sign_or_approved` int(1) DEFAULT '0' COMMENT '是签署还是审批： 0签署  1审批 2不签署',
  `copy_user` int(11) DEFAULT NULL COMMENT '抄送人',
  `type` int(1) DEFAULT NULL COMMENT '合同类型：1企业  2个人',
  `sign_url` varchar(255) DEFAULT NULL COMMENT '签章或审批或公司章图片',
  `sign_date` date DEFAULT NULL COMMENT '签署日期',
  `last_date` date DEFAULT NULL COMMENT '最晚签署日期',
  `documents_url` text COMMENT '微信发起的合同文件',
  `annex_url` text COMMENT '微信发起的附件',
  `receiving_user_name` varchar(255) DEFAULT NULL COMMENT '收件人姓名',
  `receiving_email` varchar(255) DEFAULT NULL COMMENT '收件人邮箱',
  `is_sign` int(11) DEFAULT NULL COMMENT '是否签署：0未签署  1签署',
  `sign_user_name` varchar(255) DEFAULT NULL COMMENT '签署或审批人姓名',
  `copy_user_name` varchar(255) DEFAULT NULL COMMENT '抄送人姓名',
  `contract_type` int(1) DEFAULT NULL COMMENT '1企业 2个人',
  `user_name` varchar(255) DEFAULT NULL COMMENT '发起人姓名',
  `id_card` varchar(255) DEFAULT NULL COMMENT '发起人身份证号',
  `id_card_number` varchar(255) DEFAULT NULL COMMENT '签署人身份证号',
  `user_phone` varchar(255) DEFAULT NULL COMMENT '发起人手机号',
  `sign_user_phone` varchar(255) DEFAULT NULL COMMENT '签署人手机号',
  `is_principal` int(1) DEFAULT NULL COMMENT '甲方乙方标识：1甲方  2乙方',
  `user_legal_person` varchar(255) DEFAULT NULL COMMENT '自己填写自己的企业法人',
  `sign_legal_person` varchar(255) DEFAULT NULL COMMENT '自己填写的对方的企业法人',
  `agreement_type` int(1) DEFAULT NULL COMMENT '合同类型：1企业 2个人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=262 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='发起人和合同关联表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract_seal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_seal` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同印章id',
  `seal_url` varchar(255) DEFAULT NULL COMMENT '个人印章路径',
  `sign_url` varchar(255) DEFAULT NULL COMMENT '个人签名路径',
  `company_seal` varchar(255) DEFAULT NULL COMMENT '公司印章路径',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `sign_type` int(11) DEFAULT NULL COMMENT '1手动签名 2系统生成签名 3全部',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='印章表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同模板id',
  `con_number` varchar(255) DEFAULT NULL COMMENT '合同编号',
  `template_name` varchar(255) DEFAULT NULL COMMENT '合同模板名称',
  `contract_url` text COMMENT '合同模板文档',
  `contract_img` text COMMENT '合同模版图片',
  `type_id` int(11) DEFAULT NULL COMMENT '模版类型id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='合同模板表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract_template_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_template_fields` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同模板额外字段表id',
  `template_id` int(11) DEFAULT NULL COMMENT '合同模板id',
  `field_name` varchar(255) DEFAULT NULL COMMENT '合同模板额外字段名',
  `filed_value` varchar(255) DEFAULT NULL COMMENT '合同模板额外字段值',
  `sort` int(10) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `is_required` int(1) NOT NULL COMMENT '是否必填（1是）',
  `html_type` int(1) DEFAULT NULL COMMENT '显示类型（1文本框、2文本域、3下拉框、4复选框、5单选框、6日期控件、7数字类型）',
  `option_json` text COMMENT '选项值',
  `show_type` int(1) DEFAULT NULL COMMENT '展示类型 1仅用户端展示 2仅管理端展示 3都展示',
  `column_length` int(10) DEFAULT NULL COMMENT '列长度',
  `column_name` varchar(255) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='合同模板额外字段表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `contract_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contract_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同模版类型表id',
  `type_name` varchar(255) DEFAULT NULL COMMENT '类型名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='合同模版类型表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `enterprise`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '企业id',
  `enterprise_name` varchar(255) DEFAULT NULL COMMENT '企业名称',
  `company_address` varchar(255) DEFAULT NULL COMMENT '企业所在位置',
  `legal_person` varchar(255) DEFAULT NULL COMMENT '法人',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `registered_capital` varchar(255) DEFAULT NULL COMMENT '注册资金',
  `registration_time` datetime DEFAULT NULL COMMENT '注册时间',
  `start_time` time DEFAULT NULL COMMENT '营业开始时间',
  `end_time` time DEFAULT NULL COMMENT '营业结束时间',
  `front_photo_business_license` varchar(255) DEFAULT NULL COMMENT '营业执照正面照片',
  `back_photo_business_license` varchar(255) DEFAULT NULL COMMENT '营业执照反面照片',
  `status` int(1) DEFAULT NULL COMMENT '审批状态：0待审核 1审核通过 2已拒绝',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user_id` int(11) DEFAULT NULL COMMENT '修改人id',
  `is_real` int(1) DEFAULT '0' COMMENT '实名认证状态：0未认证  1已认证',
  `audit_reason` varchar(255) DEFAULT NULL COMMENT '审核理由',
  `credit_code` varchar(255) DEFAULT NULL COMMENT '纳税号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='企业信息表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `enterprise_and_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enterprise_and_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '企业和用户关联表id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `enterprise_id` int(11) DEFAULT NULL COMMENT '企业id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='企业和用户关联表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `help_classify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `help_classify` (
  `help_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助中心分类id',
  `help_classify_name` varchar(500) DEFAULT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `parent_id` int(11) DEFAULT NULL COMMENT '上级id',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_classify_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='帮助中心分类表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `help_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `help_word` (
  `help_word_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '帮助文档id',
  `help_word_title` varchar(255) DEFAULT NULL COMMENT '帮助标题',
  `help_word_content` longtext COMMENT '帮助文档内容',
  `help_classify_id` int(11) DEFAULT NULL COMMENT '帮助分类id',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`help_word_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='帮助文档表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `invite`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invite` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` int(11) DEFAULT NULL COMMENT '邀请者id',
  `invitee_user_id` int(11) DEFAULT NULL COMMENT '被邀请者id',
  `money` decimal(10,2) DEFAULT NULL COMMENT '收益',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `money_type` int(1) DEFAULT NULL COMMENT '1会员2陪玩',
  `state` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=966 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='邀请信息（暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `invite_money`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invite_money` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '收益钱包id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `money_sum` decimal(10,2) DEFAULT NULL COMMENT '总获取收益',
  `money` decimal(10,2) DEFAULT NULL COMMENT '当前金额',
  `cash_out` decimal(10,2) DEFAULT NULL COMMENT '累计提现',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='暂未使用';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `member_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员特权id',
  `member_img` varchar(255) DEFAULT NULL COMMENT '特权图标',
  `member_name` varchar(255) DEFAULT NULL COMMENT '特权名称',
  `sort` varchar(255) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`member_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='会员特权表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `message_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '内容',
  `create_at` varchar(600) DEFAULT NULL COMMENT '创建时间',
  `image` varchar(600) DEFAULT NULL COMMENT '图片',
  `is_see` varchar(600) DEFAULT NULL COMMENT '是否已读',
  `send_state` varchar(600) DEFAULT NULL COMMENT '发送状态',
  `send_time` varchar(600) DEFAULT NULL COMMENT '发送时间',
  `state` varchar(600) DEFAULT NULL COMMENT '1为公告2位用户反馈 3为投诉服务 4为投诉店铺 5为用户消息 6客服消息',
  `title` varchar(600) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题',
  `url` varchar(600) DEFAULT NULL COMMENT '地址',
  `type` varchar(600) DEFAULT NULL COMMENT '类型',
  `platform` int(11) DEFAULT NULL COMMENT '来源',
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '用户名',
  `audit_content` varchar(1000) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '审核原因',
  `status` int(11) DEFAULT NULL COMMENT '暂未使用',
  `by_user_id` int(11) DEFAULT NULL COMMENT '查询用户id（暂未使用）',
  `platform_id` int(11) DEFAULT NULL COMMENT '来源id（暂未使用）',
  `shop_id` int(11) DEFAULT NULL COMMENT '店铺id',
  `data_id` int(11) DEFAULT NULL COMMENT '服务id',
  `phone` varchar(25) DEFAULT NULL COMMENT '联系方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=421 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC COMMENT='消息表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `msg` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `code` varchar(255) DEFAULT NULL COMMENT '短信验证码',
  `phone` varchar(255) DEFAULT NULL COMMENT '电话',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_name` (`code`,`phone`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3452 DEFAULT CHARSET=big5 ROW_FORMAT=DYNAMIC COMMENT='验证码表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pay_classify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pay_classify` (
  `pay_classify_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '合同签署套餐id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '售价',
  `executed` varchar(255) DEFAULT NULL COMMENT '合同签署套餐名称',
  `number_sign` int(11) DEFAULT NULL COMMENT '份数',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` varchar(64) DEFAULT NULL COMMENT '时间',
  PRIMARY KEY (`pay_classify_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='合同签署套餐';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `pay_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pay_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '充值id',
  `classify` varchar(4) DEFAULT NULL COMMENT ' 1app  2微信二维码支付  3微信小程序 公众号支付 4支付宝APP支付 5支付宝H5支付',
  `order_id` varchar(32) DEFAULT NULL COMMENT '订单编号',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '支付单号',
  `money` decimal(10,2) DEFAULT NULL COMMENT '充值金额',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `state` int(4) DEFAULT NULL COMMENT '0待支付 1支付成功 2失败',
  `create_time` varchar(64) DEFAULT NULL COMMENT '创建时间',
  `pay_time` varchar(64) DEFAULT NULL COMMENT '支付时间',
  `type` int(4) DEFAULT NULL COMMENT '购买类型 1充值余额 2购买会员',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `parent_id` int(11) DEFAULT NULL COMMENT '关联id(如果是购买会员则是会员类型id)',
  `stripe_id` varchar(255) DEFAULT NULL COMMENT 'stripeId',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=680 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='支付记录表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sc_table_class`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sc_table_class` (
  `class_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `class_name` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `class_img` text COMMENT '图标',
  `is_enable` int(1) DEFAULT NULL COMMENT '是否启用 0不启用 1启用',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_user_id` int(11) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`class_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='业务分类表（暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sc_table_column`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sc_table_column` (
  `column_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `contract_id` varchar(64) DEFAULT NULL COMMENT '合同id',
  `column_name` varchar(200) NOT NULL COMMENT '列名称',
  `column_comment` varchar(500) NOT NULL COMMENT '列描述',
  `column_length` int(10) DEFAULT NULL COMMENT '列长度',
  `is_required` int(1) NOT NULL COMMENT '是否必填（1是）',
  `html_type` int(1) DEFAULT NULL COMMENT '显示类型（1文本框、2文本域、3下拉框、4复选框、5单选框、6日期控件、7数字类型）',
  `option_json` varchar(255) DEFAULT NULL COMMENT '选项值',
  `show_type` int(1) DEFAULT NULL COMMENT '展示类型 1仅用户端展示 2仅管理端展示 3都展示',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=350 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='业务字段表（合同模板关联额外的字段暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sc_table_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sc_table_data` (
  `data_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据id',
  `class_id` int(11) DEFAULT NULL COMMENT '分类id',
  `user_id` int(11) DEFAULT NULL COMMENT '创建人',
  `status` int(1) DEFAULT NULL COMMENT '1待审核 2审核通过 3审核未通过 4已封禁',
  `refuse_reason` text COMMENT '拒绝原因',
  `title_img` text COMMENT '首页图',
  `data_title` varchar(255) DEFAULT NULL COMMENT '标题',
  `phone` varchar(30) DEFAULT NULL COMMENT '联系方式',
  `rotation_img` text COMMENT '轮播图',
  `label` varchar(100) DEFAULT NULL COMMENT '标签',
  `browse_count` int(10) DEFAULT NULL COMMENT '浏览次数',
  `data_json` text COMMENT 'json数据',
  `is_recommend` int(1) DEFAULT NULL COMMENT '是否是推荐(1是 0不是)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `province` varchar(255) DEFAULT NULL COMMENT '省',
  `city` varchar(255) DEFAULT NULL COMMENT '市',
  `county` varchar(255) DEFAULT NULL COMMENT '县/区',
  `price_info` varchar(100) DEFAULT NULL COMMENT '价格信息,这里不是仅填写货币,而是包括货币及单位',
  `details` text COMMENT '详细信息',
  `lng` double(10,6) DEFAULT NULL COMMENT '经度',
  `lat` double(10,6) DEFAULT NULL COMMENT '纬度',
  `state` int(1) DEFAULT '1' COMMENT '1正常 2vip过期超出次数的服务已下架',
  `is_enable` int(1) DEFAULT '1' COMMENT '是否上架 0否 1是',
  PRIMARY KEY (`data_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='发布服务表（暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `search`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `search` (
  `search_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '搜索id',
  `search_name` varchar(255) DEFAULT NULL COMMENT '搜索名称',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `type` int(1) DEFAULT NULL COMMENT '1服务 2店铺',
  PRIMARY KEY (`search_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='搜索表（暂未使用）';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_captcha`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统验证码';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `param_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `param_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态   0：隐藏   1：显示',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `param_key` (`param_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统配置信息表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `name` varchar(100) NOT NULL COMMENT '字典名称',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `code` varchar(100) NOT NULL COMMENT '字典码',
  `value` varchar(1000) NOT NULL COMMENT '字典值',
  `order_num` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `type` (`type`,`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='数据字典表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `time` bigint(20) NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=456 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统日志';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=187 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='菜单管理';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_oss`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_oss` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文件上传id',
  `url` varchar(200) DEFAULT NULL COMMENT 'URL地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='文件上传';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色菜单关系id',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5923 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色与菜单对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '管理端用户id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '盐',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(4) DEFAULT NULL COMMENT '状态  0：禁用   1：正常',
  `create_user_id` bigint(20) DEFAULT NULL COMMENT '创建者ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `inviter_code` varchar(255) DEFAULT NULL COMMENT '邀请码',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户角色关系id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户与角色对应关系';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `sys_user_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_token` (
  `user_id` bigint(20) NOT NULL COMMENT '管理端用户id',
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE KEY `token` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='系统用户Token';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(500) DEFAULT NULL COMMENT '用户名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `sex` int(11) DEFAULT NULL COMMENT '性别 1男 2女',
  `age` int(4) DEFAULT NULL COMMENT '年龄',
  `open_id` varchar(500) DEFAULT NULL COMMENT '微信小程序openId',
  `wx_open_id` varchar(500) DEFAULT NULL COMMENT '微信公众号openId',
  `account_open_id` varchar(500) DEFAULT NULL COMMENT '微信App  openId',
  `balance` decimal(10,2) unsigned DEFAULT NULL COMMENT '收益',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `salt` varchar(20) DEFAULT NULL COMMENT '加密盐',
  `apple_id` varchar(64) DEFAULT NULL COMMENT '苹果id',
  `sys_phone` int(11) DEFAULT NULL COMMENT '手机类型 1安卓 2ios',
  `status` int(11) DEFAULT '1' COMMENT '状态 1正常 2禁用',
  `platform` varchar(255) DEFAULT NULL COMMENT '来源 APP 小程序  公众号',
  `clientid` varchar(255) DEFAULT NULL COMMENT 'app推送id',
  `zhi_fu_bao_name` varchar(255) DEFAULT NULL COMMENT '支付宝名称',
  `zhi_fu_bao` varchar(255) DEFAULT NULL COMMENT '支付宝账号',
  `is_send_msg` int(11) DEFAULT NULL COMMENT '是否推送消息',
  `wx_img` varchar(600) DEFAULT NULL COMMENT '微信提现图片',
  `msg` text COMMENT '最近一条信息内容',
  `user_type` int(1) DEFAULT '0' COMMENT '用户类型 0未认证 1普通用户 2企业用户',
  `is_vip` int(1) DEFAULT '0' COMMENT '是否是会员(0不是 1是)',
  `vip_end_time` datetime DEFAULT NULL COMMENT 'vip到期时间',
  `refresh_count` int(11) DEFAULT '0' COMMENT '剩余刷新次数',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `update_time` date DEFAULT NULL COMMENT '更新时间',
  `invitation_code` varchar(30) DEFAULT NULL COMMENT '邀请码',
  `inviter_code` varchar(30) DEFAULT NULL COMMENT '邀请人的邀请码',
  `inviter_inviter_code` varchar(30) DEFAULT NULL COMMENT '邀请人的上级',
  `browse_count` int(11) DEFAULT '0' COMMENT '浏览次数',
  `label` varchar(255) DEFAULT NULL COMMENT '标签',
  `zhi_rate` decimal(10,2) DEFAULT NULL COMMENT '直属佣金',
  `fei_rate` decimal(10,2) DEFAULT NULL COMMENT '非直属佣金',
  `total_login_days` int(11) DEFAULT '0' COMMENT '累计登录天数',
  `login_date` date DEFAULT NULL COMMENT '登录日期',
  `last_login_time` date DEFAULT NULL COMMENT '最后登录时间',
  `free_times` int(1) DEFAULT '2' COMMENT '免费发布合同次数：初始值是2次',
  `is_enterprise_authentication` int(1) DEFAULT NULL COMMENT '企业认证状态 1已认证 2未认证',
  `is_authentication` int(1) DEFAULT '0' COMMENT '是否实名认证 1是 0否',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=208 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `user_certification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_certification` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户实名认证id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `user_type` int(1) DEFAULT NULL COMMENT '认证类型  1普通用户 2企业用户',
  `name` varchar(50) DEFAULT NULL COMMENT '真实姓名|法人姓名',
  `id_number` varchar(255) DEFAULT NULL COMMENT '证件号码|法人身份证',
  `enterprise_name` varchar(100) DEFAULT NULL COMMENT '企业名称',
  `enterprise_phone` varchar(30) DEFAULT NULL COMMENT '企业联系电话',
  `business_license` text COMMENT '营业执照',
  `business_license_img` text COMMENT '营业执照图片',
  `status` int(1) DEFAULT NULL COMMENT '0待审核 1已通过 2未通过',
  `remark` varchar(255) DEFAULT NULL COMMENT '审核原因',
  `front` varchar(255) DEFAULT NULL COMMENT '身份证正面图片',
  `back` varchar(255) DEFAULT NULL COMMENT '身份证反面图片',
  `real_phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `enterprise_address` varchar(255) DEFAULT NULL COMMENT '个人详细地址|企业地址',
  `is_submit` int(1) DEFAULT '0' COMMENT '是否允许提交0是 1否',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='认证表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `user_money`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_money` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '钱包id',
  `money` decimal(10,2) DEFAULT NULL COMMENT '钱包金额',
  `user_id` bigint(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=517 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户钱包表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `user_money_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_money_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '钱包详情id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `by_user_id` int(11) DEFAULT NULL COMMENT '邀请用户id',
  `title` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '标题',
  `classify` int(11) DEFAULT NULL COMMENT '1注册   2购买 3提现 5购买签署合同套餐 6佣金分成',
  `type` int(11) DEFAULT NULL COMMENT '类型 1增加 2减少',
  `state` int(11) DEFAULT '1' COMMENT '状态 1待支付 2已到账 3取消',
  `money` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `content` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '内容',
  `create_time` varchar(64) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建时间',
  `user_level` int(1) DEFAULT NULL COMMENT '1一级 2二级',
  `relation_id` int(11) DEFAULT NULL COMMENT '关联id',
  `language_type` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '语种',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1526 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='用户钱包明细表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `user_vip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_vip` (
  `vip_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员id',
  `vip_name_type` int(2) DEFAULT NULL COMMENT '会员类型0月1季2年',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `create_time` varchar(255) DEFAULT NULL COMMENT '购买时间',
  `end_time` varchar(255) DEFAULT NULL COMMENT '到期时间',
  `is_vip` int(255) DEFAULT NULL COMMENT '1是会员2不是',
  PRIMARY KEY (`vip_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户会员表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `user_visitor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_visitor` (
  `id` bigint(32) NOT NULL AUTO_INCREMENT COMMENT '访客id',
  `user_id` bigint(32) DEFAULT NULL COMMENT '用户id',
  `by_user_id` bigint(32) DEFAULT NULL COMMENT '访问用户id',
  `update_time` varchar(64) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户访客表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `vip_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vip_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `vip_month_count` int(2) DEFAULT NULL COMMENT '月数',
  `money` decimal(10,2) DEFAULT NULL COMMENT '会员价格',
  `vip_name` varchar(255) DEFAULT NULL COMMENT '会员名称',
  `discount_money` decimal(10,2) DEFAULT NULL COMMENT '折扣价',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_enable` int(1) DEFAULT '0' COMMENT '是否启用(1启用 0未启用)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='会员明细表';
/*!40101 SET character_set_client = @saved_cs_client */;
DROP TABLE IF EXISTS `vip_discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vip_discount` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `discount` decimal(10,0) DEFAULT NULL COMMENT '优惠金币',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='暂未使用';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;


-- ----------------------------
-- Seed data
-- ----------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` (`menu_id`, `parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (2,126,'管理员列表','sys/user',NULL,1,'admin',10),(3,126,'角色管理','sys/role',NULL,1,'role',11),(4,126,'菜单管理','sys/menu',NULL,1,'menu',12),(15,2,'查看',NULL,'sys:user:list,sys:user:info',2,NULL,0),(16,2,'新增',NULL,'sys:user:save,sys:role:select',2,NULL,0),(17,2,'修改',NULL,'sys:user:update,sys:role:select',2,NULL,0),(18,2,'删除',NULL,'sys:user:delete',2,NULL,0),(19,3,'查看',NULL,'sys:role:list,sys:role:info',2,NULL,0),(20,3,'新增',NULL,'sys:role:save,sys:menu:list',2,NULL,0),(21,3,'修改',NULL,'sys:role:update,sys:menu:list',2,NULL,0),(22,3,'删除',NULL,'sys:role:delete',2,NULL,0),(23,4,'查看',NULL,'sys:menu:list,sys:menu:info',2,NULL,0),(24,4,'新增',NULL,'sys:menu:save,sys:menu:select',2,NULL,0),(25,4,'修改',NULL,'sys:menu:update,sys:menu:select',2,NULL,0),(26,4,'删除',NULL,'sys:menu:delete',2,NULL,0),(32,0,'用户中心','userList','',1,'yonghul',1),(33,0,'数据中心','home','',1,'shuju',0),(34,0,'财务中心','financeList','',1,'caiwu',1),(35,34,'查看','','financeList:list',2,'',0),(36,34,'转账','','financeList:transfer',2,'',0),(37,34,'退款','','financeList:refund',2,'',0),(38,34,'退款','','financeList:add',2,'',0),(39,34,'修改','','financeList:update',2,'',0),(40,34,'删除','','financeList:delete',2,'',0),(41,0,'消息中心','message','',1,'xiaoxi',1),(42,41,'查看','','message:list',2,'',0),(43,41,'消息推送','','message:push',2,'',0),(50,0,'首页装修','bannerList','',1,'shangpin',2),(51,50,'查看','','bannerList:list',2,'',0),(52,50,'添加','','bannerList:add',2,'',0),(53,50,'修改','','bannerList:update',2,'',0),(54,50,'删除','','bannerList:delete',2,'',0),(57,0,'系统配置','allocationList','',1,'menu',9),(58,57,'查看','','allocationList:list',2,'',0),(59,57,'修改','','allocationList:update',2,'',0),(60,32,'查看','','userList:list',2,'',0),(61,32,'删除','','userList:delete',2,'',0),(69,41,'添加','','message:add',2,'',0),(70,41,'修改','','message:update',2,'',0),(71,41,'删除','','message:delete',2,'',0),(72,0,'模板管理','locality','',1,'order',5),(73,72,'添加','','locality:add',2,'',0),(74,72,'查看','','locality:list',2,'',0),(75,72,'修改','','locality:update',2,'',0),(76,72,'删除','','locality:delete',2,'',0),(77,0,'合同套餐管理','autonym','',1,'renwu',6),(78,77,'列表','','autonym:list',2,'',0),(90,32,'修改积分','','userList:updatejf',2,'',0),(91,32,'修改用户状态','','userList:updateStatus',2,'',0),(92,77,'通过','','autonym:tongguo',2,'',0),(93,77,'拒绝','','autonym:jujue',2,'',0),(105,32,'修改积分','','userList:updatebl',2,'',0),(111,0,'印章管理','dynamic','',1,'leibie',7),(112,111,'查看','','dynamic:list',2,'',0),(113,111,'添加','','dynamic:add',2,'',0),(114,111,'修改','','dynamic:update',2,'',0),(115,111,'删除','','dynamic:delete',2,'',0),(116,0,'充值配置','coupon','',1,'caiwu',7),(117,116,'查看','','coupon:list',2,'',0),(118,116,'添加','','coupon:add',2,'',0),(119,116,'修改','','coupon:update',2,'',0),(120,116,'删除','','coupon:delete',2,'',0),(126,0,'系统管理','','',0,'shezhi',20),(127,111,'审核','','dynamic:shenhe',2,'',0),(128,0,'会员管理','','',0,'jifen',7),(144,0,'帮助中心','materialsList','',1,'renwu',8),(145,144,'查看','','materialsList:list',2,'',0),(146,144,'添加','','materialsList:add',2,'',0),(147,144,'修改','','materialsList:update',2,'',0),(148,144,'删除','','materialsList:delete',2,'',0),(149,77,'删除','','autonym:delete',2,'',0),(150,0,'个人审核','authentication','',1,'yonghul',8),(151,150,'通过','','authentication:pass',2,'',0),(152,150,'拒绝','','authentication:refuse',2,'',0),(154,0,'企业审核','shopManage','',1,'shuju',3),(155,154,'修改店铺信息','','shopManage:updata',2,'',0),(157,154,'封禁店铺','','shopManage:ban',2,'shuju',3),(158,154,'店铺审核','','shopManage:examine',2,'',0),(159,150,'修改','','authentication:update',2,'',0),(160,150,'删除','','authentication:delete',2,'',0),(161,32,'设置会员','','userList:updateVip',2,'',0),(162,0,'注销信息','messageZx',NULL,1,'order',1),(163,162,'查看',NULL,'messageZx:list',2,NULL,0),(164,162,'注销',NULL,'messageZx:shenhe',2,NULL,0),(165,77,'添加',NULL,'autonym:add',2,NULL,0),(166,77,'修改',NULL,'autonym:update',2,NULL,0),(167,77,'封禁',NULL,'autonym:fengjin',2,NULL,0),(168,0,'订单中心','exchangeList',NULL,1,'order',4),(169,168,'查看',NULL,'exchangeList:list',2,NULL,0),(170,168,'退款',NULL,'exchangeList:tuikuan',2,NULL,0),(171,0,'聊天记录','chatRecord',NULL,1,'pinglun',6),(172,171,'查看',NULL,'chatRecord:list',2,NULL,0),(173,34,'导出',NULL,'financeList:daochu',2,NULL,0),(174,128,'会员特权','vipPrivilege',NULL,1,'shoucangfill',0),(175,174,'查看',NULL,'vipPrivilege:list',2,NULL,0),(176,174,'添加',NULL,'vipPrivilege:add',2,NULL,0),(177,174,'修改',NULL,'vipPrivilege:update',2,NULL,0),(178,174,'删除',NULL,'vipPrivilege:delete',2,NULL,0),(179,126,'升级配置','app',NULL,1,'shezhi',0),(180,179,'查看',NULL,'app:list',2,NULL,0),(181,179,'添加',NULL,'app:add',2,NULL,0),(182,179,'修改',NULL,'app:update',2,NULL,0),(183,179,'删除',NULL,'app:delete',2,NULL,0),(184,33,'查看','','home:list',2,'',0),(185,0,'合同管理','collectionList','',1,'order',6),(186,185,'查看','','collectionList:list',2,'',0);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` (`role_id`, `role_name`, `remark`, `create_user_id`, `create_time`) VALUES (1,'超级管理员','super',2,'2021-06-04 17:38:28'),(4,'普通用户','普通用户',2,'2021-09-10 15:33:47');
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `sys_role_menu` WRITE;
/*!40000 ALTER TABLE `sys_role_menu` DISABLE KEYS */;
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`) VALUES (5295,4,33),(5296,4,60),(5297,4,90),(5298,4,91),(5299,4,105),(5300,4,161),(5301,4,35),(5302,4,36),(5303,4,37),(5304,4,38),(5305,4,39),(5306,4,173),(5307,4,42),(5308,4,162),(5309,4,163),(5310,4,164),(5311,4,51),(5312,4,154),(5313,4,155),(5314,4,158),(5315,4,157),(5316,4,168),(5317,4,169),(5318,4,170),(5319,4,73),(5320,4,74),(5321,4,75),(5322,4,77),(5323,4,78),(5324,4,92),(5325,4,93),(5326,4,149),(5327,4,165),(5328,4,166),(5329,4,167),(5330,4,171),(5331,4,172),(5332,4,112),(5333,4,117),(5334,4,118),(5335,4,119),(5336,4,144),(5337,4,145),(5338,4,146),(5339,4,147),(5340,4,148),(5341,4,150),(5342,4,151),(5343,4,152),(5344,4,159),(5345,4,160),(5346,4,58),(5348,4,15),(5349,4,19),(5350,4,23),(5351,4,-666666),(5352,4,32),(5353,4,34),(5354,4,41),(5355,4,50),(5356,4,72),(5357,4,111),(5358,4,128),(5359,4,116),(5360,4,57),(5361,4,126),(5363,4,2),(5364,4,3),(5365,4,4),(5837,1,33),(5838,1,184),(5839,1,32),(5840,1,60),(5841,1,61),(5842,1,90),(5843,1,91),(5844,1,105),(5845,1,161),(5846,1,34),(5847,1,35),(5848,1,36),(5849,1,37),(5850,1,38),(5851,1,39),(5852,1,40),(5853,1,173),(5854,1,41),(5855,1,42),(5856,1,43),(5857,1,69),(5858,1,70),(5859,1,71),(5860,1,162),(5861,1,163),(5862,1,164),(5863,1,50),(5864,1,51),(5865,1,52),(5866,1,53),(5867,1,54),(5868,1,154),(5869,1,155),(5870,1,158),(5871,1,157),(5872,1,72),(5873,1,73),(5874,1,74),(5875,1,75),(5876,1,76),(5877,1,77),(5878,1,78),(5879,1,92),(5880,1,93),(5881,1,149),(5882,1,165),(5883,1,166),(5884,1,167),(5885,1,185),(5886,1,186),(5887,1,111),(5888,1,112),(5889,1,113),(5890,1,114),(5891,1,115),(5892,1,127),(5893,1,144),(5894,1,145),(5895,1,146),(5896,1,147),(5897,1,148),(5898,1,150),(5899,1,151),(5900,1,152),(5901,1,159),(5902,1,160),(5903,1,57),(5904,1,58),(5905,1,59),(5906,1,2),(5907,1,15),(5908,1,16),(5909,1,17),(5910,1,18),(5911,1,3),(5912,1,19),(5913,1,20),(5914,1,21),(5915,1,22),(5916,1,4),(5917,1,23),(5918,1,24),(5919,1,25),(5920,1,26),(5921,1,-666666),(5922,1,126);
/*!40000 ALTER TABLE `sys_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `common_info` WRITE;
/*!40000 ALTER TABLE `common_info` DISABLE KEYS */;
INSERT INTO `common_info` (`id`, `create_at`, `max`, `min`, `type`, `value`, `condition_from`) VALUES (1, '2023-07-18 09:40:54', NULL, '客服二维码', 1, '', 'xitong'), (2, '2020-02-21 21:17:17', NULL, '公众号二维码', 2, '', 'xitong'), (6, '2020-07-09 17:59:45', NULL, '微信公众号APPID	', 5, '', 'weixin'), (7, '2021-03-15 21:16:30', NULL, '短信签名', 6, '电子合同', 'duanxin'), (16, '2020-02-25 20:43:38', NULL, '微信公众号秘钥	', 21, '', 'weixin'), (17, '2020-02-25 20:43:59', NULL, '公众号Token', 16, '', 'weixin'), (18, '2020-02-25 20:44:15', NULL, '公众号EncodingAESKey	', 17, '', 'weixin'), (20, '2023-07-17 15:26:13', NULL, '后台管理平台域名配置	', 20, '', 'xitong'), (21, '2023-07-07 13:32:18', NULL, 'h5服务域名配置	', 19, '', 'xitong'), (22, '2023-11-08 19:29:20', NULL, '短信服务商（1 腾讯云  2阿里云  3短信宝）', 79, '2', 'duanxin'), (23, '2022-03-20 15:08:22', NULL, '后台服务名称	', 12, '电子合同管理系统', 'xitong'), (27, '2023-07-18 11:04:22', NULL, '通用APP下载地址	', 25, '', 'xitong'), (33, '2023-11-08 19:29:38', NULL, '腾讯云短信clientId	', 31, '', 'duanxin'), (34, '2023-11-08 19:29:43', NULL, '腾讯云短信clientSecret	', 32, '', 'duanxin'), (44, '2022-03-10 20:38:21', NULL, '客服微信号', 44, '', 'xitong'), (47, '2022-01-22 20:52:42', NULL, '微信小程序APPID', 45, '', 'weixin'), (48, '2022-01-22 20:52:48', NULL, '微信小程序秘钥', 46, '', 'weixin'), (51, '2020-03-29 00:21', NULL, '分享安卓下载地址', 49, '', 'xitong'), (52, '2020-03-29 00:21', NULL, '分享苹果下载地址', 50, '', 'xitong'), (58, '2020-03-29 00:21', NULL, '开启微信登录', 53, '否', 'kaiguans'), (69, '2020-06-04 16:34', NULL, 'APP消息推送PushAppKey', 60, '', 'push'), (70, '2020-06-04 16:34', NULL, 'APP消息推送PushAppId', 61, '', 'push'), (71, '2020-06-04 16:34', NULL, 'APP消息推送PushMasterSecret', 62, '', 'push'), (72, '2020-06-04 16:34', NULL, '企业支付宝APPID', 63, '', 'zhifubao'), (73, '2020-06-04 16:34', NULL, '企业支付宝公钥', 64, '', 'zhifubaos'), (74, '2020-06-04 16:34', NULL, '企业支付宝商户秘钥', 65, '', 'zhifubao'), (77, '2021-03-15 21:22:30', NULL, '文件上传阿里云Endpoint', 68, '', 'oss'), (78, '2021-03-15 21:22:38', NULL, '文件上传阿里云账号accessKeyId', 69, '', 'oss'), (79, '2021-03-15 21:22:46', NULL, '文件上传阿里云账号accessKeySecret', 70, '', 'oss'), (80, '2021-03-15 21:22:53', NULL, '文件上传阿里云Bucket名称', 71, '', 'oss'), (81, '2021-03-15 21:23:00', NULL, '文件上传阿里云Bucket域名', 72, '', 'oss'), (83, '2020-07-27 15:17', NULL, '微信APPappId', 74, '', 'weixin'), (84, '2020-07-27 15:17', NULL, '微信商户key', 75, '', 'weixins'), (85, '2020-07-27 15:17', NULL, '微信商户号mchId', 76, '', 'weixin'), (86, '2026-03-13 09:53:31', NULL, '阿里云您有一份抄送合同，请及时上线查看！模板code（开启阿里云短信必须配置）', 86, '', 'duanxin'), (88, '2024-06-18 14:27:31', NULL, '官方邀请码', 88, '8888888', 'xitong'), (95, '2026-03-13 09:53:31', NULL, '阿里云您有一份需要签署的合同，请及时上线查看并签署！模板code（开启阿里云短信必须配置）', 85, '', 'duanxin'), (96, '2020-07-27 15:17', NULL, '阿里云登陆或注册模板code（开启阿里云短信必须配置）', 80, '', 'duanxin'), (97, '2020-07-27 15:17', NULL, '阿里云找回密码模板code（开启阿里云短信必须配置）', 81, '', 'duanxin'), (98, '2020-07-27 15:17', NULL, '阿里云绑定手机号模板code（开启阿里云短信必须配置）', 82, '', 'duanxin'), (99, '2020-07-27 15:17', NULL, '阿里云短信accessKeyId', 83, '', 'duanxin'), (100, '2020-07-27 15:17', NULL, '阿里云短信accessSecret', 84, '', 'duanxin'), (101, '2022-04-21 19:40:07', NULL, '支付宝支付方式 1秘钥 2证书', 97, '1', 'zhifubao'), (102, '2022-04-21 19:40:07', NULL, '转账方式 1支付宝证书  2支付宝秘钥 3手动', 98, '3', 'zhifubao'), (105, '2024-06-18 14:27:31', NULL, '是否微信开启小程序跳转开关', 105, '否', 'kaiguan'), (108, '2023-07-14 16:40:21', NULL, '公众号是否自动登录', 108, '否', 'kaiguans'), (110, '2023-03-29 22:36:19', NULL, 'socket域名', 110, '', 'xitong'), (118, '2020-07-27 15:17', NULL, '邀请推广内容', 101, '欢迎使用电子合同系统', 'xitongs'), (120, '2023-07-18 11:14:42', NULL, '邀请微信分享内容', 103, '邀请好友一起使用电子合同', 'weixin'), (130, '2020-11-04 10:31:40', NULL, '提现最低额度', 112, '10', 'fuwufei'), (134, '2023-07-18 11:04:46', NULL, '海报邀请语', 116, '邀请好友注册', 'xitong'), (154, '2023-07-18 11:15:11', NULL, '是否开启APP微信分享', 136, '否', 'kaiguan'), (159, '2023-07-18 11:15:15', NULL, 'H5推广是否分享APP下载页面', 141, '否', 'kaiguan'), (170, '2022-03-17 18:03:01', NULL, '提现手续费（用户提现时候的手续费，可以作为平台服务费使用，设置0.3就是百分之30，举例提现100服务费30，到账70）', 152, '0', 'fuwufei'), (171, '2021-09-06 16:19:51', NULL, '最高提现金额', 153, '1000', 'fuwufei'), (181, '2023-06-02 11:22:12', NULL, '扣除背景图key', 183, '', 'xitong'), (182, '2022-03-20 15:06:28', NULL, '短信宝用户名', 164, '', 'duanxin'), (183, '2022-03-20 15:06:40', NULL, '短信宝密码', 165, '', 'duanxin'), (184, '2020-11-04 10:31:40', NULL, '支付宝证书路径', 200, '', 'zhifubao'), (200, '2021-07-19 11:32:12', NULL, '帮助中心', 175, '<p>帮助中心内容请在管理端配置。</p>', 'xieyi'), (201, '2022-04-18 14:05:03', NULL, '隐私政策', 176, '<p>请在管理端配置隐私政策。</p>', 'xieyi'), (202, '2021-08-14 19:09:54', NULL, '用户协议', 177, '<p>请在管理端配置用户协议。</p>', 'xieyi'), (203, '2021-08-14 19:09:54', NULL, '关于我们', 187, '关于我们', 'xieyi'), (204, '2023-07-18 09:36:04', NULL, '登录是否获取手机号', 188, '否', 'kaiguan'), (217, '2020-07-27 15:17', NULL, '腾讯地图key', 217, '', 'xitong'), (234, '2020-11-04 10:31:40', NULL, '上传方式 1阿里云oss  2本地 3腾讯云', 234, '2', 'oss'), (236, '2020-11-04 10:31:40', NULL, '退款微信通知模板', 236, '', 'weixin'), (240, '2020-11-04 10:31:40', NULL, '微信消息、实名认证通知模板', 240, '', 'weixin'), (241, '2020-11-04 10:31:40', NULL, '超时几分钟短信通知', 241, '5', 'xitong'), (242, '2020-11-04 10:31:40', NULL, '几条未读短信通知', 242, '1', 'xitong'), (243, '2020-11-04 10:31:40', NULL, '是否开启微信提现', 243, '否', 'kaiguan'), (244, '2021-12-22 10:11:36', NULL, '微信提现方式 1自动 2手动', 244, '2', 'xitong'), (245, '2020-11-04 10:31:40', NULL, '是否开启支付宝提现', 245, '否', 'kaiguan'), (246, '2020-10-28 16:57:07', NULL, '微信证书地址', 201, '', 'weixin'), (257, '2020-11-04 10:31:40', NULL, '身份验证SecretID', 257, '', 'xitong'), (258, '2020-11-04 10:31:40', NULL, '身份验证SecretKey', 258, '', 'xitongs'), (259, '2020-11-04 10:31:40', NULL, '身份验证实例ID', 259, '', 'xitong'), (262, '2022-05-26 21:31:52', NULL, '小程序是否上架', 262, '是', 'kaiguan'), (276, '2023-10-20 16:46:12', NULL, '推广文案', 276, '邀请好友注册', 'xitong'), (285, '2022-04-29 19:19:05', NULL, '免费用户每天最多刷新几次', 285, '1', 'xitongs'), (286, '2023-06-21 11:35:07', NULL, 'VIP用户每天最多刷新几次', 286, '3', 'xitongs'), (289, '2023-07-13 16:29:40', NULL, '推广图片', 289, '', 'image'), (290, '2023-07-10 10:32:30', NULL, '关键词过滤', 290, '', 'xitong'), (291, '2023-07-10 10:36:08', NULL, '聊天警告文字提醒', 291, '聊天过程中请勿泄露个人敏感信息，请文明交流。', 'xitong'), (292, '2023-07-10 17:31:30', NULL, '审核状态通知', 292, '', 'weixin'), (293, '2023-07-10 17:31:30', NULL, '用户注销协议', 293, '<p>账号注销后数据将无法恢复，请谨慎操作。</p>', 'xieyi'), (294, '2023-07-11 13:32:12', NULL, 'vip到期提醒', 294, '', 'weixin'), (295, '2023-07-11 13:32:12', NULL, 'vip到期提前几天提醒', 295, '5', 'weixin'), (296, '2023-07-18 10:09:35', NULL, '客服类型(1:电话，2:企业微信，3:客服二维码)', 296, '1', 'xitong'), (297, '2023-07-11 16:07:32', NULL, '企业微信客服APPID', 297, '', 'xitong'), (298, '2023-07-11 16:04:28', NULL, '客服电话', 298, '', 'xitong'), (300, '2023-07-11 16:07:27', NULL, '企业客户链接', 300, '', 'xitong'), (301, '2023-10-20 17:31:46', NULL, '小程序上线开关1', 301, '是', 'kaiguan'), (302, '2023-10-20 16:10:15', NULL, '小程序上线开关2', 302, '是', 'kaiguan'), (303, '2023-07-12 09:42:00', NULL, '是否开启分佣提成', 303, '否', 'kaiguan'), (304, '2023-07-12 09:42:00', NULL, '一级佣金', 304, '0', 'fuwufei'), (305, '2023-07-14 15:09:03', NULL, '二级佣金', 305, '0', 'fuwufei'), (306, '2023-07-13 17:50:00', NULL, '新人赠送vip天数(0表示不赠送)', 306, '0', 'xitong'), (307, '2023-07-14 17:27:22', NULL, '免费用户最多发布多少店铺', 307, '1', 'xitong'), (308, '2023-07-12 13:47:09', NULL, '免费用户最多发布多少信息', 308, '2', 'xitong'), (309, '2023-07-17 15:23:54', NULL, '用户小程序名称配置', 309, '电子合同', 'xitong'), (351, '2024-06-18 14:27:31', NULL, '支付宝转账场景', 351, '', 'zhifubao'), (414, '2023-07-26 16:27:15', NULL, '地图方式 1腾讯 2天地图', 414, '2', 'xitong'), (415, '2023-07-26 16:27:15', NULL, '天地图key', 415, '', 'xitong'), (422, '2024-06-18 14:27:31', NULL, '百度翻译app_id', 422, '', 'xitong'), (423, '2024-06-18 14:27:31', NULL, '百度翻译app_secret', 423, '', 'xitong'), (424, '2024-06-18 14:27:31', NULL, '新用户免费签署次数', 424, '2', 'xitong'), (425, '2024-06-18 14:27:31', NULL, '实名认证审核方式(1人工审核 2自动审核)', 425, '1', 'xitong'), (426, '2024-06-18 14:27:31', NULL, '实名认证微信消息提醒模板', 426, '', 'xitong'), (427, '2024-06-18 14:27:31', NULL, '合同编号前缀', 427, 'NO.', 'xitong'), (428, '2024-06-18 14:27:31', NULL, 'libreOffice工具，1线上 2本地', 428, '2', 'xitong'), (429, '2024-06-18 14:27:31', NULL, '合同默认过期时间（天）', 429, '7', 'xitong'), (430, '2024-06-18 14:27:31', NULL, '系统默认头像', 430, '', 'xitong'), (431, '2024-06-18 14:27:31', NULL, '企业微信客服链接', 431, '', 'xitong'), (432, '2024-06-18 14:27:31', NULL, '充值协议', 432, '', 'xieyi'), (433, '2024-06-18 14:27:31', NULL, '企业认证是否自动通过（1人工  2自动）', 433, '1', 'xitong'), (434, '2024-06-18 14:27:31', NULL, '阿里查询企业信息的AppCode', 434, '', 'xitong'), (435, '2024-06-18 14:27:31', NULL, '腾讯云查询企业信息云市场分配的密钥Id', 469, '', 'xitong'), (436, '2024-06-18 14:27:31', '', '腾讯云查询企业信息云市场分配的密钥Key', 470, '', 'xitong'), (800, '2022-11-01 11:18:00', NULL, '腾讯云oss AccessKey', 800, '', 'oss'), (801, '2022-11-01 11:18:00', NULL, '腾讯云oss SecretKey', 801, '', 'oss'), (802, '2022-11-01 11:18:00', NULL, '腾讯云oss bucket地域', 802, '', 'oss'), (803, '2022-11-01 11:18:00', NULL, '腾讯云oss bucketName', 803, '', 'oss'), (804, '2022-11-01 11:18:00', NULL, '腾讯云oss Bucket域名', 804, '', 'oss');
/*!40000 ALTER TABLE `common_info` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `sys_config` WRITE;
/*!40000 ALTER TABLE `sys_config` DISABLE KEYS */;
INSERT INTO `sys_config` (`id`, `param_key`, `param_value`, `remark`) VALUES (1, 'CLOUD_STORAGE_CONFIG_KEY', '{"type":2,"aliyunAccessKeyId":"","aliyunAccessKeySecret":"","aliyunBucketName":"","aliyunDomain":"","aliyunEndPoint":"","aliyunPrefix":"","qcloudAppId":null,"qcloudBucketName":"","qcloudDomain":"","qcloudPrefix":"","qcloudRegion":"","qcloudSecretId":"","qcloudSecretKey":"","qiniuAccessKey":"","qiniuBucketName":"","qiniuDomain":"","qiniuPrefix":"","qiniuSecretKey":""}', '云存储配置');
/*!40000 ALTER TABLE `sys_config` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `contract_type` WRITE;
/*!40000 ALTER TABLE `contract_type` DISABLE KEYS */;
INSERT INTO `contract_type` (`id`, `type_name`, `sort`, `create_user_id`, `create_time`, `update_user_id`, `update_time`) VALUES (1,'租赁合同',1,6,'2024-08-20 17:45:11',14,'2026-03-10 09:50:44'),(2,'劳务合同',2,2,'2026-04-10 11:47:00',NULL,NULL);
/*!40000 ALTER TABLE `contract_type` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `pay_classify` WRITE;
/*!40000 ALTER TABLE `pay_classify` DISABLE KEYS */;
INSERT INTO `pay_classify` (`pay_classify_id`, `price`, `executed`, `number_sign`, `sort`, `create_time`) VALUES (1,0.01,'合同签署套餐',1,1,'2026-01-29 15:39:35'),(2,10.00,'合同签署套餐',20,2,'2026-01-29 15:40:02'),(3,50.00,'合同签署套餐',80,3,'2026-01-29 15:41:16'),(4,100.00,'合同签署套餐',120,4,'2026-01-29 15:42:11'),(5,120.00,'合同签署套餐',150,5,'2026-01-29 15:43:12'),(6,160.00,'合同签署套餐',230,6,'2026-01-29 16:16:35');
/*!40000 ALTER TABLE `pay_classify` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `help_classify` WRITE;
/*!40000 ALTER TABLE `help_classify` DISABLE KEYS */;
INSERT INTO `help_classify` (`help_classify_id`, `help_classify_name`, `sort`, `parent_id`, `create_time`) VALUES (2,'小程序',1,0,'2020-12-12 12:12:12'),(3,'活动报名',2,0,'2020-12-12 12:12:12');
/*!40000 ALTER TABLE `help_classify` ENABLE KEYS */;
UNLOCK TABLES;

LOCK TABLES `help_word` WRITE;
/*!40000 ALTER TABLE `help_word` DISABLE KEYS */;
-- help_word 业务文案已省略，请在管理端自行维护
/*!40000 ALTER TABLE `help_word` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- ----------------------------
-- 演示管理员（账号 admin / 密码 admin123）
-- ----------------------------
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `create_time`, `inviter_code`)
VALUES (1, 'admin', '07a1cceac36a40316bea5015737f9714c4eb58420c029d18d5c33a28196093fe', 'opensourceDemoSalt1', 'admin@example.com', '13800000000', 1, 1, '2026-11-09 17:10:33', NULL);

INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`) VALUES (1, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
