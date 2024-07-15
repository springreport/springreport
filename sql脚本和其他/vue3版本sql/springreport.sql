/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : localhost:3306
 Source Schema         : springreport_v3

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 25/06/2024 11:45:24
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for doc_tpl
-- ----------------------------
DROP TABLE IF EXISTS `doc_tpl`;
CREATE TABLE `doc_tpl`  (
  `id` bigint(20) NOT NULL,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `tpl_code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板标识',
  `tpl_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for doc_tpl_settings
-- ----------------------------
DROP TABLE IF EXISTS `doc_tpl_settings`;
CREATE TABLE `doc_tpl_settings`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `header` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '页眉',
  `footer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '页脚',
  `main` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '内容',
  `margins` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '页边距',
  `width` int(6) NULL DEFAULT NULL COMMENT '宽度',
  `height` int(6) NULL DEFAULT NULL COMMENT '高度',
  `paper_direction` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'vertical' COMMENT '纸张方向 vertical纵向 horizontal横向',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `watermark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '水印',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` bigint(20) NOT NULL,
  `operate_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `status` tinyint(4) NULL DEFAULT NULL COMMENT '是否登录成功 1是 2否 默认 1',
  `error_info` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '错误信息',
  `operate_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求机器ip',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login_log
-- ----------------------------
INSERT INTO `login_log` VALUES (1805446522851737602, '2024-06-25 11:42:49', 2, '用户信息不存在。', '127.0.0.1', NULL, '2024-06-25 11:42:49', NULL, '2024-06-25 11:42:49', 1);
INSERT INTO `login_log` VALUES (1805446542774681602, '2024-06-25 11:42:54', 1, NULL, '127.0.0.1', NULL, '2024-06-25 11:42:54', NULL, '2024-06-25 11:42:54', 1);
INSERT INTO `login_log` VALUES (1805446855199997954, '2024-06-25 11:44:08', 1, NULL, '0:0:0:0:0:0:0:1', NULL, '2024-06-25 11:44:09', NULL, '2024-06-25 11:44:09', 1);

-- ----------------------------
-- Table structure for luckysheet
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet`;
CREATE TABLE `luckysheet`  (
  `id` bigint(30) NOT NULL,
  `block_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `row_size` int(8) NULL DEFAULT NULL COMMENT '行数',
  `column_size` int(8) NULL DEFAULT NULL COMMENT '列数',
  `sheet_index` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `list_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `status` tinyint(4) NULL DEFAULT NULL,
  `sheet_order` int(3) NULL DEFAULT NULL,
  `sheet_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sheet名称',
  `color` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'sheet颜色',
  `hide` tinyint(4) NULL DEFAULT 0 COMMENT '是否隐藏 0不隐藏 1隐藏',
  `merge_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单元格合并信息',
  `rowlen` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '行高集合',
  `columnlen` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '列宽集合',
  `rowhidden` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '隐藏行集合',
  `colhidden` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '隐藏列集合',
  `border_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '边框信息',
  `calcChain` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '公式链',
  `filter_select` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '筛选范围',
  `filter` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '筛选的具体设置',
  `luckysheet_alternateformat_save` text CHARACTER SET armscii8 COLLATE armscii8_general_ci NULL COMMENT '交替颜色配置',
  `luckysheet_conditionformat_save` text CHARACTER SET armscii8 COLLATE armscii8_general_ci NULL COMMENT '条件格式',
  `image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '图片信息',
  `dataVerification` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '数据验证信息',
  `frozen` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '冻结信息',
  `hyperlink` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '超链接信息',
  `del_flag` tinyint(4) NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_lib`(`block_id`, `sheet_index`, `list_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = armscii8 COLLATE = armscii8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luckysheet_cell
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_cell`;
CREATE TABLE `luckysheet_cell`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `block_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `row_no` int(8) NULL DEFAULT NULL COMMENT '横坐标',
  `column_no` int(8) NULL DEFAULT NULL COMMENT '纵坐标',
  `sheet_index` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `list_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `cell_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单元格数据信息',
  `del_flag` tinyint(4) NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mix`(`block_id`, `row_no`, `column_no`, `sheet_index`, `list_id`) USING BTREE,
  INDEX `idx_mix2`(`block_id`, `sheet_index`, `list_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luckysheet_his
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_his`;
CREATE TABLE `luckysheet_his`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `row_no` int(10) NULL DEFAULT NULL COMMENT '行号',
  `col_no` int(10) NULL DEFAULT NULL COMMENT '列号',
  `sheet_index` varchar(200) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL COMMENT 'sheet唯一标识',
  `list_id` varchar(200) CHARACTER SET armscii8 COLLATE armscii8_general_ci NOT NULL COMMENT '文档唯一标识',
  `bson` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '本次修改参数',
  `before_json` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '上次修改参数',
  `change_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '修改描述',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `type` tinyint(4) NULL DEFAULT 2 COMMENT '修改类型 1整体配置修改 2单元格内容修改',
  `operate_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作key',
  `operator` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '操作人',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_mix`(`row_no`, `col_no`, `sheet_index`, `list_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luckysheet_online_cell
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_online_cell`;
CREATE TABLE `luckysheet_online_cell`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT '模板sheetid',
  `coordsx` int(11) NULL DEFAULT NULL COMMENT '横坐标',
  `coordsy` int(11) NULL DEFAULT NULL COMMENT '纵坐标',
  `cell_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格值',
  `cell_value_type` tinyint(4) NULL DEFAULT 1 COMMENT '单元格内容类型 1固定值 2变量 3循环块',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `ct` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '单元格值格式',
  `ff` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字体',
  `bg` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '背景颜色',
  `fc` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字体颜色',
  `bl` tinyint(4) NULL DEFAULT 0 COMMENT '粗体 0 常规  1加粗',
  `it` tinyint(4) NULL DEFAULT 0 COMMENT '斜体 0 常规  1 斜体',
  `fs` int(3) NULL DEFAULT 10 COMMENT '字体大小',
  `cl` tinyint(4) NULL DEFAULT 0 COMMENT '删除线0 常规 、 1 删除线',
  `vt` tinyint(4) NULL DEFAULT NULL COMMENT '垂直对齐 0 中间、1 上、2下',
  `ht` tinyint(4) NULL DEFAULT NULL COMMENT '水平对齐 0 居中、1 左、2右',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luckysheet_report_block_cell
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_report_block_cell`;
CREATE TABLE `luckysheet_report_block_cell`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `report_cell_id` bigint(20) NULL DEFAULT NULL COMMENT '单元格id',
  `dataset_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集名称',
  `coordsx` int(11) NULL DEFAULT NULL COMMENT '横坐标',
  `coordsy` int(11) NULL DEFAULT NULL COMMENT '纵坐标',
  `cell_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格值',
  `cell_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格配置',
  `cell_value_type` tinyint(4) NULL DEFAULT 1 COMMENT '单元格内容类型 1固定值 2变量',
  `is_link` tinyint(4) NULL DEFAULT 2 COMMENT '是否超链接 1是 2否 默认2',
  `link_config` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '超链接配置',
  `is_merge` tinyint(4) NULL DEFAULT 2 COMMENT '是否是合并单元格 1是 2否',
  `row_span` int(11) NULL DEFAULT 1 COMMENT '合并行数',
  `col_span` int(11) NULL DEFAULT 1 COMMENT '合并列数',
  `is_function` tinyint(4) NULL DEFAULT 2 COMMENT '是否是函数 1是 2否',
  `is_data_verification` tinyint(4) NULL DEFAULT 2 COMMENT '是否是数据校验项 1是 2否',
  `data_verification` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据校验项目',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luckysheet_report_cell
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_report_cell`;
CREATE TABLE `luckysheet_report_cell`  (
  `id` bigint(20) NOT NULL,
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT '模板sheetid',
  `dataset_name` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `coordsx` int(11) NULL DEFAULT NULL COMMENT '横坐标',
  `coordsy` int(11) NULL DEFAULT NULL COMMENT '纵坐标',
  `cell_extend` tinyint(4) NULL DEFAULT 1 COMMENT '单元格扩展方向 1不扩展 2纵向扩展 3横向扩展 4交叉扩展',
  `cell_value` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格值',
  `cell_data` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格配置',
  `cell_value_type` tinyint(4) NULL DEFAULT 1 COMMENT '单元格内容类型 1固定值 2变量 3循环块',
  `aggregate_type` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'list' COMMENT '聚合类型',
  `group_property` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组属性 ',
  `is_link` tinyint(4) NULL DEFAULT 2 COMMENT '是否超链接 1是 2否 默认2',
  `link_config` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '超链接配置',
  `is_merge` tinyint(4) NULL DEFAULT 2 COMMENT '是否是合并单元格 1是 2否',
  `row_span` int(11) NULL DEFAULT 1 COMMENT '合并行数',
  `col_span` int(11) NULL DEFAULT 1 COMMENT '合并列数',
  `cell_function` tinyint(4) NULL DEFAULT 1 COMMENT '函数类型 1求和 2求平均值 3最大值 4最小值',
  `digit` tinyint(4) NULL DEFAULT 2 COMMENT '聚合函数小数位数',
  `group_summary_dependency_r` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组依赖行号',
  `group_summary_dependency_c` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '分组依赖列号',
  `data_from` tinyint(4) NULL DEFAULT 1 COMMENT '数据来源 1默认 2原始数据 3单元格',
  `is_group_merge` bit(1) NULL DEFAULT b'0' COMMENT '合并单元格是否合一',
  `is_function` tinyint(4) NULL DEFAULT 2 COMMENT '是否是函数 1是 2否',
  `warning` bit(1) NULL DEFAULT b'0' COMMENT '是否预警',
  `warning_rules` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '>=' COMMENT '预警规则',
  `warning_color` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警颜色',
  `threshold` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警阈值',
  `warning_content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '预警内容',
  `is_relied` tinyint(4) NULL DEFAULT 2 COMMENT '数据是否被其他单元格依赖 1是 2否 ',
  `rely_cells` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '依赖单元格坐标，多个单元格之间用逗号分割 ',
  `is_dict` bit(1) NULL DEFAULT b'0' COMMENT '是否是数据字典 ',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据字典数据源id',
  `dict_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据字典类型',
  `alternate_format` tinyint(4) NULL DEFAULT 2 COMMENT '是否交替颜色1是 2否',
  `alternate_format_bc_odd` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奇数行背景颜色',
  `alternate_format_bc_even` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '偶数行背景颜色',
  `alternate_format_fc_odd` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奇数行字体颜色',
  `alternate_format_fc_even` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '偶数行字体颜色',
  `is_conditions` tinyint(4) NULL DEFAULT 2 COMMENT '是否有单元格过滤条件 1是 2否',
  `cell_conditions` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格过滤条件',
  `filter_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'and' COMMENT '过滤类型 and or',
  `is_hidden_conditions` tinyint(4) NULL DEFAULT 2 COMMENT '是否有隐藏条件 1是 2否',
  `hidden_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'and' COMMENT '隐藏条件类型 and or',
  `hidden_conditions` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '隐藏条件',
  `is_chart_attr` tinyint(4) NULL DEFAULT 2 COMMENT '是否是图表中的单元格属性 1是 2否',
  `chart_ids` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图表唯一标识，多个图表用,分隔',
  `series_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图表系列名称',
  `is_chart_cell` tinyint(4) NULL DEFAULT 2 COMMENT '是否是图表的第一个单元格 1是 2否',
  `is_data_verification` tinyint(4) NULL DEFAULT 2 COMMENT '是否是数据校验项 1是 2否',
  `data_verification` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据校验项目',
  `is_drill` bit(1) NULL DEFAULT b'0' COMMENT '是否下钻',
  `drill_id` bigint(20) NULL DEFAULT NULL COMMENT '下钻报表id',
  `drill_attrs` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '下钻属性',
  `unit_transfer` bit(1) NULL DEFAULT b'0' COMMENT '是否数值单位转换',
  `transfer_type` tinyint(4) NULL DEFAULT 1 COMMENT '转换方式 1乘法 2除法',
  `multiple` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '100' COMMENT '倍数',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `is_subtotal` bit(1) NULL DEFAULT b'0' COMMENT '是否需要小计',
  `subtotal_cells` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '小计单元格',
  `is_subtotal_calc` bit(1) NULL DEFAULT b'0' COMMENT '是否有小计公式链',
  `subtotal_calc` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '小计公式链',
  `subtotal_attrs` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '小计属性',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luckysheet_report_forms_cell
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_report_forms_cell`;
CREATE TABLE `luckysheet_report_forms_cell`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT '模板 sheet id',
  `dataset_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据集名称',
  `coordsx` int(11) NULL DEFAULT NULL COMMENT '横坐标',
  `coordsy` int(11) NULL DEFAULT NULL COMMENT '纵坐标',
  `cell_extend` tinyint(4) NULL DEFAULT 1 COMMENT '单元格扩展方向 1不扩展 2纵向扩展 3横向扩展 4交叉扩展',
  `cell_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单元格值',
  `cell_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单元格配置',
  `cell_value_type` tinyint(4) NULL DEFAULT 1 COMMENT '单元格内容类型 1固定值 2变量',
  `is_merge` tinyint(4) NULL DEFAULT 2 COMMENT '是否是合并单元格 1是 2否',
  `row_span` int(11) NULL DEFAULT 1 COMMENT '合并行数',
  `col_span` int(11) NULL DEFAULT 1 COMMENT '合并列数',
  `cell_attrs` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '单元格属性',
  `warning` bit(1) NULL DEFAULT b'0' COMMENT '是否预警',
  `warning_color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预警颜色',
  `threshold` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预警阈值',
  `warning_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '预警内容',
  `is_function` tinyint(4) NULL DEFAULT 2 COMMENT '是否是函数 1是 2否',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '填报报表单元格' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for luckysheet_report_forms_his
-- ----------------------------
DROP TABLE IF EXISTS `luckysheet_report_forms_his`;
CREATE TABLE `luckysheet_report_forms_his`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '报表模板id',
  `sheet_index` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '报表模板sheet 标识',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源id',
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名称',
  `basic_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '原始数据',
  `report_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '上报的数据',
  `change_data_before` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发生变化的部分修改前的数据',
  `change_data_after` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '发生变化的部分修改后的数据',
  `operate_ip` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求机器ip',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '填报报表历史记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for map_data
-- ----------------------------
DROP TABLE IF EXISTS `map_data`;
CREATE TABLE `map_data`  (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `count` int(11) NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mq_failed_msg
-- ----------------------------
DROP TABLE IF EXISTS `mq_failed_msg`;
CREATE TABLE `mq_failed_msg`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '消息类型 1缓存 2数据库 3历史记录',
  `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '消息内容',
  `failure_exception` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '失败异常信息',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for online_tpl
-- ----------------------------
DROP TABLE IF EXISTS `online_tpl`;
CREATE TABLE `online_tpl`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `report_id` bigint(20) NULL DEFAULT NULL COMMENT '来源报表id',
  `tpl_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文档名称',
  `list_id` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文档唯一标识',
  `view_auth` tinyint(4) NULL DEFAULT 1 COMMENT '查看权限 1所有人可见 2指定角色',
  `design_pwd` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设计密码',
  `export_encrypt` tinyint(4) NULL DEFAULT 2 COMMENT '导出是否加密 1是 2否',
  `source` tinyint(4) NULL DEFAULT 1 COMMENT '来源方式 1自己创建 2动态报表另存为',
  `data_load_type` tinyint(4) NULL DEFAULT 1 COMMENT '数据加载方式 1全部加载2单sheet加载',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for online_tpl_sheet
-- ----------------------------
DROP TABLE IF EXISTS `online_tpl_sheet`;
CREATE TABLE `online_tpl_sheet`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sheet名称',
  `sheet_order` int(6) NULL DEFAULT NULL COMMENT 'sheet顺序',
  `rowlen` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格高度信息',
  `columnlen` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格宽度信息',
  `merge` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '单元格合并信息',
  `border_info` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '边框信息',
  `frozen` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '冻结属性',
  `images` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片',
  `sheet_index` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sheet页唯一索引',
  `calc_chain` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '计算链，有公式的单元格信息',
  `hyper_link` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '超衔接',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log`  (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `user_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作人姓名',
  `fun_module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模块',
  `operate_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作类型',
  `operate_remark` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '操作注释',
  `operate_method` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求操作的方法',
  `operate_params` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '请求参数',
  `operate_time` datetime(0) NULL DEFAULT NULL COMMENT '操作时间',
  `operate_status` tinyint(4) NULL DEFAULT 1 COMMENT '操作状态,1:成功 2:失败 默认 1',
  `error_info` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '错误消息',
  `result` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '返回结果',
  `operate_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '请求机器ip',
  `execute_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '执行时长',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of operate_log
-- ----------------------------
INSERT INTO `operate_log` VALUES (1805446522910457858, NULL, NULL, 'Login', 'Update', '用户登录', 'com.springreport.controller.login.LoginController.doLogin', '{\"offSet\":0,\"pageSize\":10,\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"userName\":\"madmin\",\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:42:49', 2, '请求/springReport/api/login/doLogin调用结束，调用过程出现异常，返回结果：，异常信息：用户名或者密码输入错误。异常类型：class com.springreport.exception.BizException，执行时长:126ms', '', '127.0.0.1', '126', NULL, '2024-06-25 11:42:49', NULL, '2024-06-25 11:42:49', 1);
INSERT INTO `operate_log` VALUES (1805446542879539202, NULL, NULL, 'Login', 'Update', '用户登录', 'com.springreport.controller.login.LoginController.doLogin', '{\"offSet\":0,\"pageSize\":10,\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"userName\":\"admin\",\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:42:54', 1, '请求/springReport/api/login/doLogin调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"apis\":[\"reportType_search\",\"reportType_insert\",\"reportType_update\",\"reportType_getDetail\",\"reportType_delete\",\"reportType_batchDelete\",\"reportDatasource_search\",\"reportDatasource_insert\",\"reportDatasource_update\",\"reportDatasource_getDetail\",\"reportDatasource_delete\",\"reportDatasource_batchDelete\",\"reportTpl_search\",\"reportTpl_insert\",\"reportTpl_update\",\"reportTpl_getDetail\",\"reportTpl_delete\",\"reportTpl_batchDelete\",\"reportTpl_reportDesign\",\"sysUser_search\",\"sysUser_insert\",\"sysUser_update\",\"sysUser_getDetail\",\"sysUser_delete\",\"sysUser_batchDelete\",\"sysRole_search\",\"sysRole_insert\",\"sysRole_update\",\"sysRole_getDetail\",\"sysRole_delete\",\"sysRole_batchDelete\",\"sysMenu_search\",\"sysMenu_insert\",\"sysMenu_update\",\"sysMenu_getDetail\",\"sysMenu_delete\",\"sysMenu_batchDelete\",\"sysApi_search\",\"sysApi_insert\",\"sysApi_update\",\"sysApi_getDetail\",\"sysApi_delete\",\"sysApi_batchDelete\",\"sysMenu_sysApi\",\"reportDesign_deleteDataSet\",\"reportDesign_addDataSet\",\"sysRole_authed\",\"roleReport_search\",\"roleReport_view\",\"screenTpl_search\",\"screenTpl_insert\",\"screenTpl_update\",\"screenTpl_getDetail\",\"screenTpl_delete\",\"screenTpl_deleteBatch\",\"screenTpl_screenDesign\",\"screenTpl_saveDesign\",\"printTpl_search\",\"printTpl_insert\",\"printTpl_update\",\"printTpl_getDetail\",\"printTpl_delete\",\"printTpl_batchDelete\",\"printTpl_design\",\"printTpl_saveDesign\",\"printTpl_print\",\"screenTpl_refresh\",\"screenTpl_content\",\"screenContent_refresh\",\"reportTpl_reportView\",\"reportDesign_saveLuckyTpl\",\"reportDesign_editDataSet\",\"reportDesign_previewReport\",\"screenTpl_viewScreen\",\"screenTpl_previewDesign\",\"multiScreen_search\",\"multiScreen_insert\",\"multiScreen_update\",\"multiScreen_delete\",\"multiScreen_deleteBatch\",\"multiScreen_preview\",\"multiScreen_view\",\"reportTpl_changePwd\",\"viewReport_Search\",\"viewReport_view\",\"reportForms_addDataSet\",\"reportForms_editDataSet\",\"reportForms_deleteDataSet\",\"reportForms_saveTpl\",\"reportForms_previewReport\",\"reportForms_ReportData\",\"reportDatasource_dict\",\"reportDatasourceDictType_search\",\"reportDatasourceDictType_insert\",\"reportDatasourceDictType_update\",\"reportDatasourceDictType_getDetail\",\"reportDatasourceDictType_delete\",\"reportDatasourceDictType_dictData\",\"reportDatasourceDictData_search\",\"reportDatasourceDictData_insert\",\"reportDatasourceDictData_update\",\"reportDatasourceDictData_getDetail\",\"reportDatasourceDictData_delete\",\"reportTpl_copy\",\"screenTpl_copy\",\"onlineTpl_search\",\"onlineTpl_insert\",\"onlineTpl_update\",\"onlineTpl_getDetai\",\"onlineTpl_delete\",\"onlineTpl_editDoc\",\"reportTpl_reportShare\",\"reportTask_search\",\"reportTask_detail\",\"reportTask_edit\",\"reportTask_delete\",\"reportTask_batchdelete\",\"reportTask_runTask\",\"reportTask_pause\",\"reportTask_resume\",\"reportTask_insert\",\"reportTpl_Task\",\"sysPost_search\",\"sysPost_insert\",\"sysPost_delete\",\"sysPost_getDetail\",\"sysPost_edit\",\"sysDept_search\",\"sysDept_insert\",\"sysDept_getDetail\",\"sysDept_edit\",\"sysDept_delete\",\"sysMerchantAuth_search\",\"sysMerchantAuth_insert\",\"sysMerchantAuth_getDetail\",\"sysMerchantAuth_edit\",\"sysMerchantAuth_delete\",\"sysMerchant_search\",\"sysMerchant_insert\",\"sysMerchant_delete\",\"sysMerchant_getDetail\",\"sysMerchant_edit\",\"sysUser_resetPwd\",\"docTpl_search\",\"docTpl_getDetail\",\"docTpl_insert\",\"docTpl_edit\",\"docTpl_delete\",\"docTpl_design\",\"docTpl_view\",\"docTpl_save\",\"docTpl_preview\",\"docTpl_export\"],\"isAdmin\":1,\"isSystemMerchant\":1,\"merchantMode\":1,\"merchantNo\":\"SR00000000\",\"roleName\":\"管理员\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc1N5c3RlbU1lcmNoYW50IjoxLCJ1c2VyUmVhbE5hbWUiOiJhZG1pbiIsIm1lcmNoYW50TW9kZSI6MSwicm9sZU5hbWUiOiLnrqHnkIblkZgiLCJpc0FkbWluIjoxLCJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNzE5MzczMzc0LCJ1c2VySWQiOjE0MDQ2OTczMDE4ODgyOTQ5MjUsIm1lcmNoYW50Tm8iOiJTUjAwMDAwMDAwIn0.vXuDsN9-7dImdxfS8ZoiJoyMAnc-nYxNikiJ5nowx8ZKQooFlo8WU9sqRkPrR_LxUUT0LZNyGstoHYfK1zTbYg\",\"userId\":\"1404697301888294925\",\"userName\":\"admin\",\"userRealName\":\"admin\"},\"url\":\"\"}执行时长:59ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"apis\":[\"reportType_search\",\"reportType_insert\",\"reportType_update\",\"reportType_getDetail\",\"reportType_delete\",\"reportType_batchDelete\",\"reportDatasource_search\",\"reportDatasource_insert\",\"reportDatasource_update\",\"reportDatasource_getDetail\",\"reportDatasource_delete\",\"reportDatasource_batchDelete\",\"reportTpl_search\",\"reportTpl_insert\",\"reportTpl_update\",\"reportTpl_getDetail\",\"reportTpl_delete\",\"reportTpl_batchDelete\",\"reportTpl_reportDesign\",\"sysUser_search\",\"sysUser_insert\",\"sysUser_update\",\"sysUser_getDetail\",\"sysUser_delete\",\"sysUser_batchDelete\",\"sysRole_search\",\"sysRole_insert\",\"sysRole_update\",\"sysRole_getDetail\",\"sysRole_delete\",\"sysRole_batchDelete\",\"sysMenu_search\",\"sysMenu_insert\",\"sysMenu_update\",\"sysMenu_getDetail\",\"sysMenu_delete\",\"sysMenu_batchDelete\",\"sysApi_search\",\"sysApi_insert\",\"sysApi_update\",\"sysApi_getDetail\",\"sysApi_delete\",\"sysApi_batchDelete\",\"sysMenu_sysApi\",\"reportDesign_deleteDataSet\",\"reportDesign_addDataSet\",\"sysRole_authed\",\"roleReport_search\",\"roleReport_view\",\"screenTpl_search\",\"screenTpl_insert\",\"screenTpl_update\",\"screenTpl_getDetail\",\"screenTpl_delete\",\"screenTpl_deleteBatch\",\"screenTpl_screenDesign\",\"screenTpl_saveDesign\",\"printTpl_search\",\"printTpl_insert\",\"printTpl_update\",\"printTpl_getDetail\",\"printTpl_delete\",\"printTpl_batchDelete\",\"printTpl_design\",\"printTpl_saveDesign\",\"printTpl_print\",\"screenTpl_refresh\",\"screenTpl_content\",\"screenContent_refresh\",\"reportTpl_reportView\",\"reportDesign_saveLuckyTpl\",\"reportDesign_editDataSet\",\"reportDesign_previewReport\",\"screenTpl_viewScreen\",\"screenTpl_previewDesign\",\"multiScreen_search\",\"multiScreen_insert\",\"multiScreen_update\",\"multiScreen_delete\",\"multiScreen_deleteBatch\",\"multiScreen_preview\",\"multiScreen_view\",\"reportTpl_changePwd\",\"viewReport_Search\",\"viewReport_view\",\"reportForms_addDataSet\",\"reportForms_editDataSet\",\"reportForms_deleteDataSet\",\"reportForms_saveTpl\",\"reportForms_previewReport\",\"reportForms_ReportData\",\"reportDatasource_dict\",\"reportDatasourceDictType_search\",\"reportDatasourceDictType_insert\",\"reportDatasourceDictType_update\",\"reportDatasourceDictType_getDetail\",\"reportDatasourceDictType_delete\",\"reportDatasourceDictType_dictData\",\"reportDatasourceDictData_search\",\"reportDatasourceDictData_insert\",\"reportDatasourceDictData_update\",\"reportDatasourceDictData_getDetail\",\"reportDatasourceDictData_delete\",\"reportTpl_copy\",\"screenTpl_copy\",\"onlineTpl_search\",\"onlineTpl_insert\",\"onlineTpl_update\",\"onlineTpl_getDetai\",\"onlineTpl_delete\",\"onlineTpl_editDoc\",\"reportTpl_reportShare\",\"reportTask_search\",\"reportTask_detail\",\"reportTask_edit\",\"reportTask_delete\",\"reportTask_batchdelete\",\"reportTask_runTask\",\"reportTask_pause\",\"reportTask_resume\",\"reportTask_insert\",\"reportTpl_Task\",\"sysPost_search\",\"sysPost_insert\",\"sysPost_delete\",\"sysPost_getDetail\",\"sysPost_edit\",\"sysDept_search\",\"sysDept_insert\",\"sysDept_getDetail\",\"sysDept_edit\",\"sysDept_delete\",\"sysMerchantAuth_search\",\"sysMerchantAuth_insert\",\"sysMerchantAuth_getDetail\",\"sysMerchantAuth_edit\",\"sysMerchantAuth_delete\",\"sysMerchant_search\",\"sysMerchant_insert\",\"sysMerchant_delete\",\"sysMerchant_getDetail\",\"sysMerchant_edit\",\"sysUser_resetPwd\",\"docTpl_search\",\"docTpl_getDetail\",\"docTpl_insert\",\"docTpl_edit\",\"docTpl_delete\",\"docTpl_design\",\"docTpl_view\",\"docTpl_save\",\"docTpl_preview\",\"docTpl_export\"],\"isAdmin\":1,\"isSystemMerchant\":1,\"merchantMode\":1,\"merchantNo\":\"SR00000000\",\"roleName\":\"管理员\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc1N5c3RlbU1lcmNoYW50IjoxLCJ1c2VyUmVhbE5hbWUiOiJhZG1pbiIsIm1lcmNoYW50TW9kZSI6MSwicm9sZU5hbWUiOiLnrqHnkIblkZgiLCJpc0FkbWluIjoxLCJ1c2VyTmFtZSI6ImFkbWluIiwiZXhwIjoxNzE5MzczMzc0LCJ1c2VySWQiOjE0MDQ2OTczMDE4ODgyOTQ5MjUsIm1lcmNoYW50Tm8iOiJTUjAwMDAwMDAwIn0.vXuDsN9-7dImdxfS8ZoiJoyMAnc-nYxNikiJ5nowx8ZKQooFlo8WU9sqRkPrR_LxUUT0LZNyGstoHYfK1zTbYg\",\"userId\":\"1404697301888294925\",\"userName\":\"admin\",\"userRealName\":\"admin\"},\"url\":\"\"}', '127.0.0.1', '59', NULL, '2024-06-25 11:42:54', NULL, '2024-06-25 11:42:54', 1);
INSERT INTO `operate_log` VALUES (1805446544838279169, 1404697301888294925, 'admin', 'SysMerchant', 'Search', '获取所有的商户列表', 'com.springreport.controller.sysmerchant.SysMerchantController.getMerchantList', '[]', '2024-06-25 11:42:55', 1, '请求/springReport/api/sysMerchant/getMerchantList调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824130\",\"isSystemMerchant\":1,\"merchantName\":\"系统默认商户\",\"merchantNo\":\"SR00000000\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111112\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703388920000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824131\",\"isSystemMerchant\":2,\"merchantName\":\"测试商户\",\"merchantNo\":\"SR00000001\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111111\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703390521000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"createTime\":1703391173000,\"creator\":\"1404697301888294915\",\"currentPage\":1,\"delFlag\":1,\"email\":\"sdfa@1212.com\",\"id\":\"1738774719438217218\",\"isSystemMerchant\":2,\"merchantName\":\"测试2\",\"merchantNo\":\"SR00000002\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"12354141426\",\"status\":2,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703464856000,\"updater\":\"1404697301888294915\"}],\"url\":\"\"}执行时长:15ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824130\",\"isSystemMerchant\":1,\"merchantName\":\"系统默认商户\",\"merchantNo\":\"SR00000000\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111112\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703388920000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824131\",\"isSystemMerchant\":2,\"merchantName\":\"测试商户\",\"merchantNo\":\"SR00000001\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111111\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703390521000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"createTime\":1703391173000,\"creator\":\"1404697301888294915\",\"currentPage\":1,\"delFlag\":1,\"email\":\"sdfa@1212.com\",\"id\":\"1738774719438217218\",\"isSystemMerchant\":2,\"merchantName\":\"测试2\",\"merchantNo\":\"SR00000002\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"12354141426\",\"status\":2,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703464856000,\"updater\":\"1404697301888294915\"}],\"url\":\"\"}', '127.0.0.1', '15', 1404697301888294925, '2024-06-25 11:42:55', NULL, '2024-06-25 11:42:55', 1);
INSERT INTO `operate_log` VALUES (1805446544980885506, 1404697301888294925, 'admin', 'SysMenu', 'Search', '获取首页菜单树', 'com.springreport.controller.sysmenu.SysMenuController.getIndexMenu', '{\"isSystemMerchant\":1,\"userRealName\":\"admin\",\"merchantMode\":1,\"roleName\":\"管理员\",\"expireDate\":\"1719373374000\",\"isAdmin\":1,\"userName\":\"admin\",\"userId\":\"1404697301888294925\",\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:42:55', 1, '请求/springReport/api/sysMenu/getIndexMenu调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"icon\":\"icon-chart-line\",\"id\":\"1404722852271263746\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/report\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-table-report\",\"id\":\"1405299504554487810\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportType\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表类型管理\"},{\"icon\":\"icon-data\",\"id\":\"1405299619029626882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportDatasource\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"数据源管理\"},{\"icon\":\"icon-table\",\"id\":\"1405300363866714113\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表模板管理\"},{\"icon\":\"icon-excel\",\"id\":\"1626461319411445762\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/onlineTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"协同文档管理\"},{\"icon\":\"icon-word\",\"id\":\"1786056695102541825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/docTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"word模板管理\"},{\"icon\":\"icon-full-screen\",\"id\":\"1421971956013498370\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/screenTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"大屏模板管理\"},{\"icon\":\"icon-eyes\",\"id\":\"1544474827944828930\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/viewReport\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表查看\"},{\"icon\":\"icon-multi-rectangle\",\"id\":\"1534817266438144001\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/multiScreen\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"多大屏管理\"}],\"title\":\"报表\"},{\"icon\":\"icon-system\",\"id\":\"1405315562552209410\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/system\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-peoples\",\"id\":\"1405466770491432962\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysUser\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"用户管理\"},{\"icon\":\"icon-user\",\"id\":\"1405466871968423938\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysRole\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"角色管理\"},{\"icon\":\"icon-music-list\",\"id\":\"1405466972459753473\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMenu\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"菜单管理\"},{\"icon\":\"icon-user-positioning\",\"id\":\"1738138171403386882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysPost\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"岗位管理\"},{\"icon\":\"icon-network-tree\",\"id\":\"1738779159977222145\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysDept\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"部门管理\"}],\"title\":\"系统管理\"},{\"icon\":\"icon-shop\",\"id\":\"1738480133297819649\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/merchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-permissions\",\"id\":\"1738480515709292545\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchantAuthTemplate\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"权限模板\"},{\"icon\":\"icon-user-business\",\"id\":\"1738480923462750209\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"租户管理\"}],\"title\":\"租户管理\"},{\"icon\":\"icon-like\",\"id\":\"1567288878307405825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/iconPark\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"图标\"}],\"url\":\"\"}执行时长:49ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"icon\":\"icon-chart-line\",\"id\":\"1404722852271263746\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/report\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-table-report\",\"id\":\"1405299504554487810\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportType\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表类型管理\"},{\"icon\":\"icon-data\",\"id\":\"1405299619029626882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportDatasource\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"数据源管理\"},{\"icon\":\"icon-table\",\"id\":\"1405300363866714113\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表模板管理\"},{\"icon\":\"icon-excel\",\"id\":\"1626461319411445762\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/onlineTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"协同文档管理\"},{\"icon\":\"icon-word\",\"id\":\"1786056695102541825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/docTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"word模板管理\"},{\"icon\":\"icon-full-screen\",\"id\":\"1421971956013498370\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/screenTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"大屏模板管理\"},{\"icon\":\"icon-eyes\",\"id\":\"1544474827944828930\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/viewReport\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表查看\"},{\"icon\":\"icon-multi-rectangle\",\"id\":\"1534817266438144001\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/multiScreen\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"多大屏管理\"}],\"title\":\"报表\"},{\"icon\":\"icon-system\",\"id\":\"1405315562552209410\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/system\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-peoples\",\"id\":\"1405466770491432962\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysUser\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"用户管理\"},{\"icon\":\"icon-user\",\"id\":\"1405466871968423938\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysRole\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"角色管理\"},{\"icon\":\"icon-music-list\",\"id\":\"1405466972459753473\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMenu\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"菜单管理\"},{\"icon\":\"icon-user-positioning\",\"id\":\"1738138171403386882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysPost\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"岗位管理\"},{\"icon\":\"icon-network-tree\",\"id\":\"1738779159977222145\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysDept\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"部门管理\"}],\"title\":\"系统管理\"},{\"icon\":\"icon-shop\",\"id\":\"1738480133297819649\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/merchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-permissions\",\"id\":\"1738480515709292545\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchantAuthTemplate\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"权限模板\"},{\"icon\":\"icon-user-business\",\"id\":\"1738480923462750209\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"租户管理\"}],\"title\":\"租户管理\"},{\"icon\":\"icon-like\",\"id\":\"1567288878307405825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/iconPark\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"图标\"}],\"url\":\"\"}', '127.0.0.1', '49', 1404697301888294925, '2024-06-25 11:42:55', NULL, '2024-06-25 11:42:55', 1);
INSERT INTO `operate_log` VALUES (1805446571706990594, 1404697301888294925, 'admin', 'ReportType', 'Search', '获取页面表格数据', 'com.springreport.controller.reporttype.ReportTypeController.getTableList', '{\"offSet\":0,\"pageSize\":10,\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:43:01', 1, '请求/springReport/api/reportType/getTableList调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"currentPage\":1,\"data\":[],\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\"},\"url\":\"\"}执行时长:88ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"currentPage\":1,\"data\":[],\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\"},\"url\":\"\"}', '127.0.0.1', '88', 1404697301888294925, '2024-06-25 11:43:01', NULL, '2024-06-25 11:43:01', 1);
INSERT INTO `operate_log` VALUES (1805446576475914241, 1404697301888294925, 'admin', 'ReportDatasource', 'Search', '获取页面表格数据', 'com.springreport.controller.reportdatasource.ReportDatasourceController.getTableList', '{\"offSet\":0,\"pageSize\":10,\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:43:02', 1, '请求/springReport/api/reportDatasource/getTableList调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"currentPage\":1,\"data\":[],\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\"},\"url\":\"\"}执行时长:25ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"currentPage\":1,\"data\":[],\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\"},\"url\":\"\"}', '127.0.0.1', '25', 1404697301888294925, '2024-06-25 11:43:02', NULL, '2024-06-25 11:43:02', 1);
INSERT INTO `operate_log` VALUES (1805446579009273857, 1404697301888294925, 'admin', 'ReportTpl', 'Search', '获取页面表格数据', 'com.springreport.controller.reporttpl.ReportTplController.getTableList', '{\"offSet\":0,\"pageSize\":10,\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:43:03', 1, '请求/springReport/api/reportTpl/getTableList调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"currentPage\":1,\"data\":[],\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\"},\"url\":\"\"}执行时长:46ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"currentPage\":1,\"data\":[],\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\"},\"url\":\"\"}', '127.0.0.1', '46', 1404697301888294925, '2024-06-25 11:43:03', NULL, '2024-06-25 11:43:03', 1);
INSERT INTO `operate_log` VALUES (1805446580141735937, 1404697301888294925, 'admin', 'ReportType', 'Search', '获取报表类型', 'com.springreport.controller.reporttype.ReportTypeController.getReportType', '{\"offSet\":0,\"pageSize\":10,\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:43:03', 1, '请求/springReport/api/reportType/getReportType调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[],\"url\":\"\"}执行时长:3ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[],\"url\":\"\"}', '127.0.0.1', '3', 1404697301888294925, '2024-06-25 11:43:03', NULL, '2024-06-25 11:43:03', 1);
INSERT INTO `operate_log` VALUES (1805446580200456194, 1404697301888294925, 'admin', 'ReportType', 'Search', '获取报表类型树', 'com.springreport.controller.reporttype.ReportTypeController.getReportTypeTree', '{\"offSet\":0,\"pageSize\":10,\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:43:03', 1, '请求/springReport/api/reportType/getReportTypeTree调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"children\":[],\"id\":\"1\",\"label\":\"报表分类\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"statusCode\":\"200\",\"statusMsg\":\"\"}],\"url\":\"\"}执行时长:10ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"children\":[],\"id\":\"1\",\"label\":\"报表分类\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"statusCode\":\"200\",\"statusMsg\":\"\"}],\"url\":\"\"}', '127.0.0.1', '10', 1404697301888294925, '2024-06-25 11:43:03', NULL, '2024-06-25 11:43:03', 1);
INSERT INTO `operate_log` VALUES (1805446580460503042, 1404697301888294925, 'admin', 'ReportDatasource', 'Search', '获取数据源', 'com.springreport.controller.reportdatasource.ReportDatasourceController.getReportDatasource', '{\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:43:03', 1, '请求/springReport/api/reportDatasource/getReportDatasource调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[],\"url\":\"\"}执行时长:9ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[],\"url\":\"\"}', '127.0.0.1', '9', 1404697301888294925, '2024-06-25 11:43:03', NULL, '2024-06-25 11:43:03', 1);
INSERT INTO `operate_log` VALUES (1805446855199997955, NULL, NULL, 'Login', 'Update', '用户登录', 'com.springreport.controller.login.LoginController.doLogin', '{\"offSet\":0,\"pageSize\":10,\"password\":\"e10adc3949ba59abbe56e057f20f883e\",\"userName\":\"madmin\",\"currentPage\":1,\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:44:09', 1, '请求/springReport/api/login/doLogin调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"apis\":[\"reportType_search\",\"reportType_insert\",\"reportType_update\",\"reportType_getDetail\",\"reportType_delete\",\"reportType_batchDelete\",\"reportDatasource_search\",\"reportDatasource_insert\",\"reportDatasource_update\",\"reportDatasource_getDetail\",\"reportDatasource_delete\",\"reportDatasource_batchDelete\",\"reportTpl_search\",\"reportTpl_insert\",\"reportTpl_update\",\"reportTpl_getDetail\",\"reportTpl_delete\",\"reportTpl_batchDelete\",\"reportTpl_reportDesign\",\"sysUser_search\",\"sysUser_insert\",\"sysUser_update\",\"sysUser_getDetail\",\"sysUser_delete\",\"sysUser_batchDelete\",\"sysRole_search\",\"sysRole_insert\",\"sysRole_update\",\"sysRole_getDetail\",\"sysRole_delete\",\"sysRole_batchDelete\",\"sysMenu_search\",\"sysMenu_insert\",\"sysMenu_update\",\"sysMenu_getDetail\",\"sysMenu_delete\",\"sysMenu_batchDelete\",\"sysApi_search\",\"sysApi_insert\",\"sysApi_update\",\"sysApi_getDetail\",\"sysApi_delete\",\"sysApi_batchDelete\",\"sysMenu_sysApi\",\"reportDesign_deleteDataSet\",\"reportDesign_addDataSet\",\"sysRole_authed\",\"roleReport_search\",\"roleReport_view\",\"screenTpl_search\",\"screenTpl_insert\",\"screenTpl_update\",\"screenTpl_getDetail\",\"screenTpl_delete\",\"screenTpl_deleteBatch\",\"screenTpl_screenDesign\",\"screenTpl_saveDesign\",\"printTpl_search\",\"printTpl_insert\",\"printTpl_update\",\"printTpl_getDetail\",\"printTpl_delete\",\"printTpl_batchDelete\",\"printTpl_design\",\"printTpl_saveDesign\",\"printTpl_print\",\"screenTpl_refresh\",\"screenTpl_content\",\"screenContent_refresh\",\"reportTpl_reportView\",\"reportDesign_saveLuckyTpl\",\"reportDesign_editDataSet\",\"reportDesign_previewReport\",\"screenTpl_viewScreen\",\"screenTpl_previewDesign\",\"multiScreen_search\",\"multiScreen_insert\",\"multiScreen_update\",\"multiScreen_delete\",\"multiScreen_deleteBatch\",\"multiScreen_preview\",\"multiScreen_view\",\"reportTpl_changePwd\",\"viewReport_Search\",\"viewReport_view\",\"reportForms_addDataSet\",\"reportForms_editDataSet\",\"reportForms_deleteDataSet\",\"reportForms_saveTpl\",\"reportForms_previewReport\",\"reportForms_ReportData\",\"reportDatasource_dict\",\"reportDatasourceDictType_search\",\"reportDatasourceDictType_insert\",\"reportDatasourceDictType_update\",\"reportDatasourceDictType_getDetail\",\"reportDatasourceDictType_delete\",\"reportDatasourceDictType_dictData\",\"reportDatasourceDictData_search\",\"reportDatasourceDictData_insert\",\"reportDatasourceDictData_update\",\"reportDatasourceDictData_getDetail\",\"reportDatasourceDictData_delete\",\"reportTpl_copy\",\"screenTpl_copy\",\"onlineTpl_search\",\"onlineTpl_insert\",\"onlineTpl_update\",\"onlineTpl_getDetai\",\"onlineTpl_delete\",\"onlineTpl_editDoc\",\"reportTpl_reportShare\",\"reportTask_search\",\"reportTask_detail\",\"reportTask_edit\",\"reportTask_delete\",\"reportTask_batchdelete\",\"reportTask_runTask\",\"reportTask_pause\",\"reportTask_resume\",\"reportTask_insert\",\"reportTpl_Task\",\"sysPost_search\",\"sysPost_insert\",\"sysPost_delete\",\"sysPost_getDetail\",\"sysPost_edit\",\"sysDept_search\",\"sysDept_insert\",\"sysDept_getDetail\",\"sysDept_edit\",\"sysDept_delete\",\"sysMerchantAuth_search\",\"sysMerchantAuth_insert\",\"sysMerchantAuth_getDetail\",\"sysMerchantAuth_edit\",\"sysMerchantAuth_delete\",\"sysMerchant_search\",\"sysMerchant_insert\",\"sysMerchant_delete\",\"sysMerchant_getDetail\",\"sysMerchant_edit\",\"sysUser_resetPwd\",\"docTpl_search\",\"docTpl_getDetail\",\"docTpl_insert\",\"docTpl_edit\",\"docTpl_delete\",\"docTpl_design\",\"docTpl_view\",\"docTpl_save\",\"docTpl_preview\",\"docTpl_export\"],\"isAdmin\":1,\"isSystemMerchant\":1,\"merchantMode\":1,\"merchantNo\":\"SR00000000\",\"roleName\":\"管理员\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc1N5c3RlbU1lcmNoYW50IjoxLCJ1c2VyUmVhbE5hbWUiOiJtYWRtaW4iLCJtZXJjaGFudE1vZGUiOjEsInJvbGVOYW1lIjoi566h55CG5ZGYIiwiaXNBZG1pbiI6MSwidXNlck5hbWUiOiJtYWRtaW4iLCJleHAiOjE3MTkzNzM0NDgsInVzZXJJZCI6MTQwNDY5NzMwMTg4ODI5NDkyNSwibWVyY2hhbnRObyI6IlNSMDAwMDAwMDAifQ.jgO5d_etyXDyiv15HWCeUv8iXOrS_MSSW0ltClego-jolTcrNqLfFkcRJ4oPtrgE-SaNSmFWhNEssqnO2yX7NA\",\"userId\":\"1404697301888294925\",\"userName\":\"madmin\",\"userRealName\":\"madmin\"},\"url\":\"\"}执行时长:38ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":{\"apis\":[\"reportType_search\",\"reportType_insert\",\"reportType_update\",\"reportType_getDetail\",\"reportType_delete\",\"reportType_batchDelete\",\"reportDatasource_search\",\"reportDatasource_insert\",\"reportDatasource_update\",\"reportDatasource_getDetail\",\"reportDatasource_delete\",\"reportDatasource_batchDelete\",\"reportTpl_search\",\"reportTpl_insert\",\"reportTpl_update\",\"reportTpl_getDetail\",\"reportTpl_delete\",\"reportTpl_batchDelete\",\"reportTpl_reportDesign\",\"sysUser_search\",\"sysUser_insert\",\"sysUser_update\",\"sysUser_getDetail\",\"sysUser_delete\",\"sysUser_batchDelete\",\"sysRole_search\",\"sysRole_insert\",\"sysRole_update\",\"sysRole_getDetail\",\"sysRole_delete\",\"sysRole_batchDelete\",\"sysMenu_search\",\"sysMenu_insert\",\"sysMenu_update\",\"sysMenu_getDetail\",\"sysMenu_delete\",\"sysMenu_batchDelete\",\"sysApi_search\",\"sysApi_insert\",\"sysApi_update\",\"sysApi_getDetail\",\"sysApi_delete\",\"sysApi_batchDelete\",\"sysMenu_sysApi\",\"reportDesign_deleteDataSet\",\"reportDesign_addDataSet\",\"sysRole_authed\",\"roleReport_search\",\"roleReport_view\",\"screenTpl_search\",\"screenTpl_insert\",\"screenTpl_update\",\"screenTpl_getDetail\",\"screenTpl_delete\",\"screenTpl_deleteBatch\",\"screenTpl_screenDesign\",\"screenTpl_saveDesign\",\"printTpl_search\",\"printTpl_insert\",\"printTpl_update\",\"printTpl_getDetail\",\"printTpl_delete\",\"printTpl_batchDelete\",\"printTpl_design\",\"printTpl_saveDesign\",\"printTpl_print\",\"screenTpl_refresh\",\"screenTpl_content\",\"screenContent_refresh\",\"reportTpl_reportView\",\"reportDesign_saveLuckyTpl\",\"reportDesign_editDataSet\",\"reportDesign_previewReport\",\"screenTpl_viewScreen\",\"screenTpl_previewDesign\",\"multiScreen_search\",\"multiScreen_insert\",\"multiScreen_update\",\"multiScreen_delete\",\"multiScreen_deleteBatch\",\"multiScreen_preview\",\"multiScreen_view\",\"reportTpl_changePwd\",\"viewReport_Search\",\"viewReport_view\",\"reportForms_addDataSet\",\"reportForms_editDataSet\",\"reportForms_deleteDataSet\",\"reportForms_saveTpl\",\"reportForms_previewReport\",\"reportForms_ReportData\",\"reportDatasource_dict\",\"reportDatasourceDictType_search\",\"reportDatasourceDictType_insert\",\"reportDatasourceDictType_update\",\"reportDatasourceDictType_getDetail\",\"reportDatasourceDictType_delete\",\"reportDatasourceDictType_dictData\",\"reportDatasourceDictData_search\",\"reportDatasourceDictData_insert\",\"reportDatasourceDictData_update\",\"reportDatasourceDictData_getDetail\",\"reportDatasourceDictData_delete\",\"reportTpl_copy\",\"screenTpl_copy\",\"onlineTpl_search\",\"onlineTpl_insert\",\"onlineTpl_update\",\"onlineTpl_getDetai\",\"onlineTpl_delete\",\"onlineTpl_editDoc\",\"reportTpl_reportShare\",\"reportTask_search\",\"reportTask_detail\",\"reportTask_edit\",\"reportTask_delete\",\"reportTask_batchdelete\",\"reportTask_runTask\",\"reportTask_pause\",\"reportTask_resume\",\"reportTask_insert\",\"reportTpl_Task\",\"sysPost_search\",\"sysPost_insert\",\"sysPost_delete\",\"sysPost_getDetail\",\"sysPost_edit\",\"sysDept_search\",\"sysDept_insert\",\"sysDept_getDetail\",\"sysDept_edit\",\"sysDept_delete\",\"sysMerchantAuth_search\",\"sysMerchantAuth_insert\",\"sysMerchantAuth_getDetail\",\"sysMerchantAuth_edit\",\"sysMerchantAuth_delete\",\"sysMerchant_search\",\"sysMerchant_insert\",\"sysMerchant_delete\",\"sysMerchant_getDetail\",\"sysMerchant_edit\",\"sysUser_resetPwd\",\"docTpl_search\",\"docTpl_getDetail\",\"docTpl_insert\",\"docTpl_edit\",\"docTpl_delete\",\"docTpl_design\",\"docTpl_view\",\"docTpl_save\",\"docTpl_preview\",\"docTpl_export\"],\"isAdmin\":1,\"isSystemMerchant\":1,\"merchantMode\":1,\"merchantNo\":\"SR00000000\",\"roleName\":\"管理员\",\"token\":\"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc1N5c3RlbU1lcmNoYW50IjoxLCJ1c2VyUmVhbE5hbWUiOiJtYWRtaW4iLCJtZXJjaGFudE1vZGUiOjEsInJvbGVOYW1lIjoi566h55CG5ZGYIiwiaXNBZG1pbiI6MSwidXNlck5hbWUiOiJtYWRtaW4iLCJleHAiOjE3MTkzNzM0NDgsInVzZXJJZCI6MTQwNDY5NzMwMTg4ODI5NDkyNSwibWVyY2hhbnRObyI6IlNSMDAwMDAwMDAifQ.jgO5d_etyXDyiv15HWCeUv8iXOrS_MSSW0ltClego-jolTcrNqLfFkcRJ4oPtrgE-SaNSmFWhNEssqnO2yX7NA\",\"userId\":\"1404697301888294925\",\"userName\":\"madmin\",\"userRealName\":\"madmin\"},\"url\":\"\"}', '0:0:0:0:0:0:0:1', '38', NULL, '2024-06-25 11:44:09', NULL, '2024-06-25 11:44:09', 1);
INSERT INTO `operate_log` VALUES (1805446857808855041, 1404697301888294925, 'madmin', 'SysMerchant', 'Search', '获取所有的商户列表', 'com.springreport.controller.sysmerchant.SysMerchantController.getMerchantList', '[]', '2024-06-25 11:44:09', 1, '请求/springReport/api/sysMerchant/getMerchantList调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824130\",\"isSystemMerchant\":1,\"merchantName\":\"系统默认商户\",\"merchantNo\":\"SR00000000\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111112\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703388920000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824131\",\"isSystemMerchant\":2,\"merchantName\":\"测试商户\",\"merchantNo\":\"SR00000001\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111111\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703390521000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"createTime\":1703391173000,\"creator\":\"1404697301888294915\",\"currentPage\":1,\"delFlag\":1,\"email\":\"sdfa@1212.com\",\"id\":\"1738774719438217218\",\"isSystemMerchant\":2,\"merchantName\":\"测试2\",\"merchantNo\":\"SR00000002\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"12354141426\",\"status\":2,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703464856000,\"updater\":\"1404697301888294915\"}],\"url\":\"\"}执行时长:3ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824130\",\"isSystemMerchant\":1,\"merchantName\":\"系统默认商户\",\"merchantNo\":\"SR00000000\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111112\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703388920000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824131\",\"isSystemMerchant\":2,\"merchantName\":\"测试商户\",\"merchantNo\":\"SR00000001\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111111\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703390521000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"createTime\":1703391173000,\"creator\":\"1404697301888294915\",\"currentPage\":1,\"delFlag\":1,\"email\":\"sdfa@1212.com\",\"id\":\"1738774719438217218\",\"isSystemMerchant\":2,\"merchantName\":\"测试2\",\"merchantNo\":\"SR00000002\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"12354141426\",\"status\":2,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703464856000,\"updater\":\"1404697301888294915\"}],\"url\":\"\"}', '0:0:0:0:0:0:0:1', '3', 1404697301888294925, '2024-06-25 11:44:09', NULL, '2024-06-25 11:44:09', 1);
INSERT INTO `operate_log` VALUES (1805446857943072769, 1404697301888294925, 'madmin', 'SysMenu', 'Search', '获取首页菜单树', 'com.springreport.controller.sysmenu.SysMenuController.getIndexMenu', '{\"isSystemMerchant\":1,\"userRealName\":\"madmin\",\"merchantMode\":1,\"roleName\":\"管理员\",\"expireDate\":\"1719373448000\",\"isAdmin\":1,\"userName\":\"madmin\",\"userId\":\"1404697301888294925\",\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:44:09', 1, '请求/springReport/api/sysMenu/getIndexMenu调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"icon\":\"icon-chart-line\",\"id\":\"1404722852271263746\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/report\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-table-report\",\"id\":\"1405299504554487810\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportType\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表类型管理\"},{\"icon\":\"icon-data\",\"id\":\"1405299619029626882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportDatasource\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"数据源管理\"},{\"icon\":\"icon-table\",\"id\":\"1405300363866714113\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表模板管理\"},{\"icon\":\"icon-excel\",\"id\":\"1626461319411445762\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/onlineTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"协同文档管理\"},{\"icon\":\"icon-word\",\"id\":\"1786056695102541825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/docTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"word模板管理\"},{\"icon\":\"icon-full-screen\",\"id\":\"1421971956013498370\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/screenTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"大屏模板管理\"},{\"icon\":\"icon-eyes\",\"id\":\"1544474827944828930\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/viewReport\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表查看\"},{\"icon\":\"icon-multi-rectangle\",\"id\":\"1534817266438144001\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/multiScreen\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"多大屏管理\"}],\"title\":\"报表\"},{\"icon\":\"icon-system\",\"id\":\"1405315562552209410\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/system\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-peoples\",\"id\":\"1405466770491432962\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysUser\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"用户管理\"},{\"icon\":\"icon-user\",\"id\":\"1405466871968423938\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysRole\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"角色管理\"},{\"icon\":\"icon-music-list\",\"id\":\"1405466972459753473\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMenu\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"菜单管理\"},{\"icon\":\"icon-user-positioning\",\"id\":\"1738138171403386882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysPost\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"岗位管理\"},{\"icon\":\"icon-network-tree\",\"id\":\"1738779159977222145\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysDept\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"部门管理\"}],\"title\":\"系统管理\"},{\"icon\":\"icon-shop\",\"id\":\"1738480133297819649\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/merchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-permissions\",\"id\":\"1738480515709292545\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchantAuthTemplate\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"权限模板\"},{\"icon\":\"icon-user-business\",\"id\":\"1738480923462750209\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"租户管理\"}],\"title\":\"租户管理\"},{\"icon\":\"icon-like\",\"id\":\"1567288878307405825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/iconPark\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"图标\"}],\"url\":\"\"}执行时长:23ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"icon\":\"icon-chart-line\",\"id\":\"1404722852271263746\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/report\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-table-report\",\"id\":\"1405299504554487810\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportType\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表类型管理\"},{\"icon\":\"icon-data\",\"id\":\"1405299619029626882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportDatasource\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"数据源管理\"},{\"icon\":\"icon-table\",\"id\":\"1405300363866714113\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表模板管理\"},{\"icon\":\"icon-excel\",\"id\":\"1626461319411445762\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/onlineTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"协同文档管理\"},{\"icon\":\"icon-word\",\"id\":\"1786056695102541825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/docTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"word模板管理\"},{\"icon\":\"icon-full-screen\",\"id\":\"1421971956013498370\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/screenTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"大屏模板管理\"},{\"icon\":\"icon-eyes\",\"id\":\"1544474827944828930\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/viewReport\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表查看\"},{\"icon\":\"icon-multi-rectangle\",\"id\":\"1534817266438144001\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/multiScreen\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"多大屏管理\"}],\"title\":\"报表\"},{\"icon\":\"icon-system\",\"id\":\"1405315562552209410\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/system\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-peoples\",\"id\":\"1405466770491432962\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysUser\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"用户管理\"},{\"icon\":\"icon-user\",\"id\":\"1405466871968423938\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysRole\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"角色管理\"},{\"icon\":\"icon-music-list\",\"id\":\"1405466972459753473\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMenu\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"菜单管理\"},{\"icon\":\"icon-user-positioning\",\"id\":\"1738138171403386882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysPost\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"岗位管理\"},{\"icon\":\"icon-network-tree\",\"id\":\"1738779159977222145\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysDept\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"部门管理\"}],\"title\":\"系统管理\"},{\"icon\":\"icon-shop\",\"id\":\"1738480133297819649\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/merchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-permissions\",\"id\":\"1738480515709292545\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchantAuthTemplate\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"权限模板\"},{\"icon\":\"icon-user-business\",\"id\":\"1738480923462750209\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"租户管理\"}],\"title\":\"租户管理\"},{\"icon\":\"icon-like\",\"id\":\"1567288878307405825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/iconPark\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"图标\"}],\"url\":\"\"}', '0:0:0:0:0:0:0:1', '23', 1404697301888294925, '2024-06-25 11:44:09', NULL, '2024-06-25 11:44:09', 1);
INSERT INTO `operate_log` VALUES (1805447054752399361, 1404697301888294925, 'madmin', 'SysMerchant', 'Search', '获取所有的商户列表', 'com.springreport.controller.sysmerchant.SysMerchantController.getMerchantList', '[]', '2024-06-25 11:44:56', 1, '请求/springReport/api/sysMerchant/getMerchantList调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824130\",\"isSystemMerchant\":1,\"merchantName\":\"系统默认商户\",\"merchantNo\":\"SR00000000\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111112\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703388920000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824131\",\"isSystemMerchant\":2,\"merchantName\":\"测试商户\",\"merchantNo\":\"SR00000001\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111111\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703390521000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"createTime\":1703391173000,\"creator\":\"1404697301888294915\",\"currentPage\":1,\"delFlag\":1,\"email\":\"sdfa@1212.com\",\"id\":\"1738774719438217218\",\"isSystemMerchant\":2,\"merchantName\":\"测试2\",\"merchantNo\":\"SR00000002\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"12354141426\",\"status\":2,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703464856000,\"updater\":\"1404697301888294915\"}],\"url\":\"\"}执行时长:7ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824130\",\"isSystemMerchant\":1,\"merchantName\":\"系统默认商户\",\"merchantNo\":\"SR00000000\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111112\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703388920000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"currentPage\":1,\"delFlag\":1,\"email\":\"1212@123.com\",\"id\":\"1542700042843824131\",\"isSystemMerchant\":2,\"merchantName\":\"测试商户\",\"merchantNo\":\"SR00000001\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"11111111111\",\"status\":1,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703390521000,\"updater\":\"1404697301888294915\"},{\"authTemplate\":\"1738526339965362177\",\"createTime\":1703391173000,\"creator\":\"1404697301888294915\",\"currentPage\":1,\"delFlag\":1,\"email\":\"sdfa@1212.com\",\"id\":\"1738774719438217218\",\"isSystemMerchant\":2,\"merchantName\":\"测试2\",\"merchantNo\":\"SR00000002\",\"msgLevel\":\"success\",\"offSet\":0,\"orderSql\":\"\",\"pageSize\":10,\"phone\":\"12354141426\",\"status\":2,\"statusCode\":\"200\",\"statusMsg\":\"\",\"total\":\"0\",\"updateTime\":1703464856000,\"updater\":\"1404697301888294915\"}],\"url\":\"\"}', '0:0:0:0:0:0:0:1', '7', 1404697301888294925, '2024-06-25 11:44:56', NULL, '2024-06-25 11:44:56', 1);
INSERT INTO `operate_log` VALUES (1805447054819508225, 1404697301888294925, 'madmin', 'SysMenu', 'Search', '获取首页菜单树', 'com.springreport.controller.sysmenu.SysMenuController.getIndexMenu', '{\"isSystemMerchant\":1,\"userRealName\":\"madmin\",\"merchantMode\":1,\"roleName\":\"管理员\",\"expireDate\":\"1719373448000\",\"isAdmin\":1,\"userName\":\"madmin\",\"userId\":\"1404697301888294925\",\"merchantNo\":\"SR00000000\"}', '2024-06-25 11:44:56', 1, '请求/springReport/api/sysMenu/getIndexMenu调用结束，返回结果：{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"icon\":\"icon-chart-line\",\"id\":\"1404722852271263746\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/report\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-table-report\",\"id\":\"1405299504554487810\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportType\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表类型管理\"},{\"icon\":\"icon-data\",\"id\":\"1405299619029626882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportDatasource\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"数据源管理\"},{\"icon\":\"icon-table\",\"id\":\"1405300363866714113\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表模板管理\"},{\"icon\":\"icon-excel\",\"id\":\"1626461319411445762\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/onlineTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"协同文档管理\"},{\"icon\":\"icon-word\",\"id\":\"1786056695102541825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/docTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"word模板管理\"},{\"icon\":\"icon-eyes\",\"id\":\"1544474827944828930\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/viewReport\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表查看\"}],\"title\":\"报表\"},{\"icon\":\"icon-system\",\"id\":\"1405315562552209410\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/system\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-peoples\",\"id\":\"1405466770491432962\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysUser\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"用户管理\"},{\"icon\":\"icon-user\",\"id\":\"1405466871968423938\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysRole\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"角色管理\"},{\"icon\":\"icon-music-list\",\"id\":\"1405466972459753473\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMenu\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"菜单管理\"},{\"icon\":\"icon-user-positioning\",\"id\":\"1738138171403386882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysPost\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"岗位管理\"},{\"icon\":\"icon-network-tree\",\"id\":\"1738779159977222145\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysDept\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"部门管理\"}],\"title\":\"系统管理\"},{\"icon\":\"icon-shop\",\"id\":\"1738480133297819649\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/merchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-permissions\",\"id\":\"1738480515709292545\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchantAuthTemplate\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"权限模板\"},{\"icon\":\"icon-user-business\",\"id\":\"1738480923462750209\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"租户管理\"}],\"title\":\"租户管理\"},{\"icon\":\"icon-like\",\"id\":\"1567288878307405825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/iconPark\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"图标\"}],\"url\":\"\"}执行时长:15ms', '{\"code\":\"200\",\"msgLevel\":\"success\",\"responseData\":[{\"icon\":\"icon-chart-line\",\"id\":\"1404722852271263746\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/report\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-table-report\",\"id\":\"1405299504554487810\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportType\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表类型管理\"},{\"icon\":\"icon-data\",\"id\":\"1405299619029626882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportDatasource\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"数据源管理\"},{\"icon\":\"icon-table\",\"id\":\"1405300363866714113\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/reportTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表模板管理\"},{\"icon\":\"icon-excel\",\"id\":\"1626461319411445762\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/onlineTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"协同文档管理\"},{\"icon\":\"icon-word\",\"id\":\"1786056695102541825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/docTpl\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"word模板管理\"},{\"icon\":\"icon-eyes\",\"id\":\"1544474827944828930\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/viewReport\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"报表查看\"}],\"title\":\"报表\"},{\"icon\":\"icon-system\",\"id\":\"1405315562552209410\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/system\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-peoples\",\"id\":\"1405466770491432962\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysUser\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"用户管理\"},{\"icon\":\"icon-user\",\"id\":\"1405466871968423938\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysRole\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"角色管理\"},{\"icon\":\"icon-music-list\",\"id\":\"1405466972459753473\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMenu\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"菜单管理\"},{\"icon\":\"icon-user-positioning\",\"id\":\"1738138171403386882\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysPost\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"岗位管理\"},{\"icon\":\"icon-network-tree\",\"id\":\"1738779159977222145\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysDept\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"部门管理\"}],\"title\":\"系统管理\"},{\"icon\":\"icon-shop\",\"id\":\"1738480133297819649\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/merchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[{\"icon\":\"icon-permissions\",\"id\":\"1738480515709292545\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchantAuthTemplate\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"权限模板\"},{\"icon\":\"icon-user-business\",\"id\":\"1738480923462750209\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/sysMerchant\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"租户管理\"}],\"title\":\"租户管理\"},{\"icon\":\"icon-like\",\"id\":\"1567288878307405825\",\"msgLevel\":\"success\",\"orderSql\":\"\",\"page\":true,\"path\":\"/iconPark\",\"statusCode\":\"200\",\"statusMsg\":\"\",\"subs\":[],\"title\":\"图标\"}],\"url\":\"\"}', '0:0:0:0:0:0:0:1', '15', 1404697301888294925, '2024-06-25 11:44:56', NULL, '2024-06-25 11:44:56', 1);

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `BLOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `CRON_EXPRESSION` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TRIG_INST_NAME`(`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY`(`SCHED_NAME`, `INSTANCE_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_FT_J_G`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_T_G`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_FT_TG`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_J_REQ_RECOVERY`(`SCHED_NAME`, `REQUESTS_RECOVERY`) USING BTREE,
  INDEX `IDX_QRTZ_J_GRP`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_report_detail
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_report_detail`;
CREATE TABLE `qrtz_report_detail`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `job_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务名称',
  `job_time_type` tinyint(4) NULL DEFAULT 2 COMMENT '任务执行时间类型 1指定时间 2cron表达式',
  `job_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务执行时间',
  `job_cron` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务执行时间(cron表达式)',
  `job_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '任务参数',
  `email` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '导出数据后发送邮箱，多个邮箱用;分割',
  `export_type` tinyint(4) NULL DEFAULT 1 COMMENT '导出类型 1excel 2pdf 3excel+pdf',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `INSTANCE_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `INT_PROP_1` int(11) NULL DEFAULT NULL,
  `INT_PROP_2` int(11) NULL DEFAULT NULL,
  `LONG_PROP_1` bigint(20) NULL DEFAULT NULL,
  `LONG_PROP_2` bigint(20) NULL DEFAULT NULL,
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL,
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `JOB_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `JOB_GROUP` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) NULL DEFAULT NULL,
  `PRIORITY` int(11) NULL DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) NULL DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) NULL DEFAULT NULL,
  `JOB_DATA` blob NULL,
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_J`(`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_JG`(`SCHED_NAME`, `JOB_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_C`(`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE,
  INDEX `IDX_QRTZ_T_G`(`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `IDX_QRTZ_T_STATE`(`SCHED_NAME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_STATE`(`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_N_G_STATE`(`SCHED_NAME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NEXT_FIRE_TIME`(`SCHED_NAME`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST`(`SCHED_NAME`, `TRIGGER_STATE`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_STATE`) USING BTREE,
  INDEX `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP`(`SCHED_NAME`, `MISFIRE_INSTR`, `NEXT_FIRE_TIME`, `TRIGGER_GROUP`, `TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_datasource
-- ----------------------------
DROP TABLE IF EXISTS `report_datasource`;
CREATE TABLE `report_datasource`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '编码',
  `name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '数据库类型 1mysql 2oracle 3 sqlserver 4api',
  `driver_class` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '驱动',
  `jdbc_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据库链接url',
  `user_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `api_columns_prefix` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性前缀',
  `api_columns` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'api返回列',
  `api_result_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'api返回类型 String Array ObjectArray Object',
  `api_request_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT 'post' COMMENT '请求方式 post get',
  `api_request_header` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '接口请求头',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_datasource_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `report_datasource_dict_data`;
CREATE TABLE `report_datasource_dict_data`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源id',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `sort` int(6) NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `merchant_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_datasource_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `report_datasource_dict_type`;
CREATE TABLE `report_datasource_dict_type`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源id',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '字典类型',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `merchant_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商户号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_forms_datasource
-- ----------------------------
DROP TABLE IF EXISTS `report_forms_datasource`;
CREATE TABLE `report_forms_datasource`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT '模板sheetid',
  `name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '名称',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源id',
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '填报绑定的数据源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_forms_datasource_attrs
-- ----------------------------
DROP TABLE IF EXISTS `report_forms_datasource_attrs`;
CREATE TABLE `report_forms_datasource_attrs`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `report_forms_datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '填报绑定数据源表主键',
  `type` tinyint(4) NULL DEFAULT 1 COMMENT '列类型 1关联单元格列 2主键列',
  `coordsx` int(11) NULL DEFAULT NULL COMMENT '单元格横坐标',
  `coordsy` int(11) NULL DEFAULT NULL COMMENT '单元格纵坐标',
  `cell_coords` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '单元格原始坐标',
  `column_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源列名',
  `id_type` tinyint(4) NULL DEFAULT 1 COMMENT '主键生成规则 1 自定义填写 2雪花算法 3自增主键',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '填报绑定的数据源属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_range_auth
-- ----------------------------
DROP TABLE IF EXISTS `report_range_auth`;
CREATE TABLE `report_range_auth`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT 'sheet页id',
  `sheet_index` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'sheet页唯一标识',
  `range_txt` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '范围 如A1:C1',
  `rows_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `cols_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `type` tinyint(4) NULL DEFAULT 1 COMMENT '类型 1报表 2协同文档',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_range_auth_user
-- ----------------------------
DROP TABLE IF EXISTS `report_range_auth_user`;
CREATE TABLE `report_range_auth_user`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT 'sheet页id',
  `range_auth_id` bigint(20) NULL DEFAULT NULL COMMENT '授权范围id',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `type` tinyint(4) NULL DEFAULT 1 COMMENT '类型 1报表 2协同文档',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `auth_type` tinyint(4) NULL DEFAULT 1 COMMENT '授权类型 1编辑权限 2不允许查看',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_sheet_pdf_print_setting
-- ----------------------------
DROP TABLE IF EXISTS `report_sheet_pdf_print_setting`;
CREATE TABLE `report_sheet_pdf_print_setting`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `tpl_sheet_id` bigint(20) NULL DEFAULT NULL COMMENT '模板sheeti d',
  `page_type` tinyint(4) NULL DEFAULT 2 COMMENT '纸张类型 1 A3 2 A4 3 A5 4 A6 5 B2 6B3 7B4 8 B5 9LETTER 10 LEGAL',
  `page_layout` tinyint(4) NULL DEFAULT 1 COMMENT '纸张布局 1纵向 2横向',
  `page_header_show` tinyint(4) NULL DEFAULT 2 COMMENT '页眉是否显示 1是 2否',
  `page_header_content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '页眉显示内容',
  `page_header_position` tinyint(4) NULL DEFAULT 3 COMMENT '页眉显示位置 1左 2中 3右',
  `water_mark_show` tinyint(4) NULL DEFAULT 2 COMMENT '水印是否显示 1是 2否',
  `water_mark_type` tinyint(4) NULL DEFAULT 1 COMMENT '水印类型 1文本 2图片',
  `water_mark_content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '文本水印内容',
  `water_mark_img` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片水印url',
  `water_mark_opacity` float(2, 1) NULL DEFAULT 0.4 COMMENT '水印透明度 大于0小于1的值',
  `page_show` tinyint(4) NULL DEFAULT 1 COMMENT '页码是否显示 1是 2否',
  `page_position` tinyint(4) NULL DEFAULT 2 COMMENT '页码显示位置 1左 2中 3右',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(3) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `horizontal_page` tinyint(4) NULL DEFAULT 2 COMMENT '是否水平分页 1是 2否',
  `horizontal_page_column` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '分页列，多个列用,隔开',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_tpl
-- ----------------------------
DROP TABLE IF EXISTS `report_tpl`;
CREATE TABLE `report_tpl`  (
  `id` bigint(20) NOT NULL,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `tpl_code` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板标识',
  `tpl_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `tpl_type` tinyint(4) NULL DEFAULT 1 COMMENT '报表类型 1列表式报表 2详情式报表',
  `report_type` bigint(20) NULL DEFAULT NULL COMMENT '报表类型',
  `is_index` tinyint(4) NULL DEFAULT 2 COMMENT '是否显示行号1是 2否',
  `is_param_merge` tinyint(4) NULL DEFAULT 2 COMMENT '数据集参数是否合并 1是 2否',
  `config` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'luckysheet行高，列宽等额外配置',
  `frozen` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '冻结属性',
  `images` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片',
  `view_auth` tinyint(4) NULL DEFAULT 1 COMMENT '查看权限 1所有人可见 2指定角色',
  `design_pwd` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '设计密码',
  `export_encrypt` tinyint(4) NULL DEFAULT 2 COMMENT '导出是否加密 1是 2否',
  `sheet_index` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sheet页唯一索引',
  `calc_chain` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '计算链，有公式的单元格信息',
  `concurrency_flag` tinyint(4) NULL DEFAULT 2 COMMENT '并发控制 1是 2否',
  `show_toolbar` tinyint(4) NULL DEFAULT 2 COMMENT '预览是否展示工具栏 1是 2否',
  `show_row_header` tinyint(4) NULL DEFAULT 1 COMMENT '预览是否显示行标题1是 2否',
  `show_col_header` tinyint(4) NULL DEFAULT 1 COMMENT '预览是否显示列标题 1是 2否',
  `show_gridlines` tinyint(4) NULL DEFAULT 1 COMMENT '预览是否显示网格线 1是 2否',
  `refresh_page` tinyint(4) NULL DEFAULT 2 COMMENT '上报数据后是否刷新页面 1是 2否',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `coedit_flag` tinyint(4) NULL DEFAULT 1 COMMENT '开启协同 1是 2否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_tpl_dataset
-- ----------------------------
DROP TABLE IF EXISTS `report_tpl_dataset`;
CREATE TABLE `report_tpl_dataset`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `dataset_type` tinyint(4) NULL DEFAULT 1 COMMENT '数据集类型 1sql 2api',
  `dataset_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集名称',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源id',
  `tpl_sql` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'sql语句',
  `tpl_param` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '参数',
  `sql_type` tinyint(4) NULL DEFAULT 1 COMMENT 'sql类型 1标准sql 2存储过程',
  `in_param` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '存储过程入参',
  `out_param` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '存储过程出参',
  `is_pagination` tinyint(4) NULL DEFAULT 2 COMMENT '是否分页 1是 2否',
  `page_count` int(11) NULL DEFAULT NULL COMMENT '每页显示条数',
  `type` tinyint(4) NULL DEFAULT 1 COMMENT '类型 1报表 2大屏',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_tpl_datasource
-- ----------------------------
DROP TABLE IF EXISTS `report_tpl_datasource`;
CREATE TABLE `report_tpl_datasource`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `datasource_id` bigint(20) NULL DEFAULT NULL COMMENT '数据源id',
  `type` tinyint(4) NULL DEFAULT 1 COMMENT '类型 1报表 2大屏',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_tpl_sheet
-- ----------------------------
DROP TABLE IF EXISTS `report_tpl_sheet`;
CREATE TABLE `report_tpl_sheet`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sheet名称',
  `sheet_order` int(6) NULL DEFAULT NULL COMMENT 'sheet顺序',
  `config` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT 'luckysheet行高，列宽等额外配置',
  `frozen` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '冻结属性',
  `images` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图片',
  `sheet_index` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'sheet页唯一索引',
  `calc_chain` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '计算链，有公式的单元格信息',
  `alternateformat_save` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '登录名',
  `chart` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '图表配置',
  `data_verification` longtext CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '数据校验项',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `xxbt_screenshot` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '斜线表头截图',
  `page_divider` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '分页线配置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_tpl_sheet_chart
-- ----------------------------
DROP TABLE IF EXISTS `report_tpl_sheet_chart`;
CREATE TABLE `report_tpl_sheet_chart`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT 'sheetid',
  `chart_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图表标识',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图表标题',
  `data_type` tinyint(4) NULL DEFAULT 1 COMMENT '数据来源 1自定义 2数据集',
  `dataset_id` bigint(20) NULL DEFAULT NULL COMMENT '数据集id',
  `dataset_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据集名称',
  `attr` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '属性',
  `xaxis_datas` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义值',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for report_type
-- ----------------------------
DROP TABLE IF EXISTS `report_type`;
CREATE TABLE `report_type`  (
  `id` bigint(20) NOT NULL,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `report_type_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报表类型名称',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_api`;
CREATE TABLE `sys_api`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单表主键',
  `api_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `api_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限名称',
  `api_function` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限描述',
  `sort` int(6) NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_api
-- ----------------------------
INSERT INTO `sys_api` VALUES (1405301141893328897, 1405299504554487810, 'reportType_search', '查询/重置', '查询列表数据', 1, 1404697301888294914, '2021-06-17 07:07:56', 0, NULL, 1);
INSERT INTO `sys_api` VALUES (1405301314702848002, 1405299504554487810, 'reportType_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-06-17 07:08:37', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405301498308505602, 1405299504554487810, 'reportType_update', '更新', '更新数据', 3, 1404697301888294914, '2021-06-17 07:09:21', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405301717527998466, 1405299504554487810, 'reportType_getDetail', '查看', '查看数据', 4, 1404697301888294914, '2021-06-17 07:10:13', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405301858293035009, 1405299504554487810, 'reportType_delete', '删除', '删除数据', 5, 1404697301888294914, '2021-06-17 07:10:47', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405302046789251074, 1405299504554487810, 'reportType_batchDelete', '批量删除', '批量删除数据', 6, 1404697301888294914, '2021-06-17 07:11:32', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405303174180417538, 1405299619029626882, 'reportDatasource_search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2021-06-17 07:16:01', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405303507476590594, 1405299619029626882, 'reportDatasource_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-06-17 07:17:20', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405303613030445057, 1405299619029626882, 'reportDatasource_update', '更新', '更新数据', 3, 1404697301888294914, '2021-06-17 07:17:45', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405303824343674881, 1405299619029626882, 'reportDatasource_getDetail', '查看', '查看数据', 4, 1404697301888294914, '2021-06-17 07:18:36', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405303972910116865, 1405299619029626882, 'reportDatasource_delete', '删除', '单条删除数据', 5, 1404697301888294914, '2021-06-17 07:19:11', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405304198198767618, 1405299619029626882, 'reportDatasource_batchDelete', '批量删除', '批量删除数据', 6, 1404697301888294914, '2021-06-17 07:20:05', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405304751960141826, 1405300363866714113, 'reportTpl_search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2021-06-17 07:22:17', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405304871174844417, 1405300363866714113, 'reportTpl_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-06-17 07:22:45', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405305048585515009, 1405300363866714113, 'reportTpl_update', '更新', '更新数据', 3, 1404697301888294914, '2021-06-17 07:23:28', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405305203900592129, 1405300363866714113, 'reportTpl_getDetail', '查看', '查看数据', 4, 1404697301888294914, '2021-06-17 07:24:05', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405305300512190465, 1405300363866714113, 'reportTpl_delete', '删除', '单条删除数据', 5, 1404697301888294914, '2021-06-17 07:24:28', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405305445274398722, 1405300363866714113, 'reportTpl_batchDelete', '批量删除', '批量删除数据', 6, 1404697301888294914, '2021-06-17 07:25:02', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405311658913476610, 1405300363866714113, 'reportTpl_reportDesign', '报表设计', '报表设计', 7, 1404697301888294914, '2021-06-17 07:49:44', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405467399616061442, 1405466770491432962, 'sysUser_search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2021-06-17 18:08:35', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405467490242387970, 1405466770491432962, 'sysUser_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-06-17 18:08:57', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405467569036582913, 1405466770491432962, 'sysUser_update', '更新', '更新数据', 3, 1404697301888294914, '2021-06-17 18:09:15', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405467677505478657, 1405466770491432962, 'sysUser_getDetail', '查看', '查看数据', 4, 1404697301888294914, '2021-06-17 18:09:41', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405467785726910466, 1405466770491432962, 'sysUser_delete', '删除', '单条删除', 5, 1404697301888294914, '2021-06-17 18:10:07', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405467907697270785, 1405466770491432962, 'sysUser_batchDelete', '批量删除', '批量删除数据', 6, 1404697301888294914, '2021-06-17 18:10:36', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468185620242433, 1405466871968423938, 'sysRole_search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2021-06-17 18:11:42', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468276355620865, 1405466871968423938, 'sysRole_insert', '新增', '新增数据', 3, 1404697301888294914, '2021-06-17 18:12:04', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468376880504833, 1405466871968423938, 'sysRole_update', '更新', '更新数据', 2, 1404697301888294914, '2021-06-17 18:12:28', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468475530534913, 1405466871968423938, 'sysRole_getDetail', '查看', '查看数据', 4, 1404697301888294914, '2021-06-17 18:12:52', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468567327072257, 1405466871968423938, 'sysRole_delete', '删除', '单条删除数据', 5, 1404697301888294914, '2021-06-17 18:13:13', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468667109564418, 1405466871968423938, 'sysRole_batchDelete', '批量删除', '批量删除数据', 6, 1404697301888294914, '2021-06-17 18:13:37', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468838434299906, 1405466972459753473, 'sysMenu_search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2021-06-17 18:14:18', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405468943841353729, 1405466972459753473, 'sysMenu_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-06-17 18:14:43', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405469064603754497, 1405466972459753473, 'sysMenu_update', '更新', '更新数据', 3, 1404697301888294914, '2021-06-17 18:15:12', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405469182782464002, 1405466972459753473, 'sysMenu_getDetail', '查看', '查看数据', 4, 1404697301888294914, '2021-06-17 18:15:40', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405469481353994241, 1405466972459753473, 'sysMenu_delete', '删除', '单条删除数据', 5, 1404697301888294914, '2021-06-17 18:16:51', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405469666599624706, 1405466972459753473, 'sysMenu_batchDelete', '批量删除', '批量删除数据', 6, 1404697301888294914, '2021-06-17 18:17:36', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405469928080924673, 1405467087488540673, 'sysApi_search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2021-06-17 18:18:38', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405470048491003905, 1405467087488540673, 'sysApi_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-06-17 18:19:07', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405470141055098881, 1405467087488540673, 'sysApi_update', '更新', '更新数据', 3, 1404697301888294914, '2021-06-17 18:19:29', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405470256549453826, 1405467087488540673, 'sysApi_getDetail', '查看', '查看数据', 4, 1404697301888294914, '2021-06-17 18:19:56', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405470347712651265, 1405467087488540673, 'sysApi_delete', '删除', '单条删除数据', 5, 1404697301888294914, '2021-06-17 18:20:18', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405470480063913986, 1405467087488540673, 'sysApi_batchDelete', '批量删除', '批量删除数据', 6, 1404697301888294914, '2021-06-17 18:20:49', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405470685538672641, 1405466972459753473, 'sysMenu_sysApi', '菜单功能', '菜单功能维护', 7, 1404697301888294914, '2021-06-17 18:21:38', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405473540714651649, 1405470840950218754, 'reportDesign_deleteDataSet', '删除数据集', '删除数据集', 3, 1404697301888294914, '2021-06-17 18:32:59', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405473959943725057, 1405470840950218754, 'reportDesign_addDataSet', '添加报表数据集', '添加报表数据集', 1, 1404697301888294914, '2021-06-17 18:34:39', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1405475787045355521, 1405466871968423938, 'sysRole_authed', '功能权限', '权限配置', 7, 1404697301888294914, '2021-06-17 18:41:55', 0, NULL, 1);
INSERT INTO `sys_api` VALUES (1407290957715492866, 1407276589648977921, 'roleReport_search', '查询/重置', '查询/重置', NULL, 1404697301888294914, '2021-06-22 18:54:45', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1407291121578561538, 1407276589648977921, 'roleReport_view', '查看报表', '查看报表', NULL, 1404697301888294914, '2021-06-22 18:55:24', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1424165862295486465, 1421971956013498370, 'screenTpl_search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2021-08-08 08:29:36', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1424165974212100098, 1421971956013498370, 'screenTpl_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-08-08 08:30:03', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1424221005015695362, 1421971956013498370, 'screenTpl_update', '编辑', '更新数据', 3, 1404697301888294914, '2021-08-08 12:08:43', 0, NULL, 1);
INSERT INTO `sys_api` VALUES (1424221114415726593, 1421971956013498370, 'screenTpl_getDetail', '查看', '查看详情', 4, 1404697301888294914, '2021-08-08 12:09:09', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1424221233877893122, 1421971956013498370, 'screenTpl_delete', '删除', '单条删除', 5, 1404697301888294914, '2021-08-08 12:09:38', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1424221352564113410, 1421971956013498370, 'screenTpl_deleteBatch', '批量删除', '批量删除', 6, 1404697301888294914, '2021-08-08 12:10:06', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1424221497850609666, 1421971956013498370, 'screenTpl_screenDesign', '大屏设计', '大屏设计', 7, 1404697301888294914, '2021-08-08 12:10:41', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1424221638846332929, 1424270599875547138, 'screenTpl_saveDesign', '保存大屏设计', '保存大屏设计', 1, 1404697301888294914, '2021-08-08 12:11:14', 0, NULL, 1);
INSERT INTO `sys_api` VALUES (1439113797250957314, 1437685292558557185, 'printTpl_search', '查询/重置', '查询列表数据', 2, 1404697301888294914, '2021-09-18 14:27:21', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1439113987001270273, 1437685292558557185, 'printTpl_insert', '新增', '新增数据', 2, 1404697301888294914, '2021-09-18 14:28:07', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1439114121332244481, 1437685292558557185, 'printTpl_update', '编辑', '编辑', 2, 1404697301888294914, '2021-09-18 14:28:39', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1439114339805151233, 1437685292558557185, 'printTpl_getDetail', '查看', '查看', 2, 1404697301888294914, '2021-09-18 14:29:31', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1439115046687981569, 1437685292558557185, 'printTpl_delete', '删除', '删除', 2, 1404697301888294914, '2021-09-18 14:32:19', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1439115157375664129, 1437685292558557185, 'printTpl_batchDelete', '批量删除', '批量删除', 2, 1404697301888294914, '2021-09-18 14:32:46', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1439115742380408834, 1437685292558557185, 'printTpl_design', '打印设计', '打印设计', 2, 1404697301888294914, '2021-09-18 14:35:05', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1439116359068925954, 1437685292558557185, 'printTpl_saveDesign', '保存模板', '保存模板', 2, 1404697301888294914, '2021-09-18 14:37:32', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1463063459183767554, 1437685292558557185, 'printTpl_print', '打印', '打印', 2, 1404697301888294914, '2021-11-23 16:34:46', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1465561208529735682, 1421971956013498370, 'screenTpl_refresh', '刷新', '刷新大屏页面', 8, 1404697301888294914, '2021-11-30 13:59:56', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1465572262567510018, 1421971956013498370, 'screenTpl_content', '大屏组件', '查看大屏组件', 9, 1404697301888294914, '2021-11-30 14:43:51', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1465605114222886913, 1465604831325470721, 'screenContent_refresh', '刷新组件', '刷新组件', NULL, 1404697301888294914, '2021-11-30 16:54:24', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1494555721453289474, 1405300363866714113, 'reportTpl_reportView', '报表查看', '报表查看', 8, 1404697301888294914, '2022-02-18 14:13:46', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1495702676325826561, 1405470840950218754, 'reportDesign_saveLuckyTpl', '保存', '保存模板', 4, 1404697301888294914, '2022-02-21 18:11:22', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542331800988078082, 1405470840950218754, 'reportDesign_editDataSet', '编辑数据集', '编辑数据集', 2, 1404697301888294914, '2022-06-30 10:18:51', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542332626318053378, 1405470840950218754, 'reportDesign_previewReport', '预览', '预览报表', 5, 1404697301888294914, '2022-06-30 10:22:08', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542348573812621314, 1421971956013498370, 'screenTpl_viewScreen', '查看大屏', '查看大屏', 10, 1404697301888294914, '2022-06-30 11:25:30', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542381974942810114, 1424270599875547138, 'screenTpl_previewDesign', '预览大屏设计', '预览大屏设计', 2, 1404697301888294914, '2022-06-30 13:38:14', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542403812838313985, 1534817266438144001, 'multiScreen_search', '查询', '查询', 1, 1404697301888294914, '2022-06-30 15:05:00', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542403987489132546, 1534817266438144001, 'multiScreen_insert', '新增', '新增数据', 2, 1404697301888294914, '2022-06-30 15:05:42', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542404100680814594, 1534817266438144001, 'multiScreen_update', '编辑', '更新数据', 3, 1404697301888294914, '2022-06-30 15:06:09', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542404196017344514, 1534817266438144001, 'multiScreen_delete', '删除', '单条删除', 4, 1404697301888294914, '2022-06-30 15:06:31', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542404196017344515, 1534817266438144001, 'multiScreen_deleteBatch', '批量删除', '批量删除', 5, 1404697301888294914, '2022-06-30 15:06:31', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542404571143311361, 1534817266438144001, 'multiScreen_preview', '多大屏预览', '多大屏预览', 6, 1404697301888294914, '2022-06-30 15:08:01', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1542404685916246017, 1534817266438144001, 'multiScreen_view', '多大屏查看', '多大屏查看', 7, 1404697301888294914, '2022-06-30 15:08:28', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1544272259952099329, 1405300363866714113, 'reportTpl_changePwd', '修改密码', '修改密码', 9, 1404697301888294914, '2022-07-05 18:49:33', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1546647225540489217, 1544474827944828930, 'viewReport_Search', '查询/重置', '查询表格数据', 1, 1404697301888294914, '2022-07-12 08:06:49', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1546647339193544705, 1544474827944828930, 'viewReport_view', '报表查看', '报表查看', 2, 1404697301888294914, '2022-07-12 08:07:16', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597387045241597954, 1597384623035232257, 'reportForms_addDataSet', '添加数据集', '添加数据集', 1, 1404697301888294914, '2022-11-29 08:28:44', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597387567000432642, 1597384623035232257, 'reportForms_editDataSet', '编辑数据集', '编辑数据集', 2, 1404697301888294914, '2022-11-29 08:30:49', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597387840951398402, 1597384623035232257, 'reportForms_deleteDataSet', '删除数据集', '删除数据集', 3, 1404697301888294914, '2022-11-29 08:31:54', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597388436018278402, 1597384623035232257, 'reportForms_saveTpl', '保存', '保存模板', 4, 1404697301888294914, '2022-11-29 08:34:16', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597389121065562113, 1597384623035232257, 'reportForms_previewReport', '预览', '预览填报报表', 5, 1404697301888294914, '2022-11-29 08:36:59', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597391476154675202, 1597389706904973314, 'reportForms_ReportData', '保存', '保存上报数据', 1, 1404697301888294914, '2022-11-29 08:46:21', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597393416263544833, 1405299619029626882, 'reportDatasource_dict', '数据字典', '数据字典', 7, 1404697301888294914, '2022-11-29 08:54:03', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597420289181855745, 1597419165079023618, 'reportDatasourceDictType_search', '查询/重置', '查询/重置', 1, 1404697301888294914, '2022-11-29 10:40:50', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597423867028025345, 1597419165079023618, 'reportDatasourceDictType_insert', '新增', '新增', 2, 1404697301888294914, '2022-11-29 10:55:03', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597424940950204417, 1597419165079023618, 'reportDatasourceDictType_update', '编辑', '编辑', 3, 1404697301888294914, '2022-11-29 10:59:19', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597425245129519106, 1597419165079023618, 'reportDatasourceDictType_getDetail', '查看', '查看', 4, 1404697301888294914, '2022-11-29 11:00:32', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597425770235408386, 1597419165079023618, 'reportDatasourceDictType_delete', '删除', '删除', 5, 1404697301888294914, '2022-11-29 11:02:37', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597426240341389313, 1597419165079023618, 'reportDatasourceDictType_dictData', '字典值', '字典值', 6, 1404697301888294914, '2022-11-29 11:04:29', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597427125343727617, 1597419308230619138, 'reportDatasourceDictData_search', '查询/重置', '查询/重置', 1, 1404697301888294914, '2022-11-29 11:08:00', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597427784797368321, 1597419308230619138, 'reportDatasourceDictData_insert', '新增', '新增', 2, 1404697301888294914, '2022-11-29 11:10:37', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597428241628377090, 1597419308230619138, 'reportDatasourceDictData_update', '编辑', '编辑', 3, 1404697301888294914, '2022-11-29 11:12:26', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597428289913204737, 1597419308230619138, 'reportDatasourceDictData_getDetail', '查看', '查看', 4, 1404697301888294914, '2022-11-29 11:12:38', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1597428633430896641, 1597419308230619138, 'reportDatasourceDictData_delete', '删除', '删除', 5, 1404697301888294914, '2022-11-29 11:14:00', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1600289405223759873, 1405300363866714113, 'reportTpl_copy', '复制', '复制', 10, 1404697301888294914, '2022-12-07 08:41:41', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1600289789489115138, 1421971956013498370, 'screenTpl_copy', '复制', '复制', 11, 1404697301888294914, '2022-12-07 08:43:12', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1626466088913936386, 1626461319411445762, 'onlineTpl_search', '查询/重置', '查询/重置', 1, 1404697301888294914, '2023-02-17 14:18:29', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1626466987442905090, 1626461319411445762, 'onlineTpl_insert', '新增', '新增', 2, 1404697301888294914, '2023-02-17 14:22:03', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1626467034335223809, 1626461319411445762, 'onlineTpl_update', '编辑', '编辑', 3, 1404697301888294914, '2023-02-17 14:22:14', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1626467097987981313, 1626461319411445762, 'onlineTpl_getDetai', '查看', '查看', 4, 1404697301888294914, '2023-02-17 14:22:29', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1626467157777784834, 1626461319411445762, 'onlineTpl_delete', '删除', '删除', 5, 1404697301888294914, '2023-02-17 14:22:43', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1626468015018024962, 1626461319411445762, 'onlineTpl_editDoc', '编辑文档', '编辑文档', 6, 1404697301888294914, '2023-02-17 14:26:08', NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1673335064480055297, 1405300363866714113, 'reportTpl_reportShare', '报表分享', '报表分享', 11, 1404697301888294914, '2023-06-26 22:19:03', NULL, '2023-06-26 22:19:03', 1);
INSERT INTO `sys_api` VALUES (1685829143013068801, 1685828895406526466, 'reportTask_search', '查询/重置', '查询/重置', 1, 1404697301888294914, '2023-07-31 09:46:04', NULL, '2023-07-31 09:46:04', 1);
INSERT INTO `sys_api` VALUES (1685831610387894273, 1685828895406526466, 'reportTask_detail', '查看', '查看', 2, 1404697301888294914, '2023-07-31 09:55:52', NULL, '2023-07-31 09:55:52', 1);
INSERT INTO `sys_api` VALUES (1685831690339717121, 1685828895406526466, 'reportTask_edit', '编辑', '编辑', 3, 1404697301888294914, '2023-07-31 09:56:11', NULL, '2023-07-31 09:56:11', 1);
INSERT INTO `sys_api` VALUES (1685831758522322946, 1685828895406526466, 'reportTask_delete', '删除', '删除', 4, 1404697301888294914, '2023-07-31 09:56:27', NULL, '2023-07-31 09:56:27', 1);
INSERT INTO `sys_api` VALUES (1685831822036668418, 1685828895406526466, 'reportTask_batchdelete', '批量删除', '批量删除', 5, 1404697301888294914, '2023-07-31 09:56:42', NULL, '2023-07-31 09:56:42', 1);
INSERT INTO `sys_api` VALUES (1685831971400028162, 1685828895406526466, 'reportTask_runTask', '立即执行', '立即执行', 6, 1404697301888294914, '2023-07-31 09:57:18', NULL, '2023-07-31 09:57:18', 1);
INSERT INTO `sys_api` VALUES (1685832211347771393, 1685828895406526466, 'reportTask_pause', '暂停任务', '暂停任务', 7, 1404697301888294914, '2023-07-31 09:58:15', NULL, '2023-07-31 09:58:15', 1);
INSERT INTO `sys_api` VALUES (1685832268834902017, 1685828895406526466, 'reportTask_resume', '恢复任务', '恢复任务', 8, 1404697301888294914, '2023-07-31 09:58:29', NULL, '2023-07-31 09:58:29', 1);
INSERT INTO `sys_api` VALUES (1685833914331566081, 1685828895406526466, 'reportTask_insert', '新增', '新增', 9, 1404697301888294914, '2023-07-31 10:05:01', NULL, '2023-07-31 10:05:01', 1);
INSERT INTO `sys_api` VALUES (1685845716071284738, 1405300363866714113, 'reportTpl_Task', '定时任务', '定时任务', 11, 1404697301888294914, '2023-07-31 10:51:55', NULL, '2023-07-31 10:51:55', 1);
INSERT INTO `sys_api` VALUES (1739993909432143874, 1738138171403386882, 'sysPost_search', '查询/重置', '查询/重置', 1, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739993959939952641, 1738138171403386882, 'sysPost_insert', '新增', '新增数据', 2, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739994056689963010, 1738138171403386882, 'sysPost_delete', '删除', '删除数据', 3, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739994174747037698, 1738138171403386882, 'sysPost_getDetail', '查看', '查看', 4, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739994237296693249, 1738138171403386882, 'sysPost_edit', '编辑', '修改数据', 5, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739997959435309058, 1738779159977222145, 'sysDept_search', '查询/重置', '查询数据', 1, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998026573533186, 1738779159977222145, 'sysDept_insert', '新增', '添加数据', 2, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998100393283586, 1738779159977222145, 'sysDept_getDetail', '查看', '查看详情', 3, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998153384120322, 1738779159977222145, 'sysDept_edit', '编辑', '修改数据', 4, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998195641733121, 1738779159977222145, 'sysDept_delete', '删除', '删除数据', 5, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998559690543106, 1738480515709292545, 'sysMerchantAuth_search', '查询/重置', '查询数据', 1, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998614266826754, 1738480515709292545, 'sysMerchantAuth_insert', '新增', '添加数据', 2, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998670671826945, 1738480515709292545, 'sysMerchantAuth_getDetail', '查看', '查看详情', 3, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998730897838081, 1738480515709292545, 'sysMerchantAuth_edit', '编辑', '修改数据', 4, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739998811705298946, 1738480515709292545, 'sysMerchantAuth_delete', '删除', '删除数据', 5, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1739999144154222593, 1738480923462750209, 'sysMerchant_search', '查询/重置', '查询数据', 1, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1740180704501420033, 1738480923462750209, 'sysMerchant_insert', '新增', '添加数据', 2, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1740180759971090434, 1738480923462750209, 'sysMerchant_delete', '删除', '删除数据', 3, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1740180868381265922, 1738480923462750209, 'sysMerchant_getDetail', '查看', '查看详情', 4, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1740180915231641601, 1738480923462750209, 'sysMerchant_edit', '编辑', '修改数据', 5, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_api` VALUES (1740618341397778433, 1405466770491432962, 'sysUser_resetPwd', '重置密码', '重置密码', 7, 1404697301888294915, '2023-12-29 14:18:46', NULL, '2023-12-29 14:18:46', 1);
INSERT INTO `sys_api` VALUES (1786058960034996225, 1786056695102541825, 'docTpl_search', '查询', '查询列表数据', 1, 1404697301888294915, '2024-05-02 23:43:34', NULL, '2024-05-02 23:43:34', 1);
INSERT INTO `sys_api` VALUES (1786059094290472961, 1786056695102541825, 'docTpl_getDetail', '查看', '查看详情', 2, 1404697301888294915, '2024-05-02 23:44:06', NULL, '2024-05-02 23:44:06', 1);
INSERT INTO `sys_api` VALUES (1786059196736348162, 1786056695102541825, 'docTpl_insert', '新增', '添加数据', 3, 1404697301888294915, '2024-05-02 23:44:31', NULL, '2024-05-02 23:44:31', 1);
INSERT INTO `sys_api` VALUES (1786059267339067394, 1786056695102541825, 'docTpl_edit', '编辑', '更新数据', 4, 1404697301888294915, '2024-05-02 23:44:47', NULL, '2024-05-02 23:44:47', 1);
INSERT INTO `sys_api` VALUES (1786059332069761026, 1786056695102541825, 'docTpl_delete', '删除', '删除数据', 5, 1404697301888294915, '2024-05-02 23:45:03', NULL, '2024-05-02 23:45:03', 1);
INSERT INTO `sys_api` VALUES (1786060173782687745, 1786056695102541825, 'docTpl_design', '设计', '模板设计', 6, 1404697301888294915, '2024-05-02 23:48:23', NULL, '2024-05-02 23:48:23', 1);
INSERT INTO `sys_api` VALUES (1788515089751187458, 1786056695102541825, 'docTpl_view', '报表查看', '报表查看', 7, 1404697301888294915, '2024-05-09 18:23:21', NULL, '2024-05-09 18:23:21', 1);
INSERT INTO `sys_api` VALUES (1788579205882814465, 1788578745335652354, 'docTpl_save', '保存模板', '保存模板', 1, 1404697301888294915, '2024-05-09 22:38:08', NULL, '2024-05-09 22:38:08', 1);
INSERT INTO `sys_api` VALUES (1788580233424379905, 1788578745335652354, 'docTpl_preview', '预览', '预览', 2, 1404697301888294915, '2024-05-09 22:42:13', NULL, '2024-05-09 22:42:13', 1);
INSERT INTO `sys_api` VALUES (1788580318971404289, 1788578745335652354, 'docTpl_export', '导出word模板', '导出word模板', 3, 1404697301888294915, '2024-05-09 22:42:33', NULL, '2024-05-09 22:42:33', 1);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商户号',
  `parent_id` bigint(20) NULL DEFAULT 0 COMMENT '父级id，一级id用0表示',
  `parent_id_list` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '父级id集合',
  `dept_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '部门名称',
  `leader` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `dept_sort` int(8) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '部门状态（1正常 2停用）',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1739206223138123778, 'SR00000000', 0, '0', 'SpringReport', 'cy', '1259541541', NULL, 1, 1, 1404697301888294915, '2023-12-25 16:47:31', 1404697301888294915, '2023-12-25 16:57:32', 1);
INSERT INTO `sys_dept` VALUES (1739206278586822657, 'SR00000000', 1739206594141089793, '0,1739206594141089793', '研发部', '1212', '1212', '', 1, 1, 1404697301888294915, '2023-12-25 16:47:44', 1404697301888294915, '2023-12-25 18:38:17', 1);
INSERT INTO `sys_dept` VALUES (1739206323621064705, 'SR00000000', 1739206594141089793, '0,1739206594141089793', '销售部', '12', '23', '', 2, 1, 1404697301888294915, '2023-12-25 16:47:55', 1404697301888294915, '2023-12-25 18:38:17', 1);
INSERT INTO `sys_dept` VALUES (1739206594141089793, 'SR00000000', 0, '0', 'SpringReport-青岛', 'cy', '1263485645', '', 2, 1, 1404697301888294915, '2023-12-25 16:49:00', 1404697301888294915, '2023-12-25 18:38:16', 1);
INSERT INTO `sys_dept` VALUES (1739206738546782209, 'SR00000000', 1739206594141089793, '0,1739206594141089793', '研发部-青岛', '大幅度', '12', NULL, 1, 1, 1404697301888294915, '2023-12-25 16:49:34', 1404697301888294915, '2023-12-25 18:38:17', 1);
INSERT INTO `sys_dept` VALUES (1739206822709686273, 'SR00000000', 1739206594141089793, '0,1739206594141089793', '销售部-青岛', '12', '12', '', 2, 1, 1404697301888294915, '2023-12-25 16:49:54', 1404697301888294915, '2023-12-25 18:38:17', 1);
INSERT INTO `sys_dept` VALUES (1739206985633230850, 'SR00000000', 1739206278586822657, '0,1739206594141089793,1739206278586822657', '研发一部', '12', '12', '', 1, 2, 1404697301888294915, '2023-12-25 16:50:33', 1404697301888294915, '2023-12-25 18:38:42', 1);
INSERT INTO `sys_dept` VALUES (1739207055527112705, 'SR00000000', 1739206278586822657, '0,1739206594141089793,1739206278586822657', '研发二部', '12', '12', NULL, 2, 1, 1404697301888294915, '2023-12-25 16:50:50', 1404697301888294915, '2023-12-25 18:38:17', 1);
INSERT INTO `sys_dept` VALUES (1739620317615865857, 'SR00000001', 0, '0', 'xxx公司', '12', '122323', '', 1, 1, 1404697301888294915, '2023-12-26 20:12:59', NULL, '2023-12-26 20:12:59', 1);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(20) NOT NULL,
  `menu_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `menu_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单url',
  `parent_menu_id` bigint(20) NULL DEFAULT 0 COMMENT '上级菜单id，一级菜单用0表示',
  `menu_icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `is_hidden` tinyint(4) NULL DEFAULT 2 COMMENT '是否隐藏 1是 2否',
  `sort` int(11) NULL DEFAULT NULL COMMENT '排序',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1404722852271263746, '报表', '/report', 0, 'icon-chart-line', 2, 1, NULL, '2021-06-15 16:50:01', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405299504554487810, '报表类型管理', '/reportType', 1404722852271263746, 'icon-table-report', 2, 1, 1404697301888294914, '2021-06-17 07:01:26', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405299619029626882, '数据源管理', '/reportDatasource', 1404722852271263746, 'icon-data', 2, 2, 1404697301888294914, '2021-06-17 07:01:53', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405300363866714113, '报表模板管理', '/reportTpl', 1404722852271263746, 'icon-table', 2, 3, 1404697301888294914, '2021-06-17 07:04:51', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405315562552209410, '系统管理', '/system', 0, 'icon-system', 2, 2, 1404697301888294914, '2021-06-17 08:05:14', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405466770491432962, '用户管理', '/sysUser', 1405315562552209410, 'icon-peoples', 2, 1, 1404697301888294914, '2021-06-17 18:06:05', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405466871968423938, '角色管理', '/sysRole', 1405315562552209410, 'icon-user', 2, 2, 1404697301888294914, '2021-06-17 18:06:29', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405466972459753473, '菜单管理', '/sysMenu', 1405315562552209410, 'icon-music-list', 2, 3, 1404697301888294914, '2021-06-17 18:06:53', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405467087488540673, '菜单功能', '/sysApi', 1405315562552209410, 'el-icon-menu', 1, 4, 1404697301888294914, '2021-06-17 18:07:21', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1405470840950218754, '报表设计', '/reportDesign', 1404722852271263746, 'el-icon-pie-chart', 1, 4, 1404697301888294914, '2021-06-17 07:50:39', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1421971956013498370, '大屏模板管理', '/screenTpl', 1404722852271263746, 'icon-full-screen', 2, 5, 1404697301888294914, '2021-08-02 07:11:48', NULL, NULL, 2);
INSERT INTO `sys_menu` VALUES (1424270599875547138, '大屏设计页面', '/screenDesign', 1404722852271263746, '12', 1, 6, 1404697301888294914, '2021-08-08 15:25:47', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1424270751675797506, '查看大屏页面', '/screenView', 1404722852271263746, '12', 1, 7, 1404697301888294914, '2021-08-08 15:26:24', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1437685292558557185, '打印模板管理', '/printTpl', 1404722852271263746, 'el-icon-printer', 2, 12, 1404697301888294914, '2021-09-14 15:50:59', NULL, NULL, 2);
INSERT INTO `sys_menu` VALUES (1465604831325470721, '大屏模板组件', '/screenContent', 1404722852271263746, 'icon', 1, 8, 1404697301888294914, '2021-11-30 16:53:16', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1534817266438144001, '多大屏管理', '/multiScreen', 1404722852271263746, 'icon-multi-rectangle', 2, 9, 1404697301888294914, '2022-06-09 16:38:47', NULL, NULL, 2);
INSERT INTO `sys_menu` VALUES (1544474827944828930, '报表查看', '/viewReport', 1404722852271263746, 'icon-eyes', 2, 5, 1404697301888294914, '2022-07-06 08:14:29', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1567288878307405825, '图标', '/iconPark', 0, 'icon-like', 2, 3, 1404697301888294914, '2022-09-07 07:09:22', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1597384623035232257, '报表填报', '/luckyReportFroms', 1404722852271263746, 'icon', 1, 10, 1404697301888294914, '2022-11-29 08:19:07', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1597389706904973314, '填报报表预览', '/luckyReportFromsPreview', 1404722852271263746, 'icon', 1, 13, 1404697301888294914, '2022-11-29 08:39:19', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1597419165079023618, '数据字典类型', '/reportDatasourceDictType', 1404722852271263746, 'icon', 1, 1, 1404697301888294914, '2022-11-29 10:36:22', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1597419308230619138, '数据字典值', '/reportDatasourceDictData', 1404722852271263746, 'icon', 1, 1, 1404697301888294914, '2022-11-29 10:36:56', NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1626461319411445762, '协同文档管理', '/onlineTpl', 1404722852271263746, 'icon-excel', 2, 3, 1404697301888294914, '2023-02-17 13:59:31', 1404697301888294914, '2023-02-22 07:31:03', 1);
INSERT INTO `sys_menu` VALUES (1685828895406526466, '报表定时任务', '/reportTask', 1404722852271263746, 'icon', 1, 14, 1404697301888294914, '2023-07-31 09:45:05', NULL, '2023-07-31 09:45:05', 1);
INSERT INTO `sys_menu` VALUES (1738138171403386882, '岗位管理', '/sysPost', 1405315562552209410, 'icon-user-positioning', 2, 4, 1404697301888294914, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1738480133297819649, '租户管理', '/merchant', 0, 'icon-shop', 2, 2, 1404697301888294915, NULL, 1404697301888294915, NULL, 1);
INSERT INTO `sys_menu` VALUES (1738480515709292545, '权限模板', '/sysMerchantAuthTemplate', 1738480133297819649, 'icon-permissions', 2, 1, 1404697301888294915, NULL, 1404697301888294915, NULL, 1);
INSERT INTO `sys_menu` VALUES (1738480923462750209, '租户管理', '/sysMerchant', 1738480133297819649, 'icon-user-business', 2, 2, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1738779159977222145, '部门管理', '/sysDept', 1405315562552209410, 'icon-network-tree', 2, 5, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO `sys_menu` VALUES (1786056695102541825, 'word模板管理', '/docTpl', 1404722852271263746, 'icon-word', 2, 3, 1404697301888294915, '2024-05-02 23:34:34', 1404697301888294925, '2024-05-27 21:37:27', 1);
INSERT INTO `sys_menu` VALUES (1788578745335652354, 'word模板设计', '/docDesign', 1404722852271263746, 'icon', 1, 3, 1404697301888294915, '2024-05-09 22:36:18', 1404697301888294925, '2024-05-27 21:37:34', 1);

-- ----------------------------
-- Table structure for sys_merchant
-- ----------------------------
DROP TABLE IF EXISTS `sys_merchant`;
CREATE TABLE `sys_merchant`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `merchant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '租户名称',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态 1启用 2禁用',
  `auth_template` bigint(20) NULL DEFAULT NULL COMMENT '权限模板',
  `is_system_merchant` tinyint(4) NULL DEFAULT 2 COMMENT '是否系统默认租户 1是 2否',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_merchant
-- ----------------------------
INSERT INTO `sys_merchant` VALUES (1542700042843824130, 'SR00000000', '系统默认商户', '11111111112', '1212@123.com', 1, NULL, 1, NULL, NULL, 1404697301888294915, '2023-12-24 11:35:20', 1);
INSERT INTO `sys_merchant` VALUES (1542700042843824131, 'SR00000001', '测试商户', '11111111111', '1212@123.com', 1, 1738526339965362177, 2, NULL, NULL, 1404697301888294915, '2023-12-24 12:02:01', 1);
INSERT INTO `sys_merchant` VALUES (1738774719438217218, 'SR00000002', '测试2', '12354141426', 'sdfa@1212.com', 2, 1738526339965362177, 2, 1404697301888294915, '2023-12-24 12:12:53', 1404697301888294915, '2023-12-25 08:40:56', 1);

-- ----------------------------
-- Table structure for sys_merchant_auth_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_merchant_auth_template`;
CREATE TABLE `sys_merchant_auth_template`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `template_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板名称',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_merchant_auth_template
-- ----------------------------
INSERT INTO `sys_merchant_auth_template` VALUES (1738526339965362177, 'VVIP模板', 1404697301888294915, '2023-12-23 19:45:54', 1404697301888294925, '2023-12-29 12:14:58', 1);

-- ----------------------------
-- Table structure for sys_merchant_auth_template_ids
-- ----------------------------
DROP TABLE IF EXISTS `sys_merchant_auth_template_ids`;
CREATE TABLE `sys_merchant_auth_template_ids`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `template_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `auth_type` tinyint(4) NULL DEFAULT NULL COMMENT '1菜单 2按钮',
  `auth_id` bigint(20) NULL DEFAULT NULL COMMENT '权限id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_merchant_auth_template_ids
-- ----------------------------
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738541511815712770, 1738526339965362177, 1, 1404722852271263746, 1404697301888294915, '2023-12-23 20:46:12', NULL, '2023-12-23 20:46:12', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738541511815712771, 1738526339965362177, 1, 1405299504554487810, 1404697301888294915, '2023-12-23 20:46:12', NULL, '2023-12-23 20:46:12', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738541511815712772, 1738526339965362177, 2, 1405301141893328897, 1404697301888294915, '2023-12-23 20:46:12', 1404697301888294915, '2023-12-23 20:48:01', 2);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738541896244645890, 1738526339965362177, 2, 1405301314702848002, 1404697301888294915, '2023-12-23 20:47:43', 1404697301888294915, '2023-12-23 20:49:28', 2);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738542289502588930, 1738526339965362177, 2, 1405301498308505602, 1404697301888294915, '2023-12-23 20:49:17', 1404697301888294915, '2023-12-23 20:49:28', 2);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738542289502588931, 1738526339965362177, 2, 1405301717527998466, 1404697301888294915, '2023-12-23 20:49:17', NULL, '2023-12-23 20:49:17', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738542334520053761, 1738526339965362177, 2, 1405301858293035009, 1404697301888294915, '2023-12-23 20:49:28', 1404697301888294915, '2023-12-23 20:49:39', 2);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738542334520053762, 1738526339965362177, 2, 1405302046789251074, 1404697301888294915, '2023-12-23 20:49:28', 1404697301888294915, '2023-12-23 20:49:39', 2);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738542379675930626, 1738526339965362177, 2, 1405301141893328897, 1404697301888294915, '2023-12-23 20:49:39', NULL, '2023-12-23 20:49:39', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738542379684319233, 1738526339965362177, 2, 1405301314702848002, 1404697301888294915, '2023-12-23 20:49:39', NULL, '2023-12-23 20:49:39', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1738542379684319234, 1738526339965362177, 2, 1405301498308505602, 1404697301888294915, '2023-12-23 20:49:39', NULL, '2023-12-23 20:49:39', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1740587185637945345, 1738526339965362177, 1, 1597419165079023618, 1404697301888294925, '2023-12-29 12:14:58', NULL, '2023-12-29 12:14:58', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1740587185637945346, 1738526339965362177, 2, 1597420289181855745, 1404697301888294925, '2023-12-29 12:14:58', NULL, '2023-12-29 12:14:58', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1740587185637945347, 1738526339965362177, 2, 1597423867028025345, 1404697301888294925, '2023-12-29 12:14:58', NULL, '2023-12-29 12:14:58', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1740587185637945348, 1738526339965362177, 2, 1597424940950204417, 1404697301888294925, '2023-12-29 12:14:58', NULL, '2023-12-29 12:14:58', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1740587185637945349, 1738526339965362177, 2, 1597425245129519106, 1404697301888294925, '2023-12-29 12:14:58', NULL, '2023-12-29 12:14:58', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1740587185646333954, 1738526339965362177, 2, 1597425770235408386, 1404697301888294925, '2023-12-29 12:14:58', NULL, '2023-12-29 12:14:58', 1);
INSERT INTO `sys_merchant_auth_template_ids` VALUES (1740587185646333955, 1738526339965362177, 2, 1597426240341389313, 1404697301888294925, '2023-12-29 12:14:58', NULL, '2023-12-29 12:14:58', 1);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `post_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '岗位编码',
  `post_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '岗位名称',
  `post_sort` int(6) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(4) NULL DEFAULT 1 COMMENT '状态（1正常 2停用）',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `role_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色代码',
  `role_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_desc` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色描述',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_api
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_api`;
CREATE TABLE `sys_role_api`  (
  `id` bigint(20) NOT NULL,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `api_id` bigint(20) NULL DEFAULT NULL COMMENT '接口id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_api
-- ----------------------------
INSERT INTO `sys_role_api` VALUES (1542699964863324162, NULL, 1542699406546935809, 1405301141893328897, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964863324163, NULL, 1542699406546935809, 1405301717527998466, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964863324164, NULL, 1542699406546935809, 1405303174180417538, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964863324165, NULL, 1542699406546935809, 1405303824343674881, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964863324166, NULL, 1542699406546935809, 1405304751960141826, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964863324167, NULL, 1542699406546935809, 1405305203900592129, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964863324168, NULL, 1542699406546935809, 1405311658913476610, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433026, NULL, 1542699406546935809, 1494555721453289474, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433027, NULL, 1542699406546935809, 1542332626318053378, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433028, NULL, 1542699406546935809, 1424165862295486465, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433029, NULL, 1542699406546935809, 1424221114415726593, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433030, NULL, 1542699406546935809, 1424221497850609666, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433031, NULL, 1542699406546935809, 1465561208529735682, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433032, NULL, 1542699406546935809, 1465572262567510018, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433033, NULL, 1542699406546935809, 1542348573812621314, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433034, NULL, 1542699406546935809, 1542381974942810114, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433035, NULL, 1542699406546935809, 1465605114222886913, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433036, NULL, 1542699406546935809, 1542403812838313985, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433037, NULL, 1542699406546935809, 1542404685916246017, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433038, NULL, 1542699406546935809, 1405467399616061442, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433039, NULL, 1542699406546935809, 1405467677505478657, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964930433040, NULL, 1542699406546935809, 1405468185620242433, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964993347586, NULL, 1542699406546935809, 1405468475530534913, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964993347587, NULL, 1542699406546935809, 1405468838434299906, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964993347588, NULL, 1542699406546935809, 1405469182782464002, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964993347589, NULL, 1542699406546935809, 1405470685538672641, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964993347590, NULL, 1542699406546935809, 1405469928080924673, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1542699964993347591, NULL, 1542699406546935809, 1405470256549453826, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1552086758009630721, NULL, 1542699406546935809, 1546647225540489217, 1404697301888294914, '2022-07-27 08:21:34', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1552086758009630722, NULL, 1542699406546935809, 1546647339193544705, 1404697301888294914, '2022-07-27 08:21:34', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1554254523034341377, NULL, 1542699406546935809, 1439113797250957314, 1404697301888294914, '2022-08-02 07:55:30', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1554254523034341378, NULL, 1542699406546935809, 1439114339805151233, 1404697301888294914, '2022-08-02 07:55:30', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1554254523034341379, NULL, 1542699406546935809, 1439115742380408834, 1404697301888294914, '2022-08-02 07:55:30', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1554254523034341380, NULL, 1542699406546935809, 1439116359068925954, 1404697301888294914, '2022-08-02 07:55:30', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1554254523034341381, NULL, 1542699406546935809, 1463063459183767554, 1404697301888294914, '2022-08-02 07:55:30', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1562985945479815169, NULL, 1542699406546935809, 1495702676325826561, 1404697301888294914, '2022-08-26 10:11:03', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1562985945479815170, NULL, 1542699406546935809, 1542331800988078082, 1404697301888294914, '2022-08-26 10:11:03', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1562986145862688769, NULL, 1542699406546935809, 1405473959943725057, 1404697301888294914, '2022-08-26 10:11:51', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1562986439682072577, NULL, 1542699406546935809, 1405304871174844417, 1404697301888294914, '2022-08-26 10:13:01', NULL, NULL, 2);
INSERT INTO `sys_role_api` VALUES (1598494880307388418, NULL, 1542699406546935809, 1597389121065562113, 1404697301888294914, '2022-12-02 09:50:53', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1598494880307388419, NULL, 1542699406546935809, 1597391476154675202, 1404697301888294914, '2022-12-02 09:50:53', NULL, NULL, 1);
INSERT INTO `sys_role_api` VALUES (1641641911632560129, NULL, 1542699406546935809, 1626466088913936386, 1404697301888294914, '2023-03-31 11:21:47', NULL, '2023-03-31 11:21:47', 1);
INSERT INTO `sys_role_api` VALUES (1641641911649337346, NULL, 1542699406546935809, 1626467097987981313, 1404697301888294914, '2023-03-31 11:21:47', NULL, '2023-03-31 11:21:47', 1);
INSERT INTO `sys_role_api` VALUES (1641641911649337347, NULL, 1542699406546935809, 1626468015018024962, 1404697301888294914, '2023-03-31 11:21:47', NULL, '2023-03-31 11:21:47', 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `menu_id` bigint(20) NULL DEFAULT NULL COMMENT '菜单id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1542699964993347592, NULL, 1542699406546935809, 1404722852271263746, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347593, NULL, 1542699406546935809, 1405299504554487810, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347594, NULL, 1542699406546935809, 1405299619029626882, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347595, NULL, 1542699406546935809, 1405300363866714113, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347596, NULL, 1542699406546935809, 1405470840950218754, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347597, NULL, 1542699406546935809, 1421971956013498370, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347598, NULL, 1542699406546935809, 1424270599875547138, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347599, NULL, 1542699406546935809, 1465604831325470721, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347600, NULL, 1542699406546935809, 1534817266438144001, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699964993347601, NULL, 1542699406546935809, 1405315562552209410, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699965060456449, NULL, 1542699406546935809, 1405466770491432962, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699965060456450, NULL, 1542699406546935809, 1405466871968423938, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699965060456451, NULL, 1542699406546935809, 1405466972459753473, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1542699965060456452, NULL, 1542699406546935809, 1405467087488540673, 1404697301888294914, '2022-07-01 10:41:48', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1552086758043185154, NULL, 1542699406546935809, 1544474827944828930, 1404697301888294914, '2022-07-27 08:21:34', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1554254523080478721, NULL, 1542699406546935809, 1437685292558557185, 1404697301888294914, '2022-08-02 07:55:30', NULL, NULL, 2);
INSERT INTO `sys_role_menu` VALUES (1598494880332554241, NULL, 1542699406546935809, 1597384623035232257, 1404697301888294914, '2022-12-02 09:50:53', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1598494880332554242, NULL, 1542699406546935809, 1597389706904973314, 1404697301888294914, '2022-12-02 09:50:53', NULL, NULL, 1);
INSERT INTO `sys_role_menu` VALUES (1641641911678697474, NULL, 1542699406546935809, 1626461319411445762, 1404697301888294914, '2023-03-31 11:21:47', NULL, '2023-03-31 11:21:47', 1);

-- ----------------------------
-- Table structure for sys_role_report
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_report`;
CREATE TABLE `sys_role_report`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `report_id` bigint(20) NULL DEFAULT NULL COMMENT '报表id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_role_sheet
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_sheet`;
CREATE TABLE `sys_role_sheet`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `report_id` bigint(20) NULL DEFAULT NULL COMMENT '报表id',
  `sheet_id` bigint(20) NULL DEFAULT NULL COMMENT 'sheet id',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(20) NOT NULL,
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `user_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户登录名',
  `user_real_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户真实姓名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户密码',
  `user_email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户邮箱，唯一',
  `user_phone` varchar(15) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '座机',
  `user_mobile` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机，唯一',
  `user_head_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像文件名称',
  `user_head` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像路径',
  `user_locked` tinyint(4) NULL DEFAULT 2 COMMENT '用户是否锁定 1是 2否',
  `last_login_time` bigint(20) NULL DEFAULT NULL COMMENT '上次登录时间',
  `attempt` int(11) NULL DEFAULT NULL COMMENT '账户尝试登录次数',
  `is_admin` tinyint(4) NULL DEFAULT 2 COMMENT '是否超级管理员 1是 2否',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1404697301888294914, NULL, 'admin', 'admin', '14e1b600b1fd579f47433b88e8d85291', '', '', '12300000000', NULL, NULL, 2, 1623740909416, NULL, 1, NULL, '2021-06-15 15:08:29', NULL, NULL, 1);
INSERT INTO `sys_user` VALUES (1404697301888294925, 'SR00000000', 'madmin', 'madmin', '14e1b600b1fd579f47433b88e8d85291', '', '', '12300000000', NULL, NULL, 2, 1623740909416, NULL, 1, NULL, '2021-06-15 15:08:29', NULL, NULL, 1);

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `dept_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_post`;
CREATE TABLE `sys_user_post`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户id',
  `post_id` bigint(20) NULL DEFAULT NULL COMMENT '岗位id',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `user_id` bigint(20) NULL DEFAULT NULL COMMENT '用户表主键',
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色表主键',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- 20240626 协同文档新增filterrowhidden属性 START
ALTER TABLE luckysheet ADD COLUMN filterrowhidden text DEFAULT NULL COMMENT '过滤隐藏行';
-- 20240626 协同文档新增filterrowhidden属性 end
