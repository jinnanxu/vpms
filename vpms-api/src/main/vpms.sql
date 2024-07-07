/*
 Navicat Premium Data Transfer

 Source Server         : 本地MySQL8
 Source Server Type    : MySQL
 Source Server Version : 80034 (8.0.34)
 Source Host           : localhost:3306
 Source Schema         : vpms

 Target Server Type    : MySQL
 Target Server Version : 80034 (8.0.34)
 File Encoding         : 65001

 Date: 07/07/2024 22:24:20
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `admin_id` int NOT NULL AUTO_INCREMENT,
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role` int NULL DEFAULT NULL,
  `real_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `work_dept` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status` int NULL DEFAULT NULL,
  `rate` double NULL DEFAULT 0 COMMENT '评分',
  `answer_times` int NULL DEFAULT 0 COMMENT '总回答次数',
  PRIMARY KEY (`admin_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (1, '13899990000', '123456', 1, '宋菲', '', '', 1, 0, 0);
INSERT INTO `admin` VALUES (2, '18677876579', '1234', 2, '许杨', '广东省清远市农科院', '研究员', 1, 3.5, 1);
INSERT INTO `admin` VALUES (3, '18999955238', '1234', 2, '蔡栩坤', '广东省农业技术科学院', '研究员', 1, 0, 0);
INSERT INTO `admin` VALUES (4, '18908732298', '123456', 2, '叶倩彤', '华南热带农业研究所', '助理研究员', 1, 4.8, 1);
INSERT INTO `admin` VALUES (5, '18700928798', '123456', 2, '王泽明', '华南热带农业研究所', '副研究员', -1, 0, 0);
INSERT INTO `admin` VALUES (6, '13890998787', '123456', 2, '赵之谦', '中国农科院华南研究所', '助理研究员', 1, 0, 0);

-- ----------------------------
-- Table structure for answer
-- ----------------------------
DROP TABLE IF EXISTS `answer`;
CREATE TABLE `answer`  (
  `answer_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键 ',
  `question_id` int NULL DEFAULT NULL COMMENT '对应的问题',
  `admin_id` int NULL DEFAULT NULL COMMENT '专家ID',
  `user_id` int NULL DEFAULT NULL COMMENT '农民ID',
  `answer_type` int NULL DEFAULT NULL COMMENT '类型：1-专家回复；2-农民追问',
  `answer_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '正文内容',
  PRIMARY KEY (`answer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of answer
-- ----------------------------
INSERT INTO `answer` VALUES (13, 4, 4, NULL, 1, '2024-02-17 08:31:02', '<p>查看了您的田间作业，初步断定是目前气温影响的，请尽早采收，防止更多的损坏</p>');
INSERT INTO `answer` VALUES (14, 4, NULL, 5, 2, '2024-02-27 22:30:36', '<p>请问可以通过调节温室的温度温度改善这种情况吗？</p>');
INSERT INTO `answer` VALUES (15, 4, 4, NULL, 1, '2024-02-27 22:31:51', '<p>如果条件允许是可以的，气温控制在25度以下，湿度在60%以下</p>');

-- ----------------------------
-- Table structure for crops
-- ----------------------------
DROP TABLE IF EXISTS `crops`;
CREATE TABLE `crops`  (
  `crops_id` int NOT NULL AUTO_INCREMENT,
  `crops_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '作物名称',
  `category` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '作物类型：苹果梨、柑橘柚、热带水果、葡萄莓、其它',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `detail` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL COMMENT '详情',
  `period` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '果实成熟周期（天）',
  `production` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '预估产量',
  `pics` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片',
  `create_time` datetime NULL DEFAULT NULL COMMENT '录入时间',
  `user_id` int NULL DEFAULT NULL COMMENT '所属用户',
  PRIMARY KEY (`crops_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of crops
-- ----------------------------
INSERT INTO `crops` VALUES (3, '阿克苏苹果', '苹果梨', 1, '阿克苏苹果，新疆维吾尔自治区阿克苏地区特产，中国国家地理标志产品。 [1-2]  \n阿克苏苹果又称加丽果，具有果面光滑细腻、色泽光亮、果肉细腻、果核透明、甘甜味厚、汁多无渣等特点。富含丰富的维生素C、纤维素、果胶等。而阿克苏苹果果核透明（俗称“糖心”）则是其区别于其他产地红富士苹果的显著标志。\n新疆阿克苏地区地处中纬度，位于塔里木盆地西北部，北高南低，日照丰富，热量适宜，属温带大陆性干旱气候，各地年日照时数2600小时以上，得天独厚的光能资源对红富士苹果品质的提高起到了很大的作用。光、水条件均比日本原产地和中国东部和西北东部苹果产区优越。 [3]', '280天', '120KG', 'http://118.25.89.125:18080/images/202310131521577mlcx.jpg,http://118.25.89.125:18080/images/202310131522027fyoj.jpg,http://118.25.89.125:18080/images/20231013152205ryrvp.jpg', '2023-10-13 10:53:59', 2);
INSERT INTO `crops` VALUES (5, '大白菜', '叶菜类', 1, '白菜（Brassica rapa var. glabra Regel）十字花科芸薹属二年生草本，含有丰富的维生素C，具有很强的抗氧化性，能够阻止致癌物质的生成和抑制癌细胞的繁殖。', '80天', '1500KG', 'http://118.25.89.125:18080/images/20240217/123840xp80o.jpg', '2023-10-13 11:49:10', 8);
INSERT INTO `crops` VALUES (6, '花椰菜', '花菜类', 1, '花椰菜营养丰富，富含蛋白质、脂肪、碳水化合物、食物纤维、多种维生素和钙、磷、铁等矿物质；性凉，味甘，助消化，增食欲，生津止渴，是一种保健食品。', '160天', '560KG/亩', 'http://118.25.89.125:18080/images/20240205103517j93bk.jpg', '2023-10-13 11:49:12', 1);
INSERT INTO `crops` VALUES (7, '白萝卜', '根菜类', 1, '白萝卜是根菜类的主要蔬菜，属十字花科萝卜属的二年生植物。', '60天', '1500KG/亩', 'http://118.25.89.125:18080/images/20240205103104rvupf.jpg', '2023-10-13 11:49:19', 1);
INSERT INTO `crops` VALUES (14, '莴笋', '茎菜类', 1, '上茎可供食用，茎皮白绿色，茎肉质脆嫩，幼嫩茎翠绿，成熟后转变白绿色。主要食用肉质嫩茎，可生食、凉拌、炒食、干制或腌渍，嫩叶也可食用。', '120天', '850KG', 'http://118.25.89.125:18080/images/20240217/124021diwxg.jpg', '2023-10-13 11:49:26', 8);
INSERT INTO `crops` VALUES (15, '水稻', '其它', -1, '优质水稻，使用农家肥种植', '120天', '100KG/亩', 'http://118.25.89.125:18080/images/20231013142225wgf9l.jpg', '2023-10-13 13:58:13', 1);
INSERT INTO `crops` VALUES (17, '秋月梨', '苹果梨', -1, '山东秋月梨，皮爆、肉厚、水足！', '150天', '220', 'http://118.25.89.125:18080/images/20231013152946pyht0.jpg,http://118.25.89.125:18080/images/202310131529496qenp.jpg,http://118.25.89.125:18080/images/202310131529531mafa.jpg', '2023-10-13 15:30:14', 2);
INSERT INTO `crops` VALUES (20, '黄心白菜', '叶菜类', 1, '黄心白菜外叶浓绿、内叶鲜黄，口感鲜爽，营养价值高，是制作韩式泡菜的专用白菜品种。', '120天', '8000斤/亩', 'http://118.25.89.125:18080/images/20240205101403ruxqd.jpg', '2024-02-05 10:20:36', 1);
INSERT INTO `crops` VALUES (21, '宁夏菜心', '叶菜类', 1, '宁夏菜心生产区域地跨黄土高原和内蒙古高原，地势南高北低，平均海拔1000米以上，适宜种植菜心。宁夏菜心，茎短缩，呈绿色。', '90天', '1200KG/亩', 'http://118.25.89.125:18080/images/20240205103830zhbbl.jpg', NULL, 1);

-- ----------------------------
-- Table structure for crops_item
-- ----------------------------
DROP TABLE IF EXISTS `crops_item`;
CREATE TABLE `crops_item`  (
  `crops_item_id` int NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `crops_id` int NULL DEFAULT NULL COMMENT '作物类型ID',
  `crops_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作物编号',
  `plant_time` datetime NULL DEFAULT NULL COMMENT '种植时间',
  `plant_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '种植位置',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`crops_item_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of crops_item
-- ----------------------------
INSERT INTO `crops_item` VALUES (1, 7, 'X102', '2023-11-21 08:58:40', '南区平地', 1);
INSERT INTO `crops_item` VALUES (2, 3, 'P201', '2022-06-23 09:00:46', '北坡', 1);
INSERT INTO `crops_item` VALUES (3, 21, 'C701', '2015-07-23 09:01:27', '南区温室', 1);
INSERT INTO `crops_item` VALUES (4, 7, 'X103', '2024-02-01 15:39:29', '南区河边', 1);
INSERT INTO `crops_item` VALUES (5, 7, 'X104', '2024-02-01 15:40:36', '南区河边', 1);
INSERT INTO `crops_item` VALUES (6, 7, 'X101', '2024-02-01 15:49:47', '北门', -1);
INSERT INTO `crops_item` VALUES (7, 6, 'L202', '2024-02-01 16:09:33', '南区缓坡', 1);
INSERT INTO `crops_item` VALUES (8, 6, 'H31', '2024-02-05 14:47:27', '南二区', 1);
INSERT INTO `crops_item` VALUES (9, 5, 'D21', '2024-02-17 12:41:45', '大棚温室区', 1);
INSERT INTO `crops_item` VALUES (10, 5, 'D18', '2024-02-17 12:42:31', '甘蔗地旁', 1);

-- ----------------------------
-- Table structure for crops_status
-- ----------------------------
DROP TABLE IF EXISTS `crops_status`;
CREATE TABLE `crops_status`  (
  `crops_status_id` int NOT NULL AUTO_INCREMENT COMMENT '物理ID',
  `crops_id` int NULL DEFAULT NULL COMMENT '作物类别ID',
  `crops_item_id` int NULL DEFAULT NULL COMMENT '作物实例ID',
  `user_id` int NULL DEFAULT NULL COMMENT '用户ID',
  `curr_status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '当前状态',
  `update_time` datetime NULL DEFAULT NULL COMMENT '状态更新时间',
  `pics` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`crops_status_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of crops_status
-- ----------------------------
INSERT INTO `crops_status` VALUES (1, 3, 2, 2, '抽穗期，生长良好', '2023-05-16 22:51:35', 'http://118.25.89.125:18080/images/202310162251357j1d2.jpg', NULL);
INSERT INTO `crops_status` VALUES (3, 3, 2, 2, '果实膨大期', '2023-07-27 10:50:39', 'http://118.25.89.125:18080/images/20231017105031ag05y.jpg,http://118.25.89.125:18080/images/202310171050343tpc5.jpg,http://118.25.89.125:18080/images/202310171050381zi7i.jpg', '进入果实快速膨大期，果实已成型。');
INSERT INTO `crops_status` VALUES (4, 6, 3, 1, '现蕾开花期', '2023-03-26 17:16:25', 'http://118.25.89.125:18080/images/20231017105623zd9qy.jpg', '全株已开花，通过果园养的蜜蜂进行蜂媒授粉，保证挂果率。');
INSERT INTO `crops_status` VALUES (5, 6, 3, 1, '春梢幼果期', '2023-04-18 10:58:20', 'http://118.25.89.125:18080/images/20231017105819sdy06.jpg', '已进入幼果期，挂果率达标。');
INSERT INTO `crops_status` VALUES (6, 3, 2, 2, '果实糖化期', '2023-10-26 09:43:18', 'http://118.25.89.125:18080/images/20231026094317u15u0.jpg', '果实已基本成熟，正在糖化期，约一个月后可采摘');
INSERT INTO `crops_status` VALUES (7, 3, 2, 7, '果实糖化期', '2023-10-26 09:43:18', 'http://118.25.89.125:18080/images/20231026094317u15u0.jpg', '果实已基本成熟，正在糖化期，约一个月后可采摘');
INSERT INTO `crops_status` VALUES (8, 7, 1, 1, '幼苗种植', '2023-10-16 15:17:05', 'http://118.25.89.125:18080/images/20231116151704cdqpi.jpg', '地里挖出坑，把小苗的营养杯去掉，放入坑内，浇足水分');
INSERT INTO `crops_status` VALUES (9, 7, 1, 1, '成长期', '2023-10-18 15:17:40', 'http://118.25.89.125:18080/images/20231116151740ms7v9.jpg', '植株生成期，生成速度正常。');
INSERT INTO `crops_status` VALUES (10, 7, 1, 1, '花期', '2023-10-30 15:18:14', 'http://118.25.89.125:18080/images/20231116151814qybk6.jpg', '已进入花期，当前生成状况良好。');
INSERT INTO `crops_status` VALUES (11, 7, 1, 1, '果实成长期', '2023-11-06 15:19:19', 'http://118.25.89.125:18080/images/20231116151918kbepy.jpg', '香蕉果实成长期，储备更加多的营养。');
INSERT INTO `crops_status` VALUES (12, 7, 1, 1, '花苞切割', '2023-11-16 15:20:11', 'http://118.25.89.125:18080/images/20231116152010f3q4v.jpg', '香蕉长到5-10排左右，工人们要把下面的花苞直接砍掉，这样也是为了把营养都集中到这5-10排上。');
INSERT INTO `crops_status` VALUES (13, 6, 3, 1, '冬季休眠期', '2024-01-31 15:10:48', 'http://118.25.89.125:18080/images/20240131151036ef25c.jpg', '生长缓慢，冬梢已摘');
INSERT INTO `crops_status` VALUES (14, 7, 1, 1, '收获完成', '2024-01-31 15:17:32', 'http://118.25.89.125:18080/images/20240131151659hkxdc.jpg', '冬蕉已收获，香蕉树已砍伐处理');
INSERT INTO `crops_status` VALUES (18, 7, 4, 1, '花期', '2023-11-28 12:09:23', 'http://118.25.89.125:18080/images/20231116151814qybk6.jpg', '已进入花期，当前生成状况良好。');
INSERT INTO `crops_status` VALUES (19, 7, 5, 1, '花期', '2023-11-28 12:09:23', 'http://118.25.89.125:18080/images/20231116151814qybk6.jpg', '已进入花期，当前生成状况良好。');
INSERT INTO `crops_status` VALUES (20, 6, 7, 1, '幼苗期', '2024-02-01 16:10:10', '', '新果树苗栽种');

-- ----------------------------
-- Table structure for knowledge
-- ----------------------------
DROP TABLE IF EXISTS `knowledge`;
CREATE TABLE `knowledge`  (
  `knowledge_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NULL DEFAULT NULL,
  `publish_date` datetime NULL DEFAULT NULL,
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `pics` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `view_count` int NULL DEFAULT 0,
  PRIMARY KEY (`knowledge_id`) USING BTREE,
  INDEX `FK_KNOW_EXPERT`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of knowledge
-- ----------------------------
INSERT INTO `knowledge` VALUES (1, 2, '2023-10-28 14:53:36', '病虫害防治', '番茄的主要病虫害的防治方法和技术', '<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\"><span class=\"bjh-strong\" style=\"max-width: 100%; color: rgb(51, 51, 51); font-weight: 700;\">1.防止香蕉叶斑</span>，通常该病害会出现在叶子上。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">推荐的药物和浓度：一千至一千五百倍25％丙环唑敌力脱乳油溶液，一千至一千五百倍23％腈苯唑悬剂溶液，一千五百倍20.67％杜邦万兴十氟硅唑乳油1000N溶液，五百至一千倍25％丙二唑乳油溶液，八百倍80％代森锰锌可湿性粉剂溶液。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">控制方法：喷洒叶片，其他的控制方法，合理密植，不要种植的太密，在不偏施氮肥的情况下加强水肥管理，及时清除香蕉园中的积水，及时清除枯叶，病叶和杂草，使焦园通风透光。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\"><span class=\"bjh-strong\" style=\"max-width: 100%; color: rgb(51, 51, 51); font-weight: 700;\">2.预防香蕉黑星病</span>，该病害会出现在叶子和果实上。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">推荐的药物和浓度：八百倍75％的百菌清可湿性粉剂溶液 ，八百倍50％的多菌灵粉剂溶液，八百倍80％的代森锰锌可湿性粉剂溶液，六千至八千倍40％的杜邦福星乳油溶液。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">控制方法：抽蕾后和开苞前，喷洒在花蕾和附近的叶子上。其他的控制方法，加强管理，提高抗病能力，对果实进行套袋。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\"><span class=\"bjh-strong\" style=\"max-width: 100%; color: rgb(51, 51, 51); font-weight: 700;\">3.香蕉炭疽病的防治</span>，该病害会出现在果实和假茎上。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">推荐的药物和浓度：2％的农抗120水剂二百倍液，五百至八百倍50％的多菌灵可湿性粉剂溶液，八百至一千倍75％的百菌清可湿性粉剂溶液，八百倍80％的代森锰锌可湿性粉剂溶液。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">控制方法：抽穗期时喷在花穗和小果上，其他的控制方法，对果实进行套袋。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\"><span class=\"bjh-strong\" style=\"max-width: 100%; color: rgb(51, 51, 51); font-weight: 700;\">4.香蕉花叶心腐病的防治</span>，该病害会出现在叶子，假茎和果实上等。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">推荐的药物和浓度：有关杀蚜剂的信息，请参阅“ 8.香蕉交脉蚜”。方法：定期喷洒杀蚜剂以消灭传播媒介。八百至一千倍20％的病毒清(A)溶液。</span>\r\n</p>\r\n<p>\r\n    <span class=\"bjh-p\" style=\"overflow-y: auto; max-width: 100%;\">控制方法：主要在早，中期喷洒。其他的控制方法：选择无病健康的培养苗，不用病区苗作种苗，保持焦园内清洁，及时清除杂草，及时根除患病的植物并集中燃烧，加强肥水的管理，不偏向施氮肥，和甘蔗，水稻，大豆或花生等的作物轮作</span>\r\n</p>\r\n<p>\r\n    <br/>\r\n</p>', 'http://118.25.89.125:18080/images/20231028152159u80d8.jpg,http://118.25.89.125:18080/images/20231028152203hy4a9.jpg,http://118.25.89.125:18080/images/20231028152208vwcuq.jpg', 1);
INSERT INTO `knowledge` VALUES (2, 2, '2023-10-29 14:53:49', '肥水管理', '青椒种植管理技术之肥水管理', '<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    要想脐橙长得好，除了选择适宜的种植园地和环境，平时还要做好管理，比如什么时候浇水、施肥，施什么肥等等。下面本文将重点介绍脐橙种植的肥水管理，有兴趣的朋友可以了解下。\r\n</p>\r\n<p class=\"ql-align-center\" style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; text-align: center; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    <img src=\"http://p5.itc.cn/images01/20200623/6a97159b94ce49b0add279ec8ea4866d.jpeg\"/>\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    <span style=\"font-weight: 700; border: 0px; margin: 0px; padding: 0px;\">一、浇水管理</span>\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    种植脐橙时一定要注意浇水管理，干旱时应适时灌水，雨季及时排水，可采用喷灌、滴灌、浇灌等方式灌水。果实采收前1个月果园不宜灌水，果实采子、收后1周内一定要灌水1次。\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    <span style=\"font-weight: 700; border: 0px; margin: 0px; padding: 0px;\">二、施肥管理</span>\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    <span style=\"font-weight: 700; border: 0px; margin: 0px; padding: 0px;\">1、幼龄树（1-3龄）</span>\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    以扩大树冠和增强树势为主，肥料要勤施薄施，全年施肥6-8次。施肥时注意以氮肥为主，配合磷钾肥。春夏秋梢抽生前10-15d或抽生后10-15d各施1次促梢肥，施腐熟有机肥0.25-0.5kg/株或尿素0.1-0.2kg/株+复合肥0.2-0.3kg/株。每次新梢自剪后追施壮梢肥1-2次，施复合肥0.10-0.15kg/株。施肥量需随着树龄增加而逐年加大。同时，配合根外追肥，8月下旬以后停止施用速效氮肥，秋冬季深施基肥1次。幼龄树的氮磷钾肥施用比例以100:0.25-0.30:0.5为宜，对翌年挂果的幼龄果树，应适当增施磷、钾肥。\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    <span style=\"font-weight: 700; border: 0px; margin: 0px; padding: 0px;\">2、初果树（4-6龄）</span>\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    既要继续扩大树冠，又要有一定的产量，其结果母枝以早秋梢为主，因而施肥要以壮果攻秋梢肥为重点。每年施肥3-4次，2月上中旬施春芽肥（占全年施肥量的15%），施尿素0.1-0.4kg/株+复合肥0.1-0.3kg/株；6月中下旬施壮果攻秋梢肥（占全年施肥量的50%），施腐熟豆麸2-3kg/株+钙镁磷肥0.5-1kg/株+硫酸钾0.25-0.3kg/株或复合肥（15-15-15）0.5-0.75kg/株+尿素0.3kg/株；9-10月施基肥、采果肥（占全年施肥量的35%），结合扩穴改土，冬肥秋施，施绿肥30kg/株、饼肥3-4kg/株、石灰1kg/株、磷肥1kg/株，采果后施尿素0.15-0.3kg株或沼液稀薄腐熟豆麸水20kg/株。施肥量随树龄和结果量的增加而逐年增加。\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    <span style=\"font-weight: 700; border: 0px; margin: 0px; padding: 0px;\">3、盛果树（7龄以上）</span>\r\n</p>\r\n<p style=\"border: 0px; margin-top: 0.63em; margin-bottom: 1.8em; padding: 0px; counter-reset: list-1 0 list-2 0 list-3 0 list-4 0 list-5 0 list-6 0 list-7 0 list-8 0 list-9 0; color: rgb(25, 25, 25); font-family: &quot;PingFang SC&quot;, Arial, 微软雅黑, 宋体, simsun, sans-serif; text-wrap: wrap; background-color: rgb(255, 255, 255);\">\r\n    脐橙盛果树的施肥以春芽肥和壮果肥为主，适施采果肥，并及时补充微量元素。每年2月中下旬施腐熟豆麸1.5-2kg/株+磷肥0.3-0.5kg/株+复合肥0.5-1kg/株作春芽肥（占全年施肥量的30%）；6月中下旬施腐熟豆麸4-6kg/株+复合肥0.5-1kg/株+硫酸钾0.3-0.5kg/株作壮果肥（占全年施肥量的50%）；9-10月结合扩穴改土，施基肥、采果肥（占全年施肥量的20%），实行冬肥秋施，施绿肥35-40kg/株+豆麸4-5kg/株+石灰1kg/株+磷肥1-1.5kg/株，采果后施腐熟豆麸1-1.5kg/株+磷肥0.5kg/株+复合肥0.5kg/株。\r\n</p>\r\n<p>\r\n    <br/>\r\n</p>', 'http://p5.itc.cn/images01/20200623/6a97159b94ce49b0add279ec8ea4866d.jpeg', 4);
INSERT INTO `knowledge` VALUES (3, 3, '2023-10-30 22:24:46', '肥水管理', '青冬瓜科学施肥与管理', '<p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important; padding: 0px !important;\">\r\n    <strong style=\"max-width: 100%;\"><span style=\"max-width: 100%;\"><span style=\"max-width: 100%; color: rgb(51, 51, 51);\">1.多施有机肥：</span></span></strong>\r\n</p>\r\n<p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important; padding: 0px !important;\">\r\n    有机肥中不仅含有果树生长所需要的各种营养元素，而且在其它许多方面均有良好的作用。虽然含量与化学肥料相比偏低，但品种全，不仅含有果树生长需要的大量营养元素N、P、K、Ca、Mg、S等，还含有果树营养生长所需要微量元素如Zn、Fe、B、Mn等。对于协调各种养分元素的供应方面有十分重要的作用。同时，有机肥在养分供应方面较为迟缓，一般不易出现肥害现象，且供应时间长而均衡生长中不易出现脱肥现象。有机肥中含有的深色物质能提高土壤对太阳能的吸收，有利于提高早春的地温。苹果树的根系能耐一 定的低温，一般土壤温度在5摄氏度左右开始活动吸收养分，但其吸收养分的速率和能力随地温的升高而逐渐增加，至地温达30摄氏度左右时有一最高值， 而后开始逐渐下降。在早春果树地上部分还未大量生长时，阳光可大量照射到地表，施有机肥的土壤颜色较深，太阳能吸收较多、地温升高较快能促进根系早活动、吸收积累一些养分供树体萌发之后利用，促进苹果的生长发育。\r\n</p>\r\n<p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important; padding: 0px !important;\">\r\n    <strong style=\"max-width: 100%;\">2.氮肥要适量：</strong><br/>\r\n</p>\r\n<p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important; padding: 0px !important;\">\r\n    氮是苹果树需要量较大的营养元素之一，每生产100千克果实约吸收0.3干克的纯氮。氨肥的使用对率 果的产量均有很大的影响。在一定范围内适当多施氮肥，有增大枝叶数量、增强树势和提高产量的作用。但若施用氮肥过多，则会引起树梢徒长，不仅引起坐果率下降，产量降低，而且品质及耐贮性均较差，容易导致苦痘病、红斑病、果锈等生理病害的发生。一般情况下 ，幼龄树每株年施用氮肥的量以纯氮计，为0.25-0.50千克，初果树0.5-1.0千克，初盛果树为1.0-1.5千克，盛果树为1.5~2.0千克。施用时间和使用量依树势而定，对于旺长树，追施时间以5月下旬至6月上旬为宜。此时，春梢停止生长，适量追施铵态氮肥，有助于花芽的生理分化，同时配施一定量的磷、钾肥；8月中下旬，秋梢停止生长时，在大量施用磷、钾肥的基础上，适度补充氮肥，施用的氮肥量，应取上述量的下限，如施用的有机肥量较多，则可不施或延施氮肥(上述用量的1/5-1/4)。对于树势较弱的苹果树，应在旺长前追施氮肥，特别是硝态氮肥，在苹果树萌芽前追施一定量的氮肥，并结合浇水、覆膜，在夏季借雨勤追，以促进秋梢的生长，恢复树势。到秋天，在落叶前再适度追施一 定量的氮肥，并轻施磷钾肥，以增加树体的贮备，提高芽质，促进根系的生长，为恢复树势做充分准备。正常结果的壮树，一般萌芽前追施氮肥，最好以硝态氮肥为主，适当配合施用铵态氮肥，以加速果实生长。同时，注意在采果后及时补肥浇水，以协调营养，恢复树体。增加树内物质贮备为翌年做好准备。\r\n</p>\r\n<p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important; padding: 0px !important;\">\r\n    对于短枝型苹果树，由于多为密植，且早果性、丰产性较好，因此其需肥量较普通型为多。一般幼龄树每年施用的氮肥量以纯氮计为6-12千克/亩面，磷肥的用量以P2O5为10-14千克/亩，钾肥的用量以K2O为3-6千克/亩。成年短枝型苹果树的年施用氮、磷钾，以纯养分计，氮肥12-18千克/亩时.磷肥9-15千克/亩，钾肥6~12千克/亩。具体到每个果园，还需要根据有机肥的施用量及士壤的肥力状况酌情调节。在施肥的时期方面，注意重施秋季基肥，及时于花前、果实膨大期花芽分化期进行追肥，每次施用量不可过大，注意在施用量较大时应全园撒施、适度深翻，防止肥害。\r\n</p>\r\n<p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important; padding: 0px !important;\">\r\n    <strong style=\"max-width: 100%;\">3.补充磷、钾肥：</strong><br/>\r\n</p>\r\n<p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important; padding: 0px !important;\">\r\n    磷、钾也是苹果树需要量较大的营养元素，每生产100千克果实约吸收0.05~0.1千克的P2O5，0.3千克的K2O。对结果的苹果树所做的试验表明，配施磷、钾肥较单施氨肥的增高幅度在11.8%-177.8%。施用磷、钾肥不仅能提高苹果的产量，还能促进根系的生长发育，增加叶片中的光合产物向茎、根、果等部位协同运输。同时，磷肥有十分显著的诱根作用，将磷肥适度深施可促进根系向土壤深层伸展，能显著提高果树的抗旱能力，减少病害的发生。研究表明：苹果幼龄树和成年树对磷、钾肥的需要量不同，一般幼龄树适量多施用一些磷肥，可明显促进果树的生长，其适宜的氨、确、钾比例为2：2：1或1：2：1；进人结果期之后，需适量增加氮肥的比例。其适宜的氮、磷、钾比例为2：1：3 或1：0.5：1，但在具体应用时，还需要考虑土壤的性质，对于西北黄土高原区山东、河北、河南等的黄河冲积平原苹果主产区，土壤中的钙含量较多，而磷低一些，因此，实际应用时应适度增加磷肥的用量，其氮、磷、钾的比例可选用1：1，其它土壤为中性至酸性的地区成龄果树可选用1：0.5： 1。磷肥和钾肥主要作秋季秋梢停止生长后基肥(或秋追肥)施用，应占总施肥量的一半以上，其余部分可作为春梢停止生长时花芽分化期的促花肥和果实膨大期的促果肥。\r\n</p>\r\n<p>\r\n    <br/>\r\n</p>', 'http://118.25.89.125:18080/images/20231028231949dnjul.jpg,http://118.25.89.125:18080/images/2023102823200354hbg.jpg', 3);
INSERT INTO `knowledge` VALUES (7, 4, '2023-10-29 17:16:17', '田间管理', '秋后迟菜心的田间管理要点', '<div style=\"max-width: 100%; overflow-x: visible; font-size: 18px; line-height: 30px; color: rgb(34, 34, 34); margin-top: 28px; font-family: arial;\"><span style=\"overflow-y: auto; max-width: 100%;\">可能很多人在网上经常看见别人告诉你说用这个药或者那个药来控梢杀梢效果好，或者又说用哪些药来让荔枝树苏醒的，其实这些人基本上都是不懂管理的。懂得人根本就不需要用那么多药，其实不用药或者很少用药控梢的才是行家高手。</span></div><div style=\"max-width: 100%; overflow-x: visible; font-size: 18px; line-height: 30px; color: rgb(34, 34, 34); margin-top: 24px; font-family: arial;\"><span style=\"overflow-y: auto; max-width: 100%;\">顺便说一句，懂行的专家不会免费告诉你怎么做，更不会在网上侃侃而谈浪费时间告诉你，能够告诉你的都是一些最低级的方法。反正天上不会掉馅饼这个道理谁都知道，那么天上掉下来的肯定不是好东西。他们就是让你们迷惑其中，好让你们的荔枝树不要年年挂果，因为这样一来他们的荔枝树挂果才能年年卖高价，自己想想看是不是这个道理？</span></div>', 'http://118.25.89.125:18080/images/20231029171617vtwbe.jpg', 3);
INSERT INTO `knowledge` VALUES (11, 6, '2023-10-29 17:15:46', '田间管理', '蔬菜采收后的田间管理，清园是关键，做好排灌', '<p>枯萎双引<strong style=\"color: rgb(34, 34, 34); font-family: arial; font-size: 18px; max-width: 100%;\">一、采摘后如何做到科学有效的管理</strong></p><div style=\"max-width: 100%; overflow-x: visible; font-size: 18px; line-height: 30px; color: rgb(34, 34, 34); margin-top: 24px; font-family: arial;\"><p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important;\"><span style=\"max-width: 100%;\">荔枝在开花结果的过程中已经</span><strong style=\"max-width: 100%;\">消耗了树体的大量养分</strong><span style=\"max-width: 100%;\">，所以采摘后的管理十分重要，良好科学的管理，能让树体</span><strong style=\"max-width: 100%;\">恢复体力，</strong><span style=\"max-width: 100%;\">来年也能</span><strong style=\"max-width: 100%;\">高产。</strong></p></div><div style=\"max-width: 100%; overflow-x: visible; font-size: 18px; line-height: 30px; color: rgb(34, 34, 34); margin-top: 24px; font-family: arial;\"><p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important;\"><strong style=\"max-width: 100%;\">1、及时修剪树体枝条和树冠</strong></p></div><div style=\"max-width: 100%; overflow-x: visible; font-size: 18px; line-height: 30px; color: rgb(34, 34, 34); margin-top: 24px; font-family: arial;\"><p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important;\"><span style=\"max-width: 100%;\">采摘后的修建工作可以</span><strong style=\"max-width: 100%;\">帮助树体枝条生长</strong><span style=\"max-width: 100%;\">。建议在采果后的15-20天内完成。修剪之前要</span><strong style=\"max-width: 100%;\">做好施肥，</strong><span style=\"max-width: 100%;\">为树体</span><strong style=\"max-width: 100%;\">提供肥料和养分</strong><span style=\"max-width: 100%;\">。注意秋梢萌发，方便培养健壮的</span><strong style=\"max-width: 100%;\">结果母枝。</strong></p></div><div style=\"max-width: 100%; overflow-x: visible; font-size: 18px; line-height: 30px; color: rgb(34, 34, 34); margin-top: 24px; font-family: arial;\"><p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important;\"><span style=\"max-width: 100%;\">在修剪的过程中注意</span><strong style=\"max-width: 100%;\">树冠处的修剪</strong><span style=\"max-width: 100%;\">，特别是</span><strong style=\"max-width: 100%;\">树梢部分</strong><span style=\"max-width: 100%;\">，如果梢多就主要修剪树梢。这些一般都是指的成年树，而一些幼树</span><strong style=\"max-width: 100%;\">不建议过重修剪，</strong><span style=\"max-width: 100%;\">以</span><strong style=\"max-width: 100%;\">轻修剪为宜</strong><span style=\"max-width: 100%;\">。除此之外过密的树体进行</span><strong style=\"max-width: 100%;\">回缩修剪。</strong><span style=\"max-width: 100%;\"></span></p></div><div style=\"max-width: 100%; overflow-x: visible; font-size: 18px; line-height: 30px; color: rgb(34, 34, 34); margin-top: 24px; font-family: arial;\"><p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important;\"><span style=\"max-width: 100%;\">修剪时注意方式方法，由从内而外，</span><strong style=\"max-width: 100%;\">从大到小的原则</strong><span style=\"max-width: 100%;\">来进行。像一些隐蔽枝、病虫枝等都要注意</span><strong style=\"max-width: 100%;\">及时修剪</strong><span style=\"max-width: 100%;\">。以此达到提升</span><strong style=\"max-width: 100%;\">透光性</strong><span style=\"max-width: 100%;\">、减少营养</span><strong style=\"max-width: 100%;\">消耗</strong><span style=\"max-width: 100%;\">，增强光合作用的目的，有效的保证来年的</span><strong style=\"max-width: 100%;\">树体健康。</strong></p><p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important;\"><br></p><p style=\"overflow-y: auto; max-width: 100%; line-height: 30px; margin-top: 0px !important; margin-bottom: 0px !important;\"><img src=\"http://118.25.89.125:18080/images/20231029160413g9voe.jpg\" style=\"max-width:100%;\"><br></p></div><p><br></p>', 'http://118.25.89.125:18080/images/20231029171546b1l80.jpg', 5);
INSERT INTO `knowledge` VALUES (13, 4, '2024-02-17 08:26:50', '农药指南', '叶菜防止叶黄的方法', '<p>荔枝控梢技术</p><p><img src=\"http://118.25.89.125:18080/images/20240217/082647f352h.jpg\"></p>', NULL, 2);
INSERT INTO `knowledge` VALUES (15, 4, '2024-02-17 15:26:45', '种植技术', '莴笋的种植技术与管理', '<p><strong>1、种植时间</strong></p><p>莴笋对环境的适应性较强，稍微耐寒但不耐高温，喜欢在冷凉的环境中生长，最佳的种植时间在春季的4-5月和秋季的9-10月份，生长周期在80-100天之间，春季种植的成熟期较快。</p><p><strong>2、选地翻耕</strong></p><p>莴笋属于浅根性作物，对水肥的吸收力较弱，选择土质深厚、疏松排水的微酸沙壤土，深耕土壤增加透气性和松软度，暴晒土壤消毒后耙平整细，覆盖透明的塑料薄膜等待播种。</p><p><strong>3、播种定植</strong></p><p>挑选适应性强、高产的莴笋种子，浸泡催芽后揭开薄膜，采用撒布的方式播种，覆盖0.5厘米细土，盖严薄膜后搭建小型拱棚，等待幼苗长出6片真叶时按照20x27厘米行株距移栽定植。</p><p><strong>4、水肥管理</strong></p><p>莴笋种植前施入腐熟的基肥，浇足水分促使养分和土壤充分融合，播种长出真叶后结合中耕除草并浇水施肥，在晴天时拔除杂草，增加土壤的透气性，选择稀释的尿素追肥，追肥后及时浇水。</p><p><img src=\"http://118.25.89.125:18080/images/20240217/1528123oemh.jpg\"></p>', NULL, 1);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`  (
  `question_id` int NOT NULL AUTO_INCREMENT COMMENT '物理ID',
  `user_id` int NULL DEFAULT NULL COMMENT '提问农民的ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '问题标题',
  `key_word` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关键字',
  `create_time` datetime NULL DEFAULT NULL COMMENT '提问时间',
  `status` int NULL DEFAULT NULL COMMENT '状态',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '问题详情',
  `rate` double UNSIGNED NULL DEFAULT NULL COMMENT '评分',
  `search_view_count` int NULL DEFAULT NULL COMMENT '被搜索与浏览的次数',
  PRIMARY KEY (`question_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question` VALUES (1, 2, '白菜出现软腐病怎么办？', '软腐病', '2024-01-19 10:51:26', 0, '白菜根部腐烂，叶片萎蔫', 5, 20);
INSERT INTO `question` VALUES (2, 7, '蓝莓焦枯病毒如何防治？', '蓝莓', '2024-02-19 10:55:33', 0, '<p>看图：</p><p><img src=\"http://118.25.89.125:18080/images/20240119105510t74ss.jpg\"></p>', NULL, 15);
INSERT INTO `question` VALUES (3, 4, '萝卜霜霉病应该如何治理？', '萝卜', '2024-01-26 16:10:59', 0, '我菜园的萝卜好像得了霜霉病，病斑连片，叶片变黄、干枯，应该如何处理？<p><img src=\"http://118.25.89.125:18080/images/20240216/Snipaste_2024-02-16_17-03-35.jpg\"></p>', 5, 56);
INSERT INTO `question` VALUES (4, 5, '包菜容易烧心，请问是什么原因引起的？', '包菜病害', '2024-01-19 16:40:49', 1, '现在已经进入成熟采收期，没有再施用药肥了，但是经常有烧心的情况，却没发现有明显的虫害，请问如何处理？', NULL, 33);
INSERT INTO `question` VALUES (5, 2, '灌溉器没有水出来怎么办', '农机', '2024-01-22 13:49:03', 0, '新买的灌溉器，通电后没有水出来怎么办？', 5, 96);
INSERT INTO `question` VALUES (8, 8, '芹菜能在南方夏季种植吗？', '种植', '2024-01-17 09:47:22', 0, '<p>芹菜能在南方夏季种植吗？</p>', 0, 3);
INSERT INTO `question` VALUES (9, 8, '大白菜干烧心病如何防治', '大白菜', '2024-02-17 11:57:21', 1, '大白菜干烧心病如何才能有效防治？<p><img src=\"http://118.25.89.125:18080/images/20240217/11571377u75.jpg\"></p>', 0, 15);

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `task_id` int NOT NULL AUTO_INCREMENT,
  `task_time` datetime NULL DEFAULT NULL COMMENT '作业时间',
  `crops_id` int NULL DEFAULT NULL COMMENT '作物类别ID',
  `crops_item_id` int NULL DEFAULT NULL COMMENT '作物单位ID',
  `period` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '物候期：\"发芽期\",\"出苗期\",\"分蘖期\",\"越冬期\",\"返青期\",\"拔节期\",\"孕穗期\",\"抽穗期\",\"开花期\",\"成熟期\"',
  `task_type` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '作业类型：\"巡园\",\"灌水\",\"施肥\",\"播种\",\"幼苗栽植\",\"植株调整\",\"中耕\",\"培土\",\"清园\",\"补苗\",\"其他\"',
  `task_detail` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '详情',
  `pics` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '图片',
  `weather` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '天气',
  `temp` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '气温',
  `user_id` int NULL DEFAULT NULL COMMENT '操作人',
  `advice` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '专家建议',
  `advice_expert` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '专家信息',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (13, '2023-10-17 12:43:26', 7, 1, '根茎膨大期', '灌水', '天热干旱，定时抽水灌溉，保证菜苗生长', 'http://118.25.89.125:18080/images/20230817224321y084e.png', '多云', '34', 1, NULL, NULL);
INSERT INTO `task` VALUES (14, '2023-10-17 09:46:55', 7, 1, '花芽分化期', '巡园', '查看蕉树生成情况', 'http://118.25.89.125:18080/images/202308172246424cxqw.png,http://118.25.89.125:18080/images/20230817224645ayfs0.png', '晴', '36', 1, NULL, NULL);
INSERT INTO `task` VALUES (15, '2023-10-18 09:51:53', 3, 2, '现蕾至开花期', '抹芽定梢', '', '', '小雨', '12', 2, NULL, NULL);
INSERT INTO `task` VALUES (16, '2023-10-16 09:51:53', 3, 2, '现蕾至开花期', '抹芽定梢', '', '', '小雨', '12', 2, NULL, NULL);
INSERT INTO `task` VALUES (17, '2023-10-16 09:55:20', 3, 2, '两次生理落果期', '修剪', '', 'http://118.25.89.125:18080/images/20231016095519isgpb.jpg', '阴', '17', 2, NULL, NULL);
INSERT INTO `task` VALUES (18, '2023-10-17 16:55:20', 3, 2, '两次生理落果期', '修剪', '', 'http://118.25.89.125:18080/images/20231016095519isgpb.jpg', '阴', '17', 2, NULL, NULL);
INSERT INTO `task` VALUES (19, '2023-10-18 16:43:29', 6, 3, '花芽分化期', '疏花', '进行疏花作业，保证成果率。', 'http://118.25.89.125:18080/images/202310161643251u3xs.jpg', '多云', '22', 1, NULL, NULL);
INSERT INTO `task` VALUES (21, '2023-10-24 09:45:36', 3, 2, '成熟期', '巡园', '每日巡园，检查成熟情况', 'http://118.25.89.125:18080/images/202310260945217x1iv.jpg', '晴', '18', 2, NULL, NULL);
INSERT INTO `task` VALUES (22, '2023-10-25 14:45:36', 3, 2, '成熟期', '巡园', '每日巡园', 'http://118.25.89.125:18080/images/202310260945217x1iv.jpg', '晴', '18', 2, NULL, NULL);
INSERT INTO `task` VALUES (23, '2023-10-26 09:49:37', 6, 3, '秋梢抽发期', '控梢', '人工抹梢，保证年后的开花率', 'http://118.25.89.125:18080/images/2023102609493574o6u.jpg', '晴', '22', 1, NULL, NULL);
INSERT INTO `task` VALUES (25, '2023-11-06 12:14:21', 7, 1, '现蕾至开花期', '保花保果', '香蕉的花期跟气候有关系，天气热的时候花期短，天气凉的时候花期稍微长点，一般从抽穗到断蕾大概持续一个月到三个月左右。如果不进行人工干预，香蕉会持续开花，持续结果，重重的果穗会坠弯香蕉树。', 'http://118.25.89.125:18080/images/202311161524214yw7y.jpg', '小雨', '24', 1, NULL, NULL);
INSERT INTO `task` VALUES (26, '2024-01-31 11:07:36', 7, 1, '秋梢抽发期', '施肥', '复合肥，5KG', 'http://118.25.89.125:18080/images/20240131110734oxquj.jpg', '多云', '18', 1, NULL, NULL);
INSERT INTO `task` VALUES (27, '2024-02-01 16:12:44', 6, 7, '幼苗期', '定植', '新菜苗栽种', 'http://118.25.89.125:18080/images/202402011612444eby3.jpg', '阴', '19', 1, '花椰菜不建议种在缓坡，最好种在平地。', '华南热带农业研究所：叶倩彤 2024-02-17 15:15:02');
INSERT INTO `task` VALUES (28, '2024-02-05 09:41:08', 21, 3, '成熟期', '采收', '采收300斤菜心', 'http://118.25.89.125:18080/images/20240205094106majuf.jpg', '阴', '25', 1, NULL, NULL);
INSERT INTO `task` VALUES (29, '2024-02-05 14:40:43', 7, 1, '越冬期', '施肥', '施用农家肥150KG', 'http://118.25.89.125:18080/images/20240205144041m5298.jpg', '小雨', '18', 1, NULL, NULL);
INSERT INTO `task` VALUES (30, '2024-02-05 14:50:27', 6, 7, '幼苗期', '幼苗栽植', '移裁花椰菜幼苗', 'http://118.25.89.125:18080/images/20240205145024ou2a8.jpg', '阴', '23', 1, '花椰菜喜凉不耐高温，应在换季前尽快采收以免发生腐坏', '华南热带农业研究所：叶倩彤 2024-02-28 08:33:28');
INSERT INTO `task` VALUES (31, '2024-02-05 14:54:15', 7, 6, '成熟期', '采收', '采收1200KG白萝卜', 'http://118.25.89.125:18080/images/20240205145413o8lky.jpg', '阴', '22', 1, NULL, NULL);
INSERT INTO `task` VALUES (32, '2024-02-17 12:43:26', 5, 10, '幼苗期', '幼苗栽植', '将大白菜幼苗从苗圃区移裁到菜地，共种植五垄地。', 'http://118.25.89.125:18080/images/20240217/124322ts5sg.jpg', '多云', '18', 8, '移裁幼苗适宜在上午进行。', '华南热带农业研究所：叶倩彤 2024-02-17 15:04:48');
INSERT INTO `task` VALUES (33, '2024-02-17 12:44:21', 5, 10, '幼苗期', '灌水', '日常浇水', '', '晴', '22', 8, '晴天不适宜在中午前后浇水，尽量在10:00前完成灌水作业。', '广东省农业技术科学院：蔡栩坤 2024-02-17 13:36:14');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '物理ID',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  `pwd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `last_login` datetime NULL DEFAULT NULL COMMENT '上次登录时间',
  `pic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像',
  `farm_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '农场名称',
  `status` int NULL DEFAULT NULL COMMENT '状态：1、-1',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '13760613080', '123456', '赵芳菲', '2024-02-01 11:56:22', 'http://118.25.89.125:18080/images/20240112095700ujyqy.jpg', '春雨果园', 1);
INSERT INTO `user` VALUES (2, '18900987786', '123456', '张慧怡', '2024-02-01 08:48:12', NULL, '新疆天山果业', 1);
INSERT INTO `user` VALUES (3, '13899087671', '123456', '刘元杰', '2023-09-04 22:57:37', NULL, NULL, 1);
INSERT INTO `user` VALUES (4, '18677871009', '123456', '黎伟立', '2023-09-04 22:57:37', NULL, NULL, -1);
INSERT INTO `user` VALUES (5, '18567670091', '123456', '李梦', '2023-09-22 15:35:03', 'http://118.25.89.125:18080/images/20240120221815q5lq5.jpg', NULL, 1);
INSERT INTO `user` VALUES (6, '18799083989', 'abcd', '张永安', '2023-10-11 10:25:24', NULL, NULL, 1);
INSERT INTO `user` VALUES (7, '18567673451', '123456', '李子秩', '2023-10-11 10:25:29', NULL, NULL, 1);
INSERT INTO `user` VALUES (8, '13899091234', '123456', '徐大侠', '2024-01-30 22:52:38', 'http://118.25.89.125:18080/images/20240130225025geyc7.jpg', '沙溪蔬菜园', -1);
INSERT INTO `user` VALUES (9, '13290998976', '123456', '王长贵', NULL, 'http://118.25.89.125:18080/images/20240204223433lys34.jpg', '新竹果园', 1);

SET FOREIGN_KEY_CHECKS = 1;
