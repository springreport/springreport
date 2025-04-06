ALTER TABLE screen_tpl ADD COLUMN is_template tinyint(4) DEFAULT 2 COMMENT '是否是模板 1是 2否'; 
ALTER TABLE screen_tpl ADD COLUMN template_field bigint(20) DEFAULT NULL COMMENT '模板所属行业'; 

ALTER TABLE doc_tpl ADD COLUMN is_template tinyint(4) DEFAULT 2 COMMENT '是否是模板 1是 2否'; 
ALTER TABLE doc_tpl ADD COLUMN template_field bigint(20) DEFAULT NULL COMMENT '模板所属行业'; 

ALTER TABLE doc_tpl_settings MODIFY main longtext;

INSERT INTO sys_menu(id, menu_name, menu_url, parent_menu_id, menu_icon, is_hidden, sort, creator, create_time, updater, update_time, del_flag) VALUES (1901833495153209346, 'Excel模板市场', 'excelTemplate', 1404722852271263746, 'icon-a-Excelxietongwendang', 2, 15, 1404697301888294915, '2025-03-18 11:10:14', 1404697301888294915, '2025-04-01 08:35:23', 1);
INSERT INTO sys_menu(id, menu_name, menu_url, parent_menu_id, menu_icon, is_hidden, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906687919629529090, '模板市场', 'templateStore', 1404722852271263746, 'templateStore', 1, 16, 1404697301888294915, '2025-03-31 20:39:59', 1404697301888294915, '2025-03-31 20:40:32', 1);
INSERT INTO sys_menu(id, menu_name, menu_url, parent_menu_id, menu_icon, is_hidden, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906877534168674305, 'Word模板市场', 'wordTemplate', 1404722852271263746, 'icon-a-Wordbaobiao', 2, 17, 1404697301888294915, '2025-04-01 09:13:27', 1404697301888294915, '2025-04-01 09:14:12', 1);
INSERT INTO sys_menu(id, menu_name, menu_url, parent_menu_id, menu_icon, is_hidden, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907765030230409217, '大屏模板市场', 'screenTemplate', 1404722852271263746, 'icon-dapingmobanguanli', 2, 18, 1404697301888294915, '2025-04-03 20:00:02', 1404697301888294915, '2025-04-03 20:00:20', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906690056912629761, 1906687919629529090, 'template_market', '模板市场', '模板市场', 1, 1404697301888294915, '2025-03-31 20:48:29', NULL, '2025-03-31 20:48:29', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906868662754340866, 1901833495153209346, 'excelTemplate_search', '查询', '查询', 1, 1404697301888294915, '2025-04-01 08:38:12', NULL, '2025-04-01 08:38:12', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906868868120047617, 1901833495153209346, 'excelTemplate_folder', '新建分类', '新建分类', 2, 1404697301888294915, '2025-04-01 08:39:01', NULL, '2025-04-01 08:39:01', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906868957639077889, 1901833495153209346, 'excelTemplate_insert', '新建模板', '新建模板', 3, 1404697301888294915, '2025-04-01 08:39:22', NULL, '2025-04-01 08:39:22', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906869021375721474, 1901833495153209346, 'excelTemplate_getDetail', '查看', '查看', 4, 1404697301888294915, '2025-04-01 08:39:37', NULL, '2025-04-01 08:39:37', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906869150757416961, 1901833495153209346, 'excelTemplate_update', '编辑', '更新数据', 5, 1404697301888294915, '2025-04-01 08:40:08', 1404697301888294915, '2025-04-01 08:40:25', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906869309247582209, 1901833495153209346, 'excelTemplate_deleteOne', '删除', '删除', 6, 1404697301888294915, '2025-04-01 08:40:46', NULL, '2025-04-01 08:40:46', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906869418177851393, 1901833495153209346, 'excelTemplate_design', '报表设计', '报表设计', 7, 1404697301888294915, '2025-04-01 08:41:12', NULL, '2025-04-01 08:41:12', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906869565767020545, 1901833495153209346, 'excelTemplate_viewReport', '报表查看', '报表查看', 8, 1404697301888294915, '2025-04-01 08:41:47', NULL, '2025-04-01 08:41:47', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1906869672214261761, 1901833495153209346, 'excelTemplate_h5ViewReport', '报表查看(手机)', '报表查看(手机)', 9, 1404697301888294915, '2025-04-01 08:42:12', 1404697301888294915, '2025-04-01 09:01:13', 2);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907345186464874497, 1906877534168674305, 'wordTemplate_search', '查询', '查询', 1, 1404697301888294915, '2025-04-02 16:11:44', NULL, '2025-04-02 16:11:44', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907345511527628802, 1906877534168674305, 'wordTemplate_folder', '新建分类', '新建分类', 2, 1404697301888294915, '2025-04-02 16:13:01', NULL, '2025-04-02 16:13:01', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907345735100809217, 1906877534168674305, 'wordTemplate_insert', '新建文档', '新建文档', 3, 1404697301888294915, '2025-04-02 16:13:55', NULL, '2025-04-02 16:13:55', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907346112877576193, 1906877534168674305, 'wordTemplate_getDetail', '查看', '查看', 4, 1404697301888294915, '2025-04-02 16:15:25', NULL, '2025-04-02 16:15:25', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907347185159782401, 1906877534168674305, 'wordTemplate_update', '编辑', '编辑', 5, 1404697301888294915, '2025-04-02 16:19:40', NULL, '2025-04-02 16:19:40', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907347271671496706, 1906877534168674305, 'wordTemplate_design', '设计', '设计', 6, 1404697301888294915, '2025-04-02 16:20:01', NULL, '2025-04-02 16:20:01', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907347445768667138, 1906877534168674305, 'wordTemplate_view', '查看报表', '查看报表', 7, 1404697301888294915, '2025-04-02 16:20:43', NULL, '2025-04-02 16:20:43', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907347492241555457, 1906877534168674305, 'wordTemplate_delete', '删除', '删除', 8, 1404697301888294915, '2025-04-02 16:20:54', NULL, '2025-04-02 16:20:54', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907983586675105794, 1907765030230409217, 'screenTemplate_search', '查询', '查询', 1, 1404697301888294915, '2025-04-04 10:28:30', NULL, '2025-04-04 10:28:30', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907983664550748161, 1907765030230409217, 'screenTemplate_folder', '新建分类', '新建分类', 2, 1404697301888294915, '2025-04-04 10:28:49', NULL, '2025-04-04 10:28:49', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907983723640102914, 1907765030230409217, 'screenTemplate_insert', '新建模板', '新建模板', 3, 1404697301888294915, '2025-04-04 10:29:03', NULL, '2025-04-04 10:29:03', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907984239333974018, 1907765030230409217, 'screenTemplate_getDetail', '查看', '查看', 4, 1404697301888294915, '2025-04-04 10:31:06', NULL, '2025-04-04 10:31:06', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907984281990045697, 1907765030230409217, 'screenTemplate_edit', '编辑', '编辑', 5, 1404697301888294915, '2025-04-04 10:31:16', NULL, '2025-04-04 10:31:16', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907984341565939713, 1907765030230409217, 'screenTemplate_design', '大屏设计', '大屏设计', 6, 1404697301888294915, '2025-04-04 10:31:30', 1404697301888294915, '2025-04-04 10:32:16', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907984454325608450, 1907765030230409217, 'screenTemplate_view', '查看大屏', '查看大屏', 7, 1404697301888294915, '2025-04-04 10:31:57', NULL, '2025-04-04 10:31:57', 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1907984578330206210, 1907765030230409217, 'screenTemplate_delete', '删除', '删除', 8, 1404697301888294915, '2025-04-04 10:32:27', NULL, '2025-04-04 10:32:27', 1);

DROP TABLE IF EXISTS `springreport_field`;
CREATE TABLE `springreport_field`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `merchant_no` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商户号',
  `field_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分类名称',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '分类 1excel报表 2word报表 3大屏',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;
