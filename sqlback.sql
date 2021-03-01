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

 Date: 26/02/2021 12:46:45
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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_bill
-- ----------------------------
INSERT INTO `t_bill` VALUES (1, 5, 1, '刘峰', 12824, '12312320210225115727885191697', '2021-02-25 13:43:20');
INSERT INTO `t_bill` VALUES (2, 2, 1, 'ali83013129887859', 3200, '12312420210225143009806043336', '2021-02-25 16:06:59');
INSERT INTO `t_bill` VALUES (3, 2, 2, '', 3200, '12312420210225162556567095450', '2021-02-25 16:25:57');

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
) ENGINE = MyISAM AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_diagnosismsg
-- ----------------------------
INSERT INTO `t_diagnosismsg` VALUES (1, '病毒性感冒发烧', 'BDXGMFS', 'BINGDUXINGGANMAOFASHAO');
INSERT INTO `t_diagnosismsg` VALUES (2, '手臂骨折', 'SBGZ', 'SHOUBEIGUZHE');

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
) ENGINE = MyISAM AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_doctordrugs
-- ----------------------------
INSERT INTO `t_doctordrugs` VALUES (1, 2, '赵薇', '儿科');
INSERT INTO `t_doctordrugs` VALUES (2, 1, '赵薇', '儿科');
INSERT INTO `t_doctordrugs` VALUES (3, 1, '毛科', '儿科');

