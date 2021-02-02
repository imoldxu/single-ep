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

 Date: 02/02/2021 21:49:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

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
) ENGINE = MyISAM AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

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
) ENGINE = MyISAM AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_drug
-- ----------------------------
DROP TABLE IF EXISTS `t_drug`;
CREATE TABLE `t_drug`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '药品编号',
  `drugname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `shortname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
  `standard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
  `category` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类，OTC、处方药、营养膳食',
  `subcategory` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '药品子类',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `unit` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `form` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '剂型',
  `singledose` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺省单次剂量',
  `doseunit` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺省剂量单位',
  `defaultusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺省用法',
  `frequency` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺省用法',
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '厂商信息',
  `fullkeys` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音缩写',
  `shortnamekeys` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称缩写',
  `state` int(11) NULL DEFAULT 1 COMMENT '状态，有货1，无货为0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`drugname`) USING BTREE,
  INDEX `index_fullkeys`(`fullkeys`) USING BTREE,
  INDEX `index_shortkeys`(`shortnamekeys`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_prescription
-- ----------------------------
DROP TABLE IF EXISTS `t_prescription`;
CREATE TABLE `t_prescription`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '诊断号',
  `type` int(3) NULL DEFAULT NULL COMMENT '类型，西药为1，中药为2',
  `doctorname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医生姓名',
  `department` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医生科室',
  `patientname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '患者姓名',
  `patientage` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '患者年龄',
  `patientsex` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '患者性别',
  `patientphone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '患者电话',
  `diagnosis` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '诊断',
  `createdate` date NULL DEFAULT NULL COMMENT '处方开具日期',
  `state` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态  \'生成处方\',\'已领药\'',
  `zyusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用法',
  `zynum` int(3) NULL DEFAULT NULL COMMENT '中药剂数',
  `zysingledose` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zyfrequence` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zymode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_prescriptiondrugs
-- ----------------------------
DROP TABLE IF EXISTS `t_prescriptiondrugs`;
CREATE TABLE `t_prescriptiondrugs`  (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '药品编号',
  `prescriptionid` bigint(11) NULL DEFAULT NULL,
  `drugname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '药品名称',
  `standard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '规格',
  `category` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类，OTC、处方药、营养膳食',
  `price` double NULL DEFAULT NULL COMMENT '单价',
  `unit` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单位',
  `number` int(11) NULL DEFAULT NULL COMMENT '数量',
  `singledose` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医嘱单次剂量',
  `myusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医嘱用法',
  `frequency` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医嘱频次',
  `drugid` int(11) NULL DEFAULT NULL,
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
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
