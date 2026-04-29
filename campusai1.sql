/*
Navicat MySQL Data Transfer

Source Server         : course0616
Source Server Version : 50740
Source Host           : localhost:3306
Source Database       : campusai

Target Server Type    : MYSQL
Target Server Version : 50740
File Encoding         : 65001

Date: 2026-04-29 22:27:44
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` text,
  `max_num` int(11) DEFAULT '0',
  `enroll_num` int(11) DEFAULT '0',
  `end_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '1-报名中 0-已截止',
  `type` varchar(20) DEFAULT 'lecture' COMMENT '活动类型：lecture-讲座, competition-比赛, club-社团',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `merchant_id` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `category_id` int(11) DEFAULT '0',
  `description` text,
  `image` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `merchant_name` varchar(100) DEFAULT NULL COMMENT '商家名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dish_category
-- ----------------------------
DROP TABLE IF EXISTS `dish_category`;
CREATE TABLE `dish_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `sort` int(11) DEFAULT '0' COMMENT '排序序号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COMMENT='菜品分类表';

-- ----------------------------
-- Table structure for merchant
-- ----------------------------
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_name` varchar(100) NOT NULL,
  `contact_person` varchar(50) DEFAULT NULL,
  `contact_phone` varchar(20) DEFAULT NULL,
  `address` varchar(200) DEFAULT NULL,
  `business_license` varchar(100) DEFAULT NULL,
  `description` text,
  `logo` varchar(200) DEFAULT NULL,
  `status` int(11) DEFAULT '0' COMMENT '0-待审核 1-已通过 2-已拒绝',
  `reject_reason` varchar(500) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `content` text NOT NULL,
  `createTime` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(32) NOT NULL COMMENT '订单编号',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `status` tinyint(4) DEFAULT '0' COMMENT '订单状态：0-待支付 1-已支付 2-已完成 3-已取消',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `merchant_name` varchar(100) DEFAULT '第一食堂',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='订单主表';

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` int(11) NOT NULL COMMENT '所属订单ID',
  `dish_id` bigint(20) NOT NULL COMMENT '菜品ID',
  `dish_name` varchar(100) NOT NULL COMMENT '菜品名称',
  `price` decimal(10,2) NOT NULL COMMENT '单价',
  `quantity` int(11) NOT NULL COMMENT '数量',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `dish_id` (`dish_id`),
  CONSTRAINT `order_item_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `order_item_ibfk_2` FOREIGN KEY (`dish_id`) REFERENCES `dish` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单明细表';

-- ----------------------------
-- Table structure for reserve
-- ----------------------------
DROP TABLE IF EXISTS `reserve`;
CREATE TABLE `reserve` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `seat_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `time_slot` varchar(50) DEFAULT NULL,
  `reserve_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for room_reservation
-- ----------------------------
DROP TABLE IF EXISTS `room_reservation`;
CREATE TABLE `room_reservation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) DEFAULT NULL,
  `seat_id` int(11) DEFAULT NULL,
  `seat_code` varchar(20) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `time_slot` varchar(20) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for seat
-- ----------------------------
DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `room_id` bigint(20) DEFAULT NULL,
  `seat_no` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for setmeal
-- ----------------------------
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '套餐名称',
  `price` decimal(10,2) NOT NULL COMMENT '套餐价格',
  `status` int(11) DEFAULT '1' COMMENT '1启用 0禁用',
  `description` text COMMENT '描述',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `image` varchar(200) DEFAULT NULL COMMENT '套餐图片',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for setmeal_dish
-- ----------------------------
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `setmeal_id` int(11) NOT NULL COMMENT '套餐ID',
  `dish_id` bigint(20) NOT NULL COMMENT '菜品ID',
  `quantity` int(11) DEFAULT '1' COMMENT '菜品份数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for study_room
-- ----------------------------
DROP TABLE IF EXISTS `study_room`;
CREATE TABLE `study_room` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_name` varchar(50) NOT NULL,
  `total_seat` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for study_room_seat
-- ----------------------------
DROP TABLE IF EXISTS `study_room_seat`;
CREATE TABLE `study_room_seat` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `room_id` int(11) DEFAULT NULL,
  `seat_code` varchar(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=367 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `role` varchar(20) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `avatar` varchar(200) DEFAULT NULL COMMENT '用户头像',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
