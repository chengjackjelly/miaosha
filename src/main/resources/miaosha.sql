/*
 Navicat Premium Data Transfer

 Source Server         : miao
 Source Server Type    : MySQL
 Source Server Version : 80011
 Source Host           : localhost:3306
 Source Schema         : miaosha

 Target Server Type    : MySQL
 Target Server Version : 80011
 File Encoding         : 65001

 Date: 08/03/2023 16:05:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
  `id` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `price` decimal(10, 2) UNSIGNED ZEROFILL NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `sales` int(11) UNSIGNED ZEROFILL NOT NULL DEFAULT 00000000000,
  `imgurl` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES (00000000002, 'apple', 00000002.00, 'a red apple', 00000000003, 'https://applemagazine.com/wp-content/uploads/2018/11/12197659796_407f5da012_b.jpg');
INSERT INTO `item` VALUES (00000000003, 'pencil', 00000010.00, 'pencil for children', 00000000000, 'https://content.etilize.com/900/1011914699.jpg');

-- ----------------------------
-- Table structure for item_stock
-- ----------------------------
DROP TABLE IF EXISTS `item_stock`;
CREATE TABLE `item_stock`  (
  `id` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `stock` int(10) UNSIGNED ZEROFILL NOT NULL,
  `item_id` int(10) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of item_stock
-- ----------------------------
INSERT INTO `item_stock` VALUES (00000000001, 0000000046, 0000000002);
INSERT INTO `item_stock` VALUES (00000000002, 0000000030, 0000000003);

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `user_id` int(10) UNSIGNED ZEROFILL NOT NULL DEFAULT 0000000000,
  `item_id` int(10) UNSIGNED ZEROFILL NOT NULL DEFAULT 0000000000,
  `amount` int(10) UNSIGNED ZEROFILL NOT NULL DEFAULT 0000000000,
  `order_price` decimal(10, 2) UNSIGNED ZEROFILL NOT NULL,
  `item_price` decimal(10, 2) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('2023030600000100', 0000000006, 0000000002, 0000000001, 00000002.00, 00000002.00);
INSERT INTO `order_info` VALUES ('2023030600000200', 0000000006, 0000000002, 0000000001, 00000002.00, 00000002.00);
INSERT INTO `order_info` VALUES ('2023030700000300', 0000000006, 0000000002, 0000000001, 00000000.99, 00000000.99);
INSERT INTO `order_info` VALUES ('2023030700000400', 0000000006, 0000000002, 0000000001, 00000000.99, 00000000.99);

-- ----------------------------
-- Table structure for promo_info
-- ----------------------------
DROP TABLE IF EXISTS `promo_info`;
CREATE TABLE `promo_info`  (
  `id` int(10) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `start_date` datetime(0) NOT NULL,
  `end_date` datetime(0) NOT NULL,
  `item_id` int(10) UNSIGNED ZEROFILL NOT NULL,
  `promo_item_price` decimal(10, 2) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of promo_info
-- ----------------------------
INSERT INTO `promo_info` VALUES (0000000001, '苹果大甩卖', '2023-03-03 00:00:00', '2023-05-01 00:00:00', 0000000002, 00000000.99);

-- ----------------------------
-- Table structure for sequence_info
-- ----------------------------
DROP TABLE IF EXISTS `sequence_info`;
CREATE TABLE `sequence_info`  (
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `current_value` int(11) NOT NULL,
  `step` int(11) NOT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sequence_info
-- ----------------------------
INSERT INTO `sequence_info` VALUES ('order_info', 4, 1);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `gender` tinyint(255) UNSIGNED ZEROFILL NOT NULL,
  `age` int(11) UNSIGNED ZEROFILL NOT NULL,
  `telphone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `register_mode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `third_party_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `telphone_unique_index`(`telphone`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (00000000001, 'test', 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000001, 00000000024, '123', '123', '00000000011');
INSERT INTO `user_info` VALUES (00000000006, 'CHEN JIAJUN', 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002, 00000000025, '15968856762', '', '0');
INSERT INTO `user_info` VALUES (00000000008, '黄金神威', 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000002, 00000000025, '110', 'ByPhone', '0');

-- ----------------------------
-- Table structure for user_password
-- ----------------------------
DROP TABLE IF EXISTS `user_password`;
CREATE TABLE `user_password`  (
  `id` int(11) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `encrpt_password` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
  `user_id` int(11) UNSIGNED ZEROFILL NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_password
-- ----------------------------
INSERT INTO `user_password` VALUES (00000000001, 'wrfwfwfw', 00000000001);
INSERT INTO `user_password` VALUES (00000000006, 'fGWSiB/KmvcmM1wUj63lwA==', 00000000006);
INSERT INTO `user_password` VALUES (00000000007, 'fGWSiB/KmvcmM1wUj63lwA==', 00000000008);

SET FOREIGN_KEY_CHECKS = 1;
