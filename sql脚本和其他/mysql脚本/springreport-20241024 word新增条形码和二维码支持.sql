DROP TABLE IF EXISTS `doc_tpl_codes`;
CREATE TABLE `doc_tpl_codes`  (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `code_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '条形码/二维码对应的图片链接',
  `code_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `dataset_id` bigint(20) NULL DEFAULT NULL COMMENT '数据集id',
  `dataset_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据集名称',
  `value_field` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数值字段',
  `code_type` tinyint(20) NULL DEFAULT 1 COMMENT '类型 1条形码 2二维码',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;