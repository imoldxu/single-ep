/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 50643
 Source Host           : localhost:3306
 Source Schema         : kph

 Target Server Type    : MySQL
 Target Server Version : 50643
 File Encoding         : 65001

 Date: 08/03/2021 13:12:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_bill`;
CREATE TABLE `t_bill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payway` tinyint(4) NOT NULL COMMENT '支付方式',
  `type` tinyint(4) NOT NULL COMMENT '1是支付，2是退费',
  `payid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '支付机构的id',
  `amount` int(11) NOT NULL COMMENT '金额',
  `orderno` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '关联订单no',
  `createtime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_diagnosismsg
-- ----------------------------
DROP TABLE IF EXISTS `t_diagnosismsg`;
CREATE TABLE `t_diagnosismsg`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '诊断描述',
  `diagnosis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '诊断',
  `shortkeys` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '拼音首字母缩写',
  `fullkeys` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全拼字母',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_msg`(`diagnosis`) USING BTREE,
  INDEX `index_fullkeys`(`fullkeys`) USING BTREE,
  INDEX `index_shortkeys`(`shortkeys`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_doctordrugs
-- ----------------------------
DROP TABLE IF EXISTS `t_doctordrugs`;
CREATE TABLE `t_doctordrugs`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `drugid` bigint(11) NOT NULL COMMENT '药品id',
  `doctorname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '医生',
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_drug
-- ----------------------------
DROP TABLE IF EXISTS `t_drug`;
CREATE TABLE `t_drug`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '药品编号',
  `drugno` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '药品编号',
  `drugname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `shortname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
  `standard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规格',
  `category` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类，OTC、处方药、营养膳食',
  `subcategory` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '药品子类',
  `price` int(11) UNSIGNED NOT NULL COMMENT '单价',
  `unit` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '单位',
  `form` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '剂型',
  `singledose` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '缺省单次剂量',
  `defaultusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '缺省用法',
  `frequency` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '缺省用法',
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '厂商信息',
  `fullkeys` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音缩写',
  `shortnamekeys` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称缩写',
  `state` tinyint(11) NOT NULL DEFAULT 1 COMMENT '状态，有货1，无货为0',
  `stock` int(11) NOT NULL DEFAULT 0 COMMENT '库存',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_drugno`(`drugno`) USING BTREE,
  INDEX `index_name`(`drugname`) USING BTREE,
  INDEX `index_fullkeys`(`fullkeys`) USING BTREE,
  INDEX `index_shortkeys`(`shortnamekeys`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderno` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `amount` int(11) UNSIGNED NOT NULL COMMENT '金额',
  `trade_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单信息，此信息会对应sales_record',
  `createtime` datetime(0) NOT NULL COMMENT '创建时间',
  `invalidtime` datetime(0) NOT NULL COMMENT '失效时间',
  `completetime` datetime(0) NULL DEFAULT NULL COMMENT '支付完成时间',
  `state` tinyint(4) NOT NULL COMMENT '状态',
  `reg_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '患者登记号',
  `prescriptionid` bigint(20) NOT NULL COMMENT '处方id',
  `payway` tinyint(4) NULL DEFAULT NULL COMMENT '支付方式',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_no`(`orderno`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_permission`;
CREATE TABLE `t_permission`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_prescription
-- ----------------------------
DROP TABLE IF EXISTS `t_prescription`;
CREATE TABLE `t_prescription`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `prescriptionno` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '诊断号',
  `type` int(3) NULL DEFAULT NULL COMMENT '类型，西药为1，中药为2',
  `doctorname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '医生姓名',
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '医生科室',
  `patient_reg_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登记号',
  `patient_card_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '卡号',
  `patientname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '患者姓名',
  `patientage` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '患者年龄',
  `patient_birthday` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '患者的生日',
  `patientsex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '患者性别',
  `patientphone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '患者电话',
  `diagnosis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '诊断',
  `createdate` date NOT NULL COMMENT '处方开具日期',
  `state` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态  \'生成处方\',\'已领药\'',
  `zyusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用法',
  `zynum` int(3) NULL DEFAULT NULL COMMENT '中药剂数',
  `zysingledose` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中药单次计量',
  `zyfrequence` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中药频次',
  `zymode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '中药方式',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_no`(`prescriptionno`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_prescriptiondrugs
-- ----------------------------
DROP TABLE IF EXISTS `t_prescriptiondrugs`;
CREATE TABLE `t_prescriptiondrugs`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '药品编号',
  `prescriptionid` bigint(11) NOT NULL COMMENT '关联的处方id',
  `drugname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '药品名称',
  `standard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
  `category` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类，OTC、处方药、营养膳食',
  `price` int(11) NOT NULL COMMENT '单价',
  `unit` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `number` int(11) UNSIGNED NOT NULL COMMENT '数量',
  `singledose` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '医嘱单次剂量',
  `myusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '医嘱用法',
  `frequency` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '医嘱频次',
  `drugid` int(11) NOT NULL COMMENT '关联的药品id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_prescriptionnum
-- ----------------------------
DROP TABLE IF EXISTS `t_prescriptionnum`;
CREATE TABLE `t_prescriptionnum`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `number` int(11) NULL DEFAULT NULL,
  `opendate` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`  (
  `rid` int(11) NOT NULL,
  `pid` int(11) NOT NULL,
  PRIMARY KEY (`rid`, `pid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sales_record
-- ----------------------------
DROP TABLE IF EXISTS `t_sales_record`;
CREATE TABLE `t_sales_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `orderid` bigint(20) NOT NULL COMMENT '\r\n订单号',
  `prescriptionid` bigint(20) NOT NULL COMMENT '处方id',
  `drugid` int(11) NOT NULL COMMENT '药品id',
  `drugname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '药品名称',
  `standard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '药品规格',
  `drugcompany` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '药品厂商',
  `num` int(11) UNSIGNED NOT NULL COMMENT '销售数量，负数则为退货数量',
  `price` int(11) UNSIGNED NOT NULL COMMENT '售价单价',
  `createtime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `drugunit` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '单位',
  `refundnum` int(11) UNSIGNED NOT NULL DEFAULT 0 COMMENT '退货数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `createtime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `state` tinyint(4) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `uid` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  PRIMARY KEY (`uid`, `rid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