-- ----------------------------
-- Table structure for t_drug
-- ----------------------------
DROP TABLE IF EXISTS `t_drug`;
CREATE TABLE `t_drug`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '药品编号',
  `drugname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `shortname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称',
  `standard` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '规格',
  `category` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分类，OTC、处方药、营养膳食',
  `subcategory` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '药品子类',
  `price` int(11) UNSIGNED NOT NULL COMMENT '单价',
  `unit` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '单位',
  `form` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '剂型',
  `singledose` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺省单次剂量',
  `defaultusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺省用法',
  `frequency` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缺省用法',
  `company` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '厂商信息',
  `fullkeys` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拼音缩写',
  `shortnamekeys` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '简称缩写',
  `state` tinyint(11) NOT NULL DEFAULT 1 COMMENT '状态，有货1，无货为0',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_name`(`drugname`) USING BTREE,
  INDEX `index_fullkeys`(`fullkeys`) USING BTREE,
  INDEX `index_shortkeys`(`shortnamekeys`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_drug
-- ----------------------------
INSERT INTO `t_drug` VALUES (1, '小儿感冒颗粒', '感冒颗粒', '每袋装6g', 'OTC', '解热镇痛', 3200, '盒', '颗粒剂', '12g', '开水冲服', '一日2次', '三九医药', 'XEGMKL', 'GMKL', 1);
INSERT INTO `t_drug` VALUES (2, '氨酚麻美干混悬剂', '氨酚麻美', '每包含对乙酰氨基酚80毫克、盐酸伪麻黄碱7.5毫克与无水氢溴酸右美沙芬2.5毫克', 'OTC', '解热镇痛', 4812, '盒', '混悬剂', '1包', '口服', '一日3次', '浙江康德', 'AFMMGHXJ', 'AFMM', 1);

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_order
-- ----------------------------
INSERT INTO `t_order` VALUES (1, '12312320210225115727885191697', 12824, '[{\"category\":\"OTC\",\"drugid\":2,\"drugname\":\"氨酚麻美干混悬剂\",\"frequency\":\"一日3次\",\"id\":1,\"myusage\":\"口服\",\"number\":2,\"prescriptionid\":1,\"price\":4812,\"singledose\":\"1包\",\"standard\":\"每包含对乙酰氨基酚80毫克、盐酸伪麻黄碱7.5毫克与无水氢溴酸右美沙芬2.5毫克\",\"unit\":\"盒\"},{\"category\":\"OTC\",\"drugid\":1,\"drugname\":\"小儿感冒颗粒\",\"frequency\":\"一日2次\",\"id\":2,\"myusage\":\"开水冲服\",\"number\":1,\"prescriptionid\":1,\"price\":3200,\"singledose\":\"12g\",\"standard\":\"每袋装6g\",\"unit\":\"盒\"}]', '2021-02-25 11:57:28', '2021-02-28 11:57:28', '2021-02-25 15:03:09', 3, '123123', 1, 5);
INSERT INTO `t_order` VALUES (2, '12312420210225143009806043336', 3200, '[{\"category\":\"OTC\",\"drugid\":1,\"drugname\":\"小儿感冒颗粒\",\"frequency\":\"一日2次\",\"id\":3,\"myusage\":\"开水冲服\",\"number\":1,\"prescriptionid\":2,\"price\":3200,\"singledose\":\"12g\",\"standard\":\"每袋装6g\",\"unit\":\"盒\"}]', '2021-02-25 14:30:10', '2021-02-28 14:30:10', '2021-02-25 16:10:11', 3, '123124', 2, 2);
INSERT INTO `t_order` VALUES (3, '12312420210225162556567095450', 3200, '[{\"createtime\":1614241114000,\"drugcompany\":\"三九医药\",\"drugid\":1,\"drugname\":\"小儿感冒颗粒\",\"drugunit\":\"盒\",\"id\":3,\"num\":1,\"orderid\":2,\"prescriptionid\":2,\"price\":3200,\"refundnum\":1,\"standard\":\"每袋装6g\"}]', '2021-02-25 16:25:57', '2021-02-28 16:25:57', NULL, 5, '123124', 2, 2);

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
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_prescription
-- ----------------------------
INSERT INTO `t_prescription` VALUES (1, 'B2102250002', 1, '赵薇', '儿科', '123123', '123456', '刘伟', '1岁', '2019-12-01', '男', NULL, '病毒性感冒发烧', '2021-02-25', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_prescription` VALUES (2, 'B2102250004', 1, '毛科', '儿科', '123124', '123457', '曹为', '2个月', '2020-12-21', '女', NULL, '手臂骨折', '2021-02-25', NULL, NULL, NULL, NULL, NULL, NULL);

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
  `singledose` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医嘱单次剂量',
  `myusage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医嘱用法',
  `frequency` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '医嘱频次',
  `drugid` int(11) NOT NULL COMMENT '关联的药品id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_prescriptiondrugs
-- ----------------------------
INSERT INTO `t_prescriptiondrugs` VALUES (1, 1, '氨酚麻美干混悬剂', '每包含对乙酰氨基酚80毫克、盐酸伪麻黄碱7.5毫克与无水氢溴酸右美沙芬2.5毫克', 'OTC', 4812, '盒', 2, '1包', '口服', '一日3次', 2);
INSERT INTO `t_prescriptiondrugs` VALUES (2, 1, '小儿感冒颗粒', '每袋装6g', 'OTC', 3200, '盒', 1, '12g', '开水冲服', '一日2次', 1);
INSERT INTO `t_prescriptiondrugs` VALUES (3, 2, '小儿感冒颗粒', '每袋装6g', 'OTC', 3200, '盒', 1, '12g', '开水冲服', '一日2次', 1);

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

-- ----------------------------
-- Records of t_prescriptionnum
-- ----------------------------
INSERT INTO `t_prescriptionnum` VALUES (1, 4, '2021-02-25');

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_name`(`name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'admin');
INSERT INTO `t_role` VALUES (2, 'manager');
INSERT INTO `t_role` VALUES (3, 'tollman');

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_sales_record
-- ----------------------------
INSERT INTO `t_sales_record` VALUES (1, 1, 1, 2, '氨酚麻美干混悬剂', '每包含对乙酰氨基酚80毫克、盐酸伪麻黄碱7.5毫克与无水氢溴酸右美沙芬2.5毫克', '浙江康德', 2, 4812, '2021-02-25 15:03:09', '盒', 0);
INSERT INTO `t_sales_record` VALUES (2, 1, 1, 1, '小儿感冒颗粒', '每袋装6g', '三九医药', 1, 3200, '2021-02-25 15:03:09', '盒', 0);
INSERT INTO `t_sales_record` VALUES (3, 2, 2, 1, '小儿感冒颗粒', '每袋装6g', '三九医药', 1, 3200, '2021-02-25 16:18:34', '盒', 1);

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `createtime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `state` tinyint(4) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `index_phone`(`phone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '超级管理员', '13880605659', '7dd75c55c0f3a84969cacc5fcdbbd980', '2021-02-25 00:39:40', 1);
INSERT INTO `t_user` VALUES (2, '赵雷', '13880605658', '7dd75c55c0f3a84969cacc5fcdbbd980', '2021-02-25 02:22:51', 1);
INSERT INTO `t_user` VALUES (3, '刘峰', '13880605657', '7dd75c55c0f3a84969cacc5fcdbbd980', '2021-02-25 02:24:56', 1);

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `uid` int(11) NOT NULL,
  `rid` int(11) NOT NULL,
  PRIMARY KEY (`uid`, `rid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (1, 1);
INSERT INTO `t_user_role` VALUES (2, 2);
INSERT INTO `t_user_role` VALUES (3, 3);

SET FOREIGN_KEY_CHECKS = 1;
